package Servlets;

import Logica.Logica_Documentos;
import Modelo.TbTipoDocumento;
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

@WebServlet(name = "tipodocumento", urlPatterns = "/tipoDoc")
public class SvTipoDocumento extends HttpServlet {
    // Instancia de la lógica para gestionar documentos
    Logica_Documentos logicaDocumentos = new Logica_Documentos();

    /**
     * Maneja las solicitudes GET para obtener una lista de tipos de documento.
     * Los tipos de documento se devuelven en formato JSON.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error al manejar la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TbTipoDocumento> lista = logicaDocumentos.obtenerDocumentos();
        try {
            // Configurar la respuesta en formato JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Convertir la lista de tipos de documento a JSON
            JSONArray jsonArray = new JSONArray();
            for (TbTipoDocumento tipo : lista) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id_documento", tipo.getId());
                jsonObject.put("nombre_documento", tipo.getNombreDocumento());
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
     * Maneja las solicitudes POST para crear, editar o eliminar tipos de documento según la acción especificada.
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
                crearDocumento(request, response, jsonObject);
                break;
            case "edit":
                editDocumento(request, response, jsonObject);
                break;
            case "delete":
                deleteDocumento(request, response, jsonObject);
                break;
            default:
                System.out.println("Acción no reconocida");
        }
    }

    /**
     * Crea un nuevo tipo de documento en el sistema basado en los datos proporcionados en el JSON.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param jsonObject  El objeto JSON que contiene los datos del nuevo tipo de documento.
     * @throws IOException Si ocurre un error al manejar la respuesta.
     */
    private void crearDocumento(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            String nombreDocumento = jsonObject.getString("nombreDocumento");

            if (nombreDocumento != null && !nombreDocumento.trim().isEmpty()) {
                TbTipoDocumento documento = new TbTipoDocumento();
                documento.setNombreDocumento(nombreDocumento);

                ResultadoOperacion resultado = logicaDocumentos.crearDocumento(documento);

                if (resultado.isExito()) {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para crear el Documento.");
            }
        } catch (JSONException e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al crear el Documento.");
        }
    }

    /**
     * Edita un tipo de documento existente en el sistema basado en los datos proporcionados en el JSON.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param jsonObject  El objeto JSON que contiene los datos actualizados del tipo de documento.
     * @throws IOException Si ocurre un error al manejar la respuesta.
     */
    private void editDocumento(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int idDocumento = jsonObject.getInt("idDocumento");
            String nombre = jsonObject.getString("nombreDocumento");

            if (idDocumento > 0 && nombre != null && !nombre.trim().isEmpty()) {
                TbTipoDocumento documento = new TbTipoDocumento();
                documento.setId(idDocumento);
                documento.setNombreDocumento(nombre);

                ResultadoOperacion resultado = logicaDocumentos.actualizarDocumento(documento);

                if (resultado.isExito()) {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para actualizar el documento.");
            }
        } catch (JSONException e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al actualizar el documento.");
        }
    }

    /**
     * Elimina un tipo de documento existente en el sistema basado en el ID proporcionado en el JSON.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param jsonObject  El objeto JSON que contiene el ID del tipo de documento a eliminar.
     * @throws IOException Si ocurre un error al manejar la respuesta.
     */
    private void deleteDocumento(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int idDocumento = jsonObject.getInt("idDocumento");

            ResultadoOperacion resultado = logicaDocumentos.eliminarTipoDocumento(idDocumento);

            if (resultado.isExito()) {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
            }
        } catch (JSONException e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al eliminar el tipo de documento.");
        }
    }
}
