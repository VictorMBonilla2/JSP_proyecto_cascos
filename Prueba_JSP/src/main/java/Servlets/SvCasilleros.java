package Servlets;

import Modelo.Controladora_logica;
import Modelo.TbVehiculo;
import Modelo.TbEspacio;
import com.sun.jdi.IntegerValue;
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
import java.sql.Struct;
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
                String placa_vehiculo = espacio.getPlaca_vehiculo();
                if (placa_vehiculo != null) {
                    // Ahora puedes hacer lo que necesites con la placa del vehiculo
                    System.out.println("Para el espacio " + espacio.getId_espacio() + ", la placa del vehiculo es: " + placa_vehiculo);
                } else {
                    // No hay ningún vehiculo asociado a este espacio
                    System.out.println("Para el espacio " + espacio.getId_espacio() + ", no hay ningún vehiculo asociado.");
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
            System.out.println("Espacio " + espacio.getId_espacio());

            try {
                if (formType.equals("add")) {
                    Integer documento = Integer.valueOf(jsonObject.getString("documento"));
                    String nombre = jsonObject.getString("nombre");
                    String placa = jsonObject.getString("placa");
                    String ciudad = jsonObject.getString("ciudad");
                    String cantcascos = jsonObject.getString("cantcascos");

                    if (espacio != null) {
                        // Verificar si el documento tiene un vehículo vinculado
                        TbVehiculo vehiculoExistente = null;
                        try {
                            vehiculoExistente = controladora_logica.buscarVehiculoPorDocumento(documento);
                        } catch (Exception e) {
                            // Manejar cualquier excepción que ocurra durante la búsqueda
                            System.err.println("Error al buscar vehículo por documento: " + e.getMessage());
                            resp.setContentType("application/json");
                            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            resp.getWriter().write("{\"status\":\"error\", \"message\":\"Error en la búsqueda del vehículo\"}");
                            return;
                        }

                        if (vehiculoExistente != null) {
                            // Si existe un vehículo vinculado al documento, usar su información
                            espacio.setPlaca_vehiculo(vehiculoExistente.getPlaca_vehiculo());
                            espacio.setCantidad_cascos(vehiculoExistente.getCant_casco());
                        } else {
                            // Si no existe un vehículo vinculado, usar la información proporcionada
                            espacio.setPlaca_vehiculo(placa);
                            espacio.setCantidad_cascos(Integer.valueOf(cantcascos));
                        }

                        // Asignar los demás datos al espacio
                        espacio.setNombre(nombre);
                        espacio.setDocumento_aprendiz(Integer.valueOf(documento));
                        espacio.setEstado_espacio("Ocupado");
                        espacio.setHora_entrada(new Date());

                        // Guardar los cambios en el espacio
                        boolean added = false;
                        try {
                            added = controladora_logica.actualizarEspacio(espacio);
                        } catch (Exception e) {
                            // Manejar cualquier excepción que ocurra durante la actualización
                            System.err.println("Error al actualizar el espacio: " + e.getMessage());
                            resp.setContentType("application/json");
                            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                            resp.getWriter().write("{\"status\":\"error\", \"message\":\"Error al actualizar el espacio\"}");
                            return;
                        }

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
                        resp.getWriter().write("{\"status\":\"error\", \"message\":\"Espacio no encontrado\"}");
                    }
                } else if (formType.equals("edit")) {
                    Integer documento = Integer.valueOf(jsonObject.getString("documento"));
                    String nombre = jsonObject.getString("nombre");
                    String placa = jsonObject.getString("placa");
                    String ciudad = jsonObject.getString("ciudad");
                    String cantcascos = jsonObject.getString("cantcascos");
                    if (espacio != null) {

                        espacio.setPlaca_vehiculo(placa);
                        espacio.setCantidad_cascos(Integer.valueOf(cantcascos));
                        espacio.setNombre(nombre);
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
                        resp.getWriter().write("{\"status\":\"error\", \"message\":\"Espacio no encontrado\"}");
                    }
                } else if (formType.equals("pay")) {
                    System.out.println("Ingresando al meotodo pay");
                    espacio.setId_espacio(espacio.getId_espacio());
                    espacio.setDocumento_aprendiz(null);
                    espacio.setNombre(null);
                    espacio.setPlaca_vehiculo(null);
                    espacio.setHora_entrada(null);
                    espacio.setEstado_espacio(null);
                    espacio.setEstado_espacio("Libre");
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