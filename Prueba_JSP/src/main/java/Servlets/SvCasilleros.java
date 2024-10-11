package Servlets;

import Logica.Logica_Espacios;
import Logica.Logica_Persona;
import Logica.Logica_Registro;
import Logica.Logica_Reportes;
import Modelo.*;
import Modelo.enums.EstadoEspacio;
import Modelo.enums.TipoReporte;
import Utilidades.EspacioServiceManager;
import Utilidades.JsonReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static Utilidades.sendResponse.enviarRespuesta;

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
                JSONArray jsonArray = new JSONArray();

                for (TbEspacio espacio : DatosEspacio) {
                    JSONObject jsonEspacio = new JSONObject();
                    jsonEspacio.put("id_espacio", espacio.getId_espacio());
                    jsonEspacio.put("nombre", espacio.getNombre());
                    // Vehículo
                    if (espacio.getVehiculo() != null) {
                        JSONObject vehiculo = new JSONObject();
                        vehiculo.put("id_vehiculo", espacio.getVehiculo().getId_vehiculo());
                        vehiculo.put("placa", espacio.getVehiculo().getPlacaVehiculo());
                        jsonEspacio.put("vehiculo", vehiculo);
                    } else {
                        jsonEspacio.put("vehiculo", JSONObject.NULL);
                    }
                    // Aprendiz (Persona)
                    if (espacio.getPersona() != null) {
                        JSONObject persona = new JSONObject();
                        persona.put("id_persona", espacio.getPersona().getId());
                        persona.put("nombreAprendiz", espacio.getPersona().getNombre());
                        persona.put("documento", espacio.getPersona().getDocumento());
                        persona.put("correo",espacio.getPersona().getCorreo());
                        persona.put("celular",espacio.getPersona().getCelular());
                        jsonEspacio.put("persona", persona);
                    } else {
                        jsonEspacio.put("persona", JSONObject.NULL);
                    }

                    jsonEspacio.put("hora_entrada", espacio.getHora_entrada() != null ? espacio.getHora_entrada().getTime() : JSONObject.NULL);
                    jsonEspacio.put("cantidad_cascos", espacio.getCantidad_cascos());
                    jsonEspacio.put("estado_espacio", espacio.getEstado_espacio().name());

                    // Sector
                    JSONObject sector = new JSONObject();
                    sector.put("id_sector", espacio.getSector().getId());
                    sector.put("nombre_sector", espacio.getSector().getNombreSector());
                    jsonEspacio.put("sector", sector);

                    jsonArray.put(jsonEspacio);
                }

                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonArray.toString());


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
                System.out.println("Traendo espacio");
                TbEspacio espacio = logica_espacios.buscarEspacio(idEspacio);
                System.out.println("Espacio traido");
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
                    case "liberar":
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
                int documento = jsonObject.getInt("documento");
                int idVehiculo = jsonObject.getInt("idVehiculo");
                int cantcascos = jsonObject.getInt("cantcascos");
                System.out.println("intentando Buscar persona:");
                Persona persona = logica_persona.buscarPersonaConDocumento(documento);
                System.out.println(persona.getId());
                if (persona == null || persona.getVehiculos() == null) {
                    enviarRespuesta(resp, HttpServletResponse.SC_NOT_FOUND, "error", "Persona no encontrada o sin vehículos asociados");
                    return;
                }

                TbVehiculo vehiculoExistente = logica_persona.buscarVehiculoPorId(persona, idVehiculo);
                if (vehiculoExistente == null) {
                    enviarRespuesta(resp, HttpServletResponse.SC_NOT_FOUND, "error", "Vehículo no encontrado para el documento dado");
                    return;
                }
                espacio.setVehiculo(vehiculoExistente);
                espacio.setCantidad_cascos(Integer.valueOf(cantcascos));
                espacio.setPersona(persona);
                espacio.setEstado_espacio(EstadoEspacio.Ocupado);
                espacio.setHora_entrada(new Date());

                boolean result =  actualizarEspacioYEnviarRespuesta(resp, espacio);
                if (result){
                    enviarRespuesta(resp,HttpServletResponse.SC_OK,"success","El vehiculo ha sido estacionado correctamente");
                } else{
                    enviarRespuesta(resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"error","Error inesperado al vincular el vehiculo al espacio");
                }
            } catch (NumberFormatException e) {
                enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Formato de número incorrecto");
            }
        }

        private void manejarEdit(HttpServletResponse resp, TbEspacio espacio, JSONObject jsonObject) throws IOException {

            String cantcascos = jsonObject.getString("cantcascos");

            espacio.setCantidad_cascos(Integer.valueOf(cantcascos));

            boolean result =  actualizarEspacioYEnviarRespuesta(resp, espacio);
            if (result){
                enviarRespuesta(resp,HttpServletResponse.SC_OK,"success","El vehiculo ha sido editado correctamente");
            } else{
                enviarRespuesta(resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"error","Error inesperado al editar el vehiculo ");
            }
        }

        private void manejarPay(HttpServletRequest req, HttpServletResponse resp, TbEspacio espacio) throws IOException {
            System.out.println("Ingresando al método pay");

            // Crear un nuevo registro para la tabla TbRegistro
            TbRegistro nuevoRegistro = new TbRegistro();
            nuevoRegistro.setFecha_registro(LocalDateTime.now());
            nuevoRegistro.setId_espacio(espacio.getId_espacio());
            nuevoRegistro.setPlacaVehiculo(espacio.getVehiculo().getPlacaVehiculo());
            nuevoRegistro.setAprendiz(espacio.getPersona());

            // Verificar si la sesión es válida y tiene el atributo documento
            Persona colaborador = obtenerColaboradorDesdeSesion(req, resp);
            if (colaborador != null) {
                nuevoRegistro.setGestor(colaborador);
                logica_registro.CrearRegistro(nuevoRegistro);
                limpiarYActualizarEspacio(resp, espacio);
                boolean result =  actualizarEspacioYEnviarRespuesta(resp, espacio);
                if (result){
                    enviarRespuesta(resp,HttpServletResponse.SC_OK,"success","El vehiculo ha sido liberado correctamente");
                } else{
                    enviarRespuesta(resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"error","Error inesperado al liberar el vehiculo del espacio");
                }
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
            nuevoReporte.setPlacaVehiculo(espacio.getVehiculo().getPlacaVehiculo());
            String tipoReporteStr = jsonObject.getString("tipoReporte");

            TipoReporte tipoReporte;
            try {
                tipoReporte = TipoReporte.valueOf(tipoReporteStr);
            } catch (IllegalArgumentException e) {
                enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Tipo de reporte inválido proporcionado.");
                return;
            }
            nuevoReporte.setTipoReporte(tipoReporte);
            // Verificar si la sesión es válida y tiene el atributo documento
            Persona colaborador = obtenerColaboradorDesdeSesion(req, resp);
            System.out.println(colaborador);
            if (colaborador != null) {
                nuevoReporte.setGestor(colaborador);
                logica_reportes.CrearReporte(nuevoReporte);
                limpiarYActualizarEspacio(resp, espacio);
                boolean result =  actualizarEspacioYEnviarRespuesta(resp, espacio);
                if (result){
                    enviarRespuesta(resp,HttpServletResponse.SC_OK,"success","El vehiculo ha sido reportado correctamente");
                } else{
                    enviarRespuesta(resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"error","Error inesperado al reportar el vehiculo");
                }
            }
        }

        private Persona obtenerColaboradorDesdeSesion(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            HttpSession session = req.getSession(false); // false para no crear una nueva sesión si no existe
            if (session != null && session.getAttribute("documento") != null) {
                Integer documentosesionactual = (Integer) session.getAttribute("documento");
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
                enviarRespuesta(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Sesión no válida o documento no encontrado");
                return null;
            }
        }
        private void limpiarYActualizarEspacio(HttpServletResponse resp, TbEspacio espacio) throws IOException {
            espacio.setPersona(null);
            espacio.setVehiculo(null);
            espacio.setHora_entrada(null);
            espacio.setEstado_espacio(EstadoEspacio.Libre);

        }
        private boolean actualizarEspacioYEnviarRespuesta(HttpServletResponse resp, TbEspacio espacio) throws IOException {
            try {
                boolean updated = logica_espacios.actualizarEspacio(espacio);
                return updated;
            } catch (Exception e) {
                enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error al actualizar el espacio: " + e.getMessage());
                return false;
            }
        }

    }
}