package Servlets;

import Logica.Logica_Espacios;
import Logica.Logica_Persona;
import Logica.Logica_Registro;
import Logica.Logica_Reportes;
import Modelo.*;
import Modelo.enums.EstadoEspacio;
import Utilidades.EspacioServiceManager;
import Utilidades.JsonReader;
import Utilidades.sendResponse;
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
        Logica_Espacios logica_espacios = EspacioServiceManager.getInstance().getLogicaEspacios();
        Logica_Persona logica_persona = new Logica_Persona();
        Logica_Registro logica_registro = new Logica_Registro();
        Logica_Reportes logica_reportes = new Logica_Reportes();

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            try {
                List<TbEspacio> DatosEspacio= logica_espacios.DatosEspacio();
                for (TbEspacio espacio : DatosEspacio) {
                    System.out.println("Ejecución servlet");
                    TbVehiculo vehiculo = espacio.getVehiculo();
                    if (vehiculo != null) {
                        // Ahora puedes hacer lo que necesites con la placa del vehiculo
                        System.out.println("Para el espacio " + espacio.getId_espacio() + ", la placa del vehiculo es: " + vehiculo.getPlacaVehiculo());
                    } else {
                        // No hay ningún vehiculo asociado a este espacio
                        System.out.println("Para el espacio " + espacio.getId_espacio() + ", no hay ningún vehiculo asociado.");
                    }
                }
                System.out.println("hola");
                request.setAttribute("Espacios", DatosEspacio);
                RequestDispatcher dispatcher = request.getRequestDispatcher("Casilleros.jsp");
                dispatcher.forward(request, response);

            } catch (Exception e){
                System.out.println("Error al iterar sobre los espacios: " + e.getMessage());
            }



        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            System.out.println("INGRESANDO A METODO POST");

            JSONObject jsonObject = JsonReader.parsearJson(req);

            try {
                Integer idEspacio = jsonObject.getInt("espacio");
                String formType = jsonObject.getString("formType");
                TbEspacio espacio = logica_espacios.buscarEspacio(idEspacio);
                if (espacio == null) {
                    sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_NOT_FOUND, "error", "Espacio no encontrado");
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
                        sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Tipo de formulario no válido");
                        break;
                }
            } catch (JSONException e) {
                sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Invalid JSON format");
            } catch (Exception e) {
                sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error en el servidor: " + e.getMessage());
            }
        }
        private void manejarAdd(HttpServletRequest req, HttpServletResponse resp, TbEspacio espacio, JSONObject jsonObject) throws IOException {
            try {
                int documento = Integer.parseInt(jsonObject.getString("documento"));
                int idVehiculo = Integer.parseInt(jsonObject.getString("idVehiculo"));
                String cantcascos = jsonObject.getString("cantcascos");

                Persona persona = logica_persona.buscarpersona(documento);
                if (persona == null || persona.getVehiculos() == null) {
                    sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_NOT_FOUND, "error", "Persona no encontrada o sin vehículos asociados");
                    return;
                }

                TbVehiculo vehiculoExistente = logica_persona.buscarVehiculoPorId(persona, idVehiculo);

                if (vehiculoExistente == null) {
                    sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_NOT_FOUND, "error", "Vehículo no encontrado para el documento dado");
                    return;
                }
                espacio.setVehiculo(vehiculoExistente);
                espacio.setCantidad_cascos(Integer.valueOf(cantcascos));
                espacio.setPersona(persona);
                espacio.setEstado_espacio(EstadoEspacio.Ocupado);
                espacio.setHora_entrada(new Date());

                actualizarEspacioYEnviarRespuesta(resp, espacio);
            } catch (NumberFormatException e) {
                sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Formato de número incorrecto");
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
            nuevoRegistro.setId_espacio(espacio.getId_espacio());
            nuevoRegistro.setPlacaVehiculo(espacio.getVehiculo().getPlacaVehiculo());
            nuevoRegistro.setDocumentoAprendiz(espacio.getPersona().getDocumento());

            // Verificar si la sesión es válida y tiene el atributo documento
            Persona colaborador = obtenerColaboradorDesdeSesion(req, resp);
            if (colaborador != null) {
                nuevoRegistro.setDocumentoGestor(colaborador.getDocumento());
                logica_registro.CrearRegistro(nuevoRegistro);
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
            nuevoReporte.setDocumentoAprendiz(espacio.getPersona().getDocumento());
            nuevoReporte.setPlacaVehiculo(espacio.getVehiculo().getPlacaVehiculo());
            System.out.println("Tipo del reporte "+ jsonObject.getString("tipoReporte"));
            System.out.println("Nombre del reporte "+ jsonObject.getString("nombreReporte"));
            System.out.println("Desc del reporte "+ jsonObject.getString("DescReporte"));
            System.out.println("Persona del reporte "+ espacio.getPersona());
            // Verificar si la sesión es válida y tiene el atributo documento
            Persona colaborador = obtenerColaboradorDesdeSesion(req, resp);
            System.out.println(colaborador);
            if (colaborador != null) {
                nuevoReporte.setDocumentoColaborador(colaborador.getDocumento());
                logica_reportes.CrearReporte(nuevoReporte);
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
                    return logica_persona.obtenerColaborador(documentosesionactual);
                } catch (Exception e){
                    System.out.println("Error al obtener colaborador");
                    System.out.println(e);
                }
                return colaborador;

            } else {
                // Manejar el caso donde no hay sesión o el atributo documento no está presente
                System.err.println("No se pudo obtener el número de documento de la sesión actual.");
                sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Sesión no válida o documento no encontrado");
                return null;
            }
        }
        private void limpiarYActualizarEspacio(HttpServletResponse resp, TbEspacio espacio) throws IOException {
            espacio.setPersona(null);
            espacio.setNombre(null);
            espacio.setVehiculo(null);
            espacio.setHora_entrada(null);
            espacio.setEstado_espacio(EstadoEspacio.Libre);
            actualizarEspacioYEnviarRespuesta(resp, espacio);
        }

        private void actualizarEspacioYEnviarRespuesta(HttpServletResponse resp, TbEspacio espacio) throws IOException {
            try {
                boolean updated = logica_espacios.actualizarEspacio(espacio);
                if (updated) {
                    sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_OK, "success", null);
                } else {
                    sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Failed to update space");
                }
            } catch (Exception e) {
                sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error al actualizar el espacio: " + e.getMessage());
            }
        }

    }
}