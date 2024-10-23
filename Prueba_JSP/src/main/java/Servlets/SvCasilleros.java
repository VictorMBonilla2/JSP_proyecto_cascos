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
import java.util.Date;
import java.util.List;

import static Utilidades.sendResponse.enviarRespuesta;
/**
 * Servlet encargado de gestionar las operaciones relacionadas con los casilleros
 * y sus espacios, tales como añadir, editar, liberar o reportar un vehículo en un espacio.
 */
public class SvCasilleros {
    @WebServlet(name = "SvCasillero", urlPatterns = {"/SvCasillero"})
    public static class SvCasillero extends HttpServlet {
        // Instancias de lógica utilizadas para manejar la lógica de negocio
        Logica_Espacios logica_espacios = EspacioServiceManager.getInstance().getLogicaEspacios();
        Logica_Persona logica_persona = new Logica_Persona();
        Logica_Registro logica_registro = new Logica_Registro();
        Logica_Reportes logica_reportes = new Logica_Reportes();
        /**
         * Método GET que obtiene la información de todos los espacios disponibles,
         * incluyendo la información del vehículo y la persona si está ocupada.
         *
         * @param request  La solicitud HTTP.
         * @param response La respuesta HTTP.
         * @throws ServletException Si ocurre un error en el servlet.
         * @throws IOException      Si ocurre un error al manejar la respuesta.
         */
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {
            try {
                // Obtiene la lista de espacios disponibles
                List<TbEspacio> DatosEspacio= logica_espacios.DatosEspacio();
                JSONArray jsonArray = new JSONArray();
                // Itera sobre los espacios y construye un JSON con los datos
                for (TbEspacio espacio : DatosEspacio) {
                    JSONObject jsonEspacio = new JSONObject();
                    jsonEspacio.put("id_espacio", espacio.getId_espacio());
                    jsonEspacio.put("nombre", espacio.getNombre());
                    // Agrega los detalles del vehículo y la persona si están presentes
                    if (espacio.getVehiculo() != null) {
                        JSONObject vehiculo = new JSONObject();
                        vehiculo.put("id_vehiculo", espacio.getVehiculo().getId_vehiculo());
                        vehiculo.put("placa", espacio.getVehiculo().getPlacaVehiculo());
                        jsonEspacio.put("vehiculo", vehiculo);

                        // Información de la persona asociada al vehículo
                        JSONObject persona = new JSONObject();
                        Persona usuario = logica_persona.buscarpersonaPorId(espacio.getVehiculo().getPersona().getId());
                        persona.put("id_persona", usuario.getId());
                        persona.put("nombreAprendiz", usuario.getNombre());
                        persona.put("documento", usuario.getDocumento());
                        persona.put("correo",usuario.getCorreo());
                        persona.put("celular",usuario.getCelular());
                        jsonEspacio.put("persona", persona);
                    } else {
                        jsonEspacio.put("vehiculo", JSONObject.NULL);
                        jsonEspacio.put("persona", JSONObject.NULL);
                    }
                    jsonEspacio.put("hora_entrada", espacio.getHora_entrada() != null ? espacio.getHora_entrada().getTime() : JSONObject.NULL);
                    jsonEspacio.put("cantidad_cascos", espacio.getCantidad_cascos());
                    jsonEspacio.put("estado_espacio", espacio.getEstado_espacio().name());

                    // Información del sector asociado al espacio
                    JSONObject sector = new JSONObject();
                    sector.put("id_sector", espacio.getSector().getId());
                    sector.put("nombre_sector", espacio.getSector().getNombreSector());
                    jsonEspacio.put("sector", sector);

                    jsonArray.put(jsonEspacio);
                }
                // Envía la respuesta en formato JSON
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonArray.toString());
            } catch (Exception e){
                System.out.println("Error al iterar sobre los espacios: " + e.getMessage());
            }
        }
        /**
         * Método POST que maneja diferentes acciones relacionadas con los espacios:
         * agregar, editar, liberar y reportar un vehículo.
         *
         * @param req  La solicitud HTTP.
         * @param resp La respuesta HTTP.
         * @throws ServletException Si ocurre un error en el servlet.
         * @throws IOException      Si ocurre un error al manejar la respuesta.
         */
        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            System.out.println("INGRESANDO A METODO POST");
            // Parsear el JSON de la solicitud
            JSONObject jsonObject = JsonReader.parsearJson(req);

            try {
                Integer idEspacio = jsonObject.getInt("espacio");
                String formType = jsonObject.getString("formType");

                // Busca el espacio por su ID
                TbEspacio espacio = logica_espacios.buscarEspacio(idEspacio);
                if (espacio == null) {
                    enviarRespuesta(resp, HttpServletResponse.SC_NOT_FOUND, "error", "Espacio no encontrado");
                    return;
                }
                // Manejar la acción en función del tipo de formulario
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

        /**
         * Maneja la acción de agregar un vehículo a un espacio.
         *
         * @param req        La solicitud HTTP.
         * @param resp       La respuesta HTTP.
         * @param espacio    El espacio a actualizar.
         * @param jsonObject El objeto JSON con los datos necesarios.
         * @throws IOException Si ocurre un error al manejar la respuesta.
         */
        private void manejarAdd(HttpServletRequest req, HttpServletResponse resp, TbEspacio espacio, JSONObject jsonObject) throws IOException {
            try {
                int documento = jsonObject.getInt("documento");
                int idVehiculo = jsonObject.getInt("idVehiculo");
                int cantcascos = jsonObject.getInt("cantcascos");

                // Buscar la persona por documento
                Persona persona = logica_persona.buscarPersonaConDocumento(documento);
                System.out.println(persona.getId());
                if (persona == null || persona.getVehiculos() == null) {
                    enviarRespuesta(resp, HttpServletResponse.SC_NOT_FOUND, "error", "Persona no encontrada o sin vehículos asociados");
                    return;
                }
                // Verificar si el usuario ya tiene un vehículo estacionado
                boolean Estacionado = logica_persona.UsuarioEnEstacionamiento(persona.getId());

                if(Estacionado){
                    enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST,"error","¡El usuario ya esta estacionado!");
                    return;
                }

                // Buscar el vehículo por ID
                TbVehiculo vehiculoExistente = logica_persona.buscarVehiculoPorId(persona, idVehiculo);
                if (vehiculoExistente == null) {
                    enviarRespuesta(resp, HttpServletResponse.SC_NOT_FOUND, "error", "Vehículo no encontrado para el documento dado");
                    return;
                }

                // Asignar el vehículo al espacio y actualizar los datos del espacio
                espacio.setVehiculo(vehiculoExistente);
                espacio.setCantidad_cascos(Integer.valueOf(cantcascos));
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

        // Método manejarEdit
        /**
         * Maneja la edición de los datos de un espacio, actualizando
         * la cantidad de cascos asignados al espacio.
         *
         * @param resp       La respuesta HTTP.
         * @param espacio    El espacio que se va a editar.
         * @param jsonObject El objeto JSON con los datos actualizados del espacio.
         * @throws IOException Si ocurre un error al manejar la respuesta.
         */
        private void manejarEdit(HttpServletResponse resp, TbEspacio espacio, JSONObject jsonObject) throws IOException {

            String cantcascos = jsonObject.getString("cantcascos");

            // Actualiza la cantidad de cascos del espacio
            espacio.setCantidad_cascos(Integer.valueOf(cantcascos));

            boolean result =  actualizarEspacioYEnviarRespuesta(resp, espacio);
            if (result){
                enviarRespuesta(resp,HttpServletResponse.SC_OK,"success","El vehiculo ha sido editado correctamente");
            } else{
                enviarRespuesta(resp,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"error","Error inesperado al editar el vehiculo ");
            }
        }

        // Método manejarPay
        /**
         * Maneja la acción de liberar un espacio y registrar la salida de un vehículo.
         * Se crea un nuevo registro en la tabla TbRegistro y se limpia el espacio.
         *
         * @param req     La solicitud HTTP.
         * @param resp    La respuesta HTTP.
         * @param espacio El espacio que se va a liberar.
         * @throws IOException Si ocurre un error al manejar la respuesta.
         */
        private void manejarPay(HttpServletRequest req, HttpServletResponse resp, TbEspacio espacio) throws IOException {
            System.out.println("Ingresando al método pay");

            // Crear un nuevo registro para la tabla TbRegistro
            TbRegistro nuevoRegistro = new TbRegistro();
            nuevoRegistro.setFechaRegistro(new Date());
            nuevoRegistro.setEspacio(espacio);
            nuevoRegistro.setVehiculo(espacio.getVehiculo());
            nuevoRegistro.setFechaEntrada(espacio.getHora_entrada());

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

        // Método manejarReport
        /**
         * Maneja la creación de un reporte asociado a un espacio.
         * Crea un registro en la tabla TbReportes basado en los datos proporcionados.
         *
         * @param req        La solicitud HTTP.
         * @param resp       La respuesta HTTP.
         * @param espacio    El espacio que se va a reportar.
         * @param jsonObject El objeto JSON con los datos del reporte.
         * @throws IOException Si ocurre un error al manejar la respuesta.
         */
        private void manejarReport(HttpServletRequest req, HttpServletResponse resp, TbEspacio espacio, JSONObject jsonObject) throws IOException {
            System.out.println("Ingresando al método report");

            // Crear un nuevo registro para la tabla TbReportes
            TbReportes nuevoReporte = new TbReportes();
            nuevoReporte.setNombre_reporte(jsonObject.getString("tipoReporte"));
            nuevoReporte.setFecha_reporte(new Date());
            nuevoReporte.setEspacio(espacio);
            nuevoReporte.setNombre_reporte(jsonObject.getString("nombreReporte"));
            nuevoReporte.setDescripcion_reporte(jsonObject.getString("DescReporte"));
            nuevoReporte.setVehiculo(espacio.getVehiculo());
            String tipoReporteStr = jsonObject.getString("tipoReporte");

            // Validar el tipo de reporte
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

        // Método obtenerColaboradorDesdeSesion
        /**
         * Obtiene el colaborador desde la sesión activa, basándose en el número de documento almacenado en la sesión.
         * Si no hay una sesión válida o el documento no está presente, envía una respuesta de error.
         *
         * @param req  La solicitud HTTP.
         * @param resp La respuesta HTTP.
         * @return El objeto Persona correspondiente al colaborador o null si no se encuentra.
         * @throws IOException Si ocurre un error al manejar la respuesta.
         */
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
        // Método limpiarYActualizarEspacio
        /**
         * Limpia los datos de un espacio, estableciendo el vehículo y la hora de entrada como nulos,
         * y marcando el espacio como libre.
         *
         * @param resp    La respuesta HTTP.
         * @param espacio El espacio que se va a limpiar y actualizar.
         * @throws IOException Si ocurre un error al manejar la respuesta.
         */
        private void limpiarYActualizarEspacio(HttpServletResponse resp, TbEspacio espacio) throws IOException {
            espacio.setVehiculo(null);
            espacio.setHora_entrada(null);
            espacio.setEstado_espacio(EstadoEspacio.Libre);

        }
        // Método actualizarEspacioYEnviarRespuesta
        /**
         * Actualiza los datos de un espacio en la base de datos y envía una respuesta al cliente.
         *
         * @param resp    La respuesta HTTP.
         * @param espacio El espacio que se va a actualizar.
         * @return true si el espacio se actualizó correctamente, false en caso de error.
         * @throws IOException Si ocurre un error al manejar la respuesta.
         */
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