package Servlets;

import Logica.Logica_Rol;
import Modelo.Roles;
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
@WebServlet(name = "SvRoles", urlPatterns = "/listaRoles")
public class SvRol extends HttpServlet {
    // Instancia de la lógica para gestionar roles
    Logica_Rol logicaRol = new Logica_Rol();

    /**
     * Maneja las solicitudes GET para obtener una lista de roles del sistema.
     * Los roles se devuelven en formato JSON.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error al manejar la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Roles> lista = logicaRol.ObtenerRoles();
        try {
            // Configurar la respuesta en formato JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Convertir la lista de roles a JSON
            JSONArray jsonArray = new JSONArray();
            for (Roles rol : lista) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id_rol", rol.getId());
                jsonObject.put("nombre_rol", rol.getNombre());
                jsonArray.put(jsonObject);
            }

            // Enviar la respuesta JSON
            PrintWriter out = response.getWriter();
            out.println(jsonArray.toString());
            out.flush();
        } catch (Exception e) {
            System.out.println("Error al parsear el JSON: " + e);
        }
    }

    /**
     * Maneja las solicitudes POST para crear, editar o eliminar roles según la acción especificada.
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

        // Realizar la acción correspondiente
        switch (action) {
            case "add":
                crearRol(request, response, jsonObject);
                break;
            case "edit":
                editRol(request, response, jsonObject);
                break;
            case "delete":
                deleteRol(request, response, jsonObject);
                break;
            default:
                System.out.println("Acción no reconocida");
        }
    }

    /**
     * Crea un nuevo rol en el sistema basado en los datos proporcionados en el JSON.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param jsonObject  El objeto JSON que contiene los datos del nuevo rol.
     * @throws IOException Si ocurre un error al manejar la respuesta.
     */
    private void crearRol(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            String nombre_Rol = jsonObject.getString("nombreRol");

            if (nombre_Rol != null && !nombre_Rol.trim().isEmpty()) {
                Roles roles = new Roles();
                roles.setNombre(nombre_Rol);

                ResultadoOperacion resultado = logicaRol.crearRol(roles);

                if (resultado.isExito()) {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para crear el rol.");
            }
        } catch (JSONException e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al crear el rol.");
        }
    }

    /**
     * Edita un rol existente en el sistema basado en los datos proporcionados en el JSON.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param jsonObject  El objeto JSON que contiene los datos actualizados del rol.
     * @throws IOException Si ocurre un error al manejar la respuesta.
     */
    private void editRol(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int idRol = jsonObject.getInt("idRol");
            String nombre = jsonObject.getString("nombreRol");

            if (idRol > 0 && nombre != null && !nombre.trim().isEmpty()) {
                Roles roles = new Roles();
                roles.setId(idRol);
                roles.setNombre(nombre);

                ResultadoOperacion resultado = logicaRol.actualizarRol(roles);

                if (resultado.isExito()) {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para actualizar el rol.");
            }
        } catch (JSONException e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al actualizar el rol.");
        }
    }

    /**
     * Elimina un rol existente en el sistema basado en el ID proporcionado en el JSON.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param jsonObject  El objeto JSON que contiene el ID del rol a eliminar.
     * @throws IOException Si ocurre un error al manejar la respuesta.
     */
    private void deleteRol(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int idRol = jsonObject.getInt("idRol");

            ResultadoOperacion resultado = logicaRol.eliminarRol(idRol);

            if (resultado.isExito()) {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
            }
        } catch (JSONException e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al eliminar el rol.");
        }
    }
}

