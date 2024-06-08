package Servlets;

import Modelo.Controladora_logica;
import Modelo.TbCasco;
import Modelo.TbEspacio;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class SvCasilleros {
    @WebServlet(name = "SvCasillero", urlPatterns = {"/SvCasillero"})

    public static class SvCasillero extends HttpServlet {
        Controladora_logica controladora_logica = new Controladora_logica();

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            List<TbEspacio> DatosEspacio= controladora_logica.DatosEspacio();
            for (TbEspacio espacio : DatosEspacio) {
            System.out.println("Ejecución servlet");
                TbCasco casco = espacio.getCasco();
                if (casco != null) {
                    // Si hay un casco asociado al espacio, obtener la placa
                    String placaCasco = casco.getPlaca_casco();
                    // Ahora puedes hacer lo que necesites con la placa del casco
                    System.out.println("Para el espacio " + espacio.getId() + ", la placa del casco es: " + placaCasco);
                } else {
                    // No hay ningún casco asociado a este espacio
                    System.out.println("Para el espacio " + espacio.getId() + ", no hay ningún casco asociado.");
                }
            }

            Integer CantidadCascos= controladora_logica.ObtenerEspacios(1);

            System.out.println("hola");
            request.setAttribute("Espacios", DatosEspacio);
            request.setAttribute("Casilleros", CantidadCascos);
            RequestDispatcher dispatcher = request.getRequestDispatcher("Casilleros.jsp");
            dispatcher.forward(request, response);

        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            System.out.println("INGRESANDO A METODO POST");
            BufferedReader reader = req.getReader();
            StringBuilder jsonBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
            String json = jsonBuilder.toString();
            System.out.println("acceder al objeto json");
            // Parsear el JSON usando org.json
            JSONObject jsonObject = new JSONObject(json);
            System.out.println("realizado objeto json");

            Integer idEspacio = jsonObject.getInt("espacio");
            System.out.println("idEspacio: " + idEspacio);

            String formType = jsonObject.getString("formType");
            System.out.println("tipo de form: "+formType);
            // Busca el espacio por ID
            System.out.println("Intentando Obtener el ID del espacio " + idEspacio);
            TbEspacio espacio = controladora_logica.buscarEspacio(idEspacio);
            System.out.println("Espacio " + espacio.getId());

            try {
                if (formType.equals("add")){
                    String placa = jsonObject.getString("placa");
                    String ciudad = jsonObject.getString("ciudad");
                    String cantcascos = jsonObject.getString("cantcascos");
                    if (espacio != null) {
                        // Busca el casco por placa
                        TbCasco casco = controladora_logica.buscarCascoPorPlaca(placa);

                        if (casco == null) {
                            // Si el casco no existe, crearlo
                            casco = new TbCasco();
                            casco.setPlaca_casco(placa);
                            casco.setCiudad(ciudad);
                            casco.setCant_casco(Integer.valueOf(cantcascos));
                            System.out.println(" intentando añadir casco");
                            controladora_logica.Crearcasco(casco);
                            System.out.println("casco añadido");
                        }

                        // Actualizar el espacio
                        espacio.setCasco(casco);
                        espacio.setEstado_espacio("Ocupado");
                        espacio.setHora_entrada(new Date());

                        // Guardar los cambios en el espacio
                        boolean added = controladora_logica.actualizarEspacio(espacio);
                        System.out.println("Espacio actualizado");

                        if (added) {
                            resp.setContentType("application/json");
                            resp.setStatus(HttpServletResponse.SC_OK);
                            resp.getWriter().write("{\"status\":\"success\"}");
                        } else {
                            resp.setContentType("application/json");
                            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            resp.getWriter().write("{\"status\":\"error\", \"message\":\"Failed to update space\"}");
                        }
                    } else {
                        resp.setContentType("application/json");
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().write("{\"status\":\"error\"," +
                                " \"message\":\"Espacio no encontrado\"}");
                    }
                } else if (formType.equals("edit")) {
                    String placa = jsonObject.getString("placa");
                    String ciudad = jsonObject.getString("ciudad");
                    String cantcascos = jsonObject.getString("cantcascos");
                    if (espacio != null) {
                        TbCasco casco = espacio.getCasco();
                        if (casco != null) {
                            casco.setPlaca_casco(placa);
                            casco.setCiudad(ciudad);
                            casco.setCant_casco(Integer.valueOf(cantcascos));
                            controladora_logica.actualizarCasco(casco);

                            espacio.setCasco(casco);

                            boolean updated = controladora_logica.actualizarEspacio(espacio);
                            if (updated) {
                                resp.setContentType("application/json");
                                resp.setStatus(HttpServletResponse.SC_OK);
                                resp.getWriter().write("{\"status\":\"success\"}");
                            } else {
                                resp.setContentType("application/json");
                                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                resp.getWriter().write("{\"status\":\"error\", \"message\":\"Failed to update space\"}");
                            }
                        } else {
                            resp.setContentType("application/json");
                            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                            resp.getWriter().write("{\"status\":\"error\", \"message\":\"Casco no encontrado\"}");
                        }
                    } else {
                        resp.setContentType("application/json");
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().write("{\"status\":\"error\", \"message\":\"Espacio no encontrado\"}");
                    }
                } else if (formType.equals("pay")) {
                    System.out.println("Ingresando al meotodo pay");
                    TbCasco casco = espacio.getCasco();
                    if (casco != null) {
                        int idCasco = casco.getId();
                        if (true) {
                            espacio.setId(espacio.getId());
                            espacio.setCasco(null);
                            espacio.setEstado_espacio("Activo");
                            boolean updated = controladora_logica.actualizarEspacio(espacio);
                            if (updated) {
                                controladora_logica.borrarCasco(idCasco);
                                resp.setContentType("application/json");
                                resp.setStatus(HttpServletResponse.SC_OK);
                                resp.getWriter().write("{\"status\":\"success\"}");
                            } else {
                                resp.setContentType("application/json");
                                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                                resp.getWriter().write("{\"status\":\"error\", \"message\":\"Failed to update space\"}");
                            }
                        } else {
                            resp.setContentType("application/json");
                            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            resp.getWriter().write("{\"status\":\"error\", \"message\":\"Failed to delete casco\"}");
                        }
                    } else {
                        resp.setContentType("application/json");
                        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                        resp.getWriter().write("{\"status\":\"error\", \"message\":\"Casco no encontrado\"}");
                    }
                }
            } catch (JSONException e) {
                System.err.println("Error al procesar el JSON: " + e.getMessage());
                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("{\"status\":\"error\", \"message\":\"Invalid JSON format\"}");
            } catch (Exception e) {
                System.err.println("Error en el servidor: " + e.getMessage());
                resp.setContentType("application/json");
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write("{\"status\":\"error\", \"message\":\"" + e.getMessage() + "\"}");
            }
        }
    }
}