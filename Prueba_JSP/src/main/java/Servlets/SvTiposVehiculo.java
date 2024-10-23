package Servlets;


import Logica.Logica_TipoVehiculo;
import Modelo.TbTipovehiculo;
import Utilidades.JsonReader;
import Utilidades.ResultadoOperacion;
import Utilidades.sendResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "SvTiposVehiculo", urlPatterns = {"/tiposVehiculos"})
public class SvTiposVehiculo extends HttpServlet {

    // Instancia de la lógica para gestionar tipos de vehículo
    Logica_TipoVehiculo logicaTipoVehiculo = new Logica_TipoVehiculo();

    /**
     * Maneja las solicitudes GET para obtener una lista de tipos de vehículo.
     * Los tipos de vehículo se devuelven en formato JSON.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error al manejar la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TbTipovehiculo> tiposVehiculo = logicaTipoVehiculo.ObtenerTiposVehiculo();
        try {
            // Configurar la respuesta en formato JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Convertir la lista de tipos de vehículo a JSON
            JSONArray jsonArray = new JSONArray();
            for (TbTipovehiculo tipo : tiposVehiculo) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id_Tipo", tipo.getId());
                jsonObject.put("nombre_Tipo", tipo.getNombre());
                jsonArray.put(jsonObject);
            }

            // Enviar la respuesta JSON
            PrintWriter out = response.getWriter();
            out.println(jsonArray.toString());
            out.flush();
        } catch (Exception e) {
            System.out.println("Error al parsear el JSON " + e);
        }
    }

    /**
     * Maneja las solicitudes POST para crear, editar o eliminar tipos de vehículo según la acción especificada.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error al manejar la respuesta.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = JsonReader.parsearJson(request);
        String action = jsonObject.getString("action");

        switch (action) {
            case "add":
                crearTipoVehiculo(request, response, jsonObject);
                break;
            case "edit":
                editTipoVehiculo(request, response, jsonObject);
                break;
            case "delete":
                deleteTipoVehiculo(request, response, jsonObject);
                break;
            default:
                System.out.println("Acción no reconocida");
        }
    }

    /**
     * Crea un nuevo tipo de vehículo en el sistema basado en los datos proporcionados en el JSON.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param jsonObject  El objeto JSON que contiene los datos del nuevo tipo de vehículo.
     * @throws IOException Si ocurre un error al manejar la respuesta.
     */
    private void crearTipoVehiculo(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            String nombre_Tipo = jsonObject.getString("nombreVehiculo");

            if (nombre_Tipo != null && !nombre_Tipo.trim().isEmpty()) {
                TbTipovehiculo tipoVehiculo = new TbTipovehiculo();
                tipoVehiculo.setNombre(nombre_Tipo);

                ResultadoOperacion resultado = logicaTipoVehiculo.crearTipoVehiculo(tipoVehiculo);

                if (resultado.isExito()) {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para crear el tipo de vehículo.");
            }
        } catch (JSONException e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al crear el tipo de vehículo.");
        }
    }

    /**
     * Edita un tipo de vehículo existente en el sistema basado en los datos proporcionados en el JSON.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param jsonObject  El objeto JSON que contiene los datos actualizados del tipo de vehículo.
     * @throws IOException Si ocurre un error al manejar la respuesta.
     */
    private void editTipoVehiculo(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int id_Tipo = jsonObject.getInt("idTipo");
            String nombre_Tipo = jsonObject.getString("nombreVehiculo");

            if (id_Tipo > 0 && nombre_Tipo != null && !nombre_Tipo.trim().isEmpty()) {
                TbTipovehiculo tipoVehiculo = new TbTipovehiculo();
                tipoVehiculo.setId(id_Tipo);
                tipoVehiculo.setNombre(nombre_Tipo);

                ResultadoOperacion resultado = logicaTipoVehiculo.actualizarTipoVehiculo(tipoVehiculo);

                if (resultado.isExito()) {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para actualizar el tipo de vehículo.");
            }
        } catch (JSONException e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al actualizar el tipo de vehículo.");
        }
    }

    /**
     * Elimina un tipo de vehículo existente en el sistema basado en el ID proporcionado en el JSON.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param jsonObject  El objeto JSON que contiene el ID del tipo de vehículo a eliminar.
     * @throws IOException Si ocurre un error al manejar la respuesta.
     */
    private void deleteTipoVehiculo(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int id_Tipo = jsonObject.getInt("idTipo");

            ResultadoOperacion resultado = logicaTipoVehiculo.eliminarTipoVehiculo(id_Tipo);

            if (resultado.isExito()) {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
            }
        } catch (JSONException e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al eliminar el tipo de vehículo.");
        }
    }
}
