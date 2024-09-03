package Servlets;

import Modelo.*;
import Utilidades.JsonReader;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
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
                TbVehiculo vehiculo = espacio.getVehiculo();
                if (vehiculo != null) {
                    // Ahora puedes hacer lo que necesites con la placa del vehiculo
                    System.out.println("Para el espacio " + espacio.getId_espacio() + ", la placa del vehiculo es: " + vehiculo.getPlaca_vehiculo());
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

            JSONObject jsonObject = JsonReader.parsearJson(req);

            try {
                Integer idEspacio = jsonObject.getInt("espacio");
                String formType = jsonObject.getString("formType");

                TbEspacio espacio = controladora_logica.buscarEspacio(idEspacio);
                if (espacio == null) {
                    enviarRespuesta(resp, HttpServletResponse.SC_NOT_FOUND, "error", "Espacio no encontrado");
                    return;
                }

                switch (formType) {
                    case "add":
                        manejarAdd(req, resp, espacio, jsonObject);
                        break;
                    case "edit":
                        manejarEdit(resp, espacio, jsonObject);
                        break;
                    case "pay":
                        manejarPay(req, resp, espacio);
                        break;
                    case "report":
                        manejarReport(req, resp, espacio, jsonObject);
                        break;
                    default:
                        enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Tipo de formulario no válido");
                        break;
                }
            } catch (JSONException e) {
                enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Invalid JSON format");
            } catch (Exception e) {
                enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error en el servidor: " + e.getMessage());
            }
        }

        private void manejarAdd(HttpServletRequest req, HttpServletResponse resp, TbEspacio espacio, JSONObject jsonObject) throws IOException {
            try {
                Integer documento = Integer.valueOf(jsonObject.getString("documento"));
                int idVehiculo = Integer.parseInt(jsonObject.getString("idVehiculo"));
                String cantcascos = jsonObject.getString("cantcascos");

                Persona persona = controladora_logica.buscarusuario(documento);
                if (persona == null || persona.getVehiculos() == null) {
                    enviarRespuesta(resp, HttpServletResponse.SC_NOT_FOUND, "error", "Persona no encontrada o sin vehículos asociados");
                    return;
                }

                TbVehiculo vehiculoExistente = persona.getVehiculos().stream()
                        .filter(vehiculo -> vehiculo.getId_vehiculo() == idVehiculo)
                        .findFirst()
                        .orElse(null);

                if (vehiculoExistente == null) {
                    enviarRespuesta(resp, HttpServletResponse.SC_NOT_FOUND, "error", "Vehículo no encontrado para el documento dado");
                    return;
                }

                espacio.setVehiculo(vehiculoExistente);
                espacio.setCantidad_cascos(Integer.valueOf(cantcascos));
                espacio.setPersona(persona);
                espacio.setEstado_espacio("Ocupado");
                espacio.setHora_entrada(new Date());

                actualizarEspacioYEnviarRespuesta(resp, espacio);
            } catch (NumberFormatException e) {
                enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Formato de número incorrecto");
            }
        }

        private void manejarEdit(HttpServletResponse resp, TbEspacio espacio, JSONObject jsonObject) throws IOException {

            String cantcascos = jsonObject.getString("cantcascos");

            espacio.setCantidad_cascos(Integer.valueOf(cantcascos));

            actualizarEspacioYEnviarRespuesta(resp, espacio);
        }

        private void manejarPay(HttpServletRequest req, HttpServletResponse resp, TbEspacio espacio) throws IOException {
            System.out.println("Ingresando al método pay");

            // Crear un nuevo registro para la tabla TbRegistro
            TbRegistro nuevoRegistro = new TbRegistro();
            nuevoRegistro.setFecha_reporte(LocalDateTime.now());
            nuevoRegistro.setEspacio(espacio);
            nuevoRegistro.setVehiculo(espacio.getVehiculo());
            nuevoRegistro.setAprendiz(espacio.getPersona());

            // Verificar si la sesión es válida y tiene el atributo documento
            Persona colaborador = obtenerColaboradorDesdeSesion(req, resp);
            if (colaborador != null) {
                nuevoRegistro.setColaborador(colaborador);
                controladora_logica.CrearRegistro(nuevoRegistro);
                limpiarYActualizarEspacio(resp, espacio);
            }else{
                System.out.println("No se puede crear el registro");
            }
        }


        private void manejarReport(HttpServletRequest req, HttpServletResponse resp, TbEspacio espacio, JSONObject jsonObject) throws IOException {
            System.out.println("Ingresando al método report");

            // Crear un nuevo registro para la tabla TbReportes
            TbReportes nuevoReporte = new TbReportes();
            nuevoReporte.setNombre_reporte(jsonObject.getString("tipoReporte"));

            nuevoReporte.setFecha_reporte(new Date());
            nuevoReporte.setNombre_reporte(jsonObject.getString("nombreReporte"));
            nuevoReporte.setDescripcion_reporte(jsonObject.getString("DescReporte"));
            nuevoReporte.setAprendiz(espacio.getPersona());
            nuevoReporte.setVehiculo(espacio.getVehiculo());
            System.out.println("Tipo del reporte "+ jsonObject.getString("tipoReporte"));
            System.out.println("Nombre del reporte "+ jsonObject.getString("nombreReporte"));
            System.out.println("Desc del reporte "+ jsonObject.getString("DescReporte"));
            System.out.println("Persona del reporte "+ espacio.getPersona());
            // Verificar si la sesión es válida y tiene el atributo documento
            Persona colaborador = obtenerColaboradorDesdeSesion(req, resp);
            System.out.println(colaborador);
            if (colaborador != null) {
                nuevoReporte.setColaborador(colaborador);
                controladora_logica.CrearReporte(nuevoReporte);
                limpiarYActualizarEspacio(resp, espacio);
            }
        }

        private Persona obtenerColaboradorDesdeSesion(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            HttpSession session = req.getSession(false); // false para no crear una nueva sesión si no existe
            if (session != null && session.getAttribute("documento") != null) {
                System.out.println("Documento conseguido" + session.getAttribute("documento"));
                Integer documentosesionactual = (Integer) session.getAttribute("documento");
                System.out.println("Documento " + documentosesionactual);
                Persona colaborador=null;
                try{
                    return controladora_logica.obtenerColaborador(documentosesionactual);
                } catch (Exception e){
                    System.out.println("Error al obtener colaborador");
                    System.out.println(e);
                }
                return colaborador;

            } else {
                // Manejar el caso donde no hay sesión o el atributo documento no está presente
                System.err.println("No se pudo obtener el número de documento de la sesión actual.");
                enviarRespuesta(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Sesión no válida o documento no encontrado");
                return null;
            }
        }

        private void limpiarYActualizarEspacio(HttpServletResponse resp, TbEspacio espacio) throws IOException {
            espacio.setPersona(null);
            espacio.setNombre(null);
            espacio.setVehiculo(null);
            espacio.setHora_entrada(null);
            espacio.setEstado_espacio("Libre");
            actualizarEspacioYEnviarRespuesta(resp, espacio);
        }

        private void actualizarEspacioYEnviarRespuesta(HttpServletResponse resp, TbEspacio espacio) throws IOException {
            try {
                boolean updated = controladora_logica.actualizarEspacio(espacio);
                if (updated) {
                    enviarRespuesta(resp, HttpServletResponse.SC_OK, "success", null);
                } else {
                    enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Failed to update space");
                }
            } catch (Exception e) {
                enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error al actualizar el espacio: " + e.getMessage());
            }
        }

        private void enviarRespuesta(HttpServletResponse resp, int statusCode, String status, String message) throws IOException {
            resp.setContentType("application/json");
            resp.setStatus(statusCode);
            String responseMessage = (message != null) ? String.format("{\"status\":\"%s\", \"message\":\"%s\"}", status, message) : String.format("{\"status\":\"%s\"}", status);
            System.out.println("Enviando respuesta: " + responseMessage); // Logging
            resp.getWriter().write(responseMessage);
            resp.getWriter().flush();
        }


    }
}