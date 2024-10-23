package Servlets;

import Logica.Logica_Ciudad;
import Modelo.Tb_CiudadVehiculo;
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

@WebServlet(name = "SvCiudades", urlPatterns = "/listaCiudades")
public class SvCiudades extends HttpServlet {
    Logica_Ciudad logicaCiudad = new Logica_Ciudad();
    /**
     * Maneja las solicitudes GET para obtener la lista de ciudades y devolverla en formato JSON.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error al manejar la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Tb_CiudadVehiculo> listaCiudades = logicaCiudad.obtenerCiudades();
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JSONArray jsonArray = new JSONArray();
            // Itera sobre la lista de ciudades y convierte cada ciudad a formato JSON
            for (Tb_CiudadVehiculo ciudad : listaCiudades) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id_Ciudad", ciudad.getId());
                jsonObject.put("nombre_Ciudad", ciudad.getNombreCiudad());
                jsonArray.put(jsonObject);
            }
            // Envía la respuesta en formato JSON
            PrintWriter out = response.getWriter();
            out.println(jsonArray.toString());
            out.flush();
        } catch (Exception e) {
            System.out.println("Error al parsear el JSON " + e);
        }
    }

    /**
     * Maneja las solicitudes POST para realizar operaciones de agregar, editar o eliminar una ciudad.
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

        // Realiza la operación correspondiente basada en la acción recibida
        switch (action) {
            case "add":
                crearCiudad(request, response, jsonObject);
                break;
            case "edit":
                editCiudad(request, response, jsonObject);
                break;
            case "delete":
                deleteCiudad(request, response, jsonObject);
                break;
            default:
                System.out.println("Acción no reconocida");
        }
    }

    /**
     * Crea una nueva ciudad basada en los datos proporcionados en la solicitud.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param jsonObject  El objeto JSON que contiene los datos de la nueva ciudad.
     * @throws IOException Si ocurre un error al manejar la respuesta.
     */
    private void crearCiudad(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            String nombre_Ciudad = jsonObject.getString("nombreCiudad");

            // Verifica que el nombre de la ciudad no esté vacío
            if (nombre_Ciudad != null && !nombre_Ciudad.trim().isEmpty()) {
                Tb_CiudadVehiculo ciudad = new Tb_CiudadVehiculo();
                ciudad.setNombreCiudad(nombre_Ciudad);

                // Lógica para crear la ciudad
                ResultadoOperacion resultado = logicaCiudad.crearCiudad(ciudad);

                if (resultado.isExito()) {
                    System.out.println("Ciudad creada exitosamente.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    System.out.println("Error al crear la ciudad.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para crear la ciudad.");
            }

        } catch (JSONException e) {
            System.err.println("Error en los datos JSON: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al crear la ciudad.");
        }
    }

    /**
     * Edita una ciudad existente basada en los datos proporcionados en la solicitud.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param jsonObject  El objeto JSON que contiene los datos actualizados de la ciudad.
     * @throws IOException Si ocurre un error al manejar la respuesta.
     */
    private void editCiudad(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int idCiudad = jsonObject.getInt("idCiudad");
            String nombreCiudad = jsonObject.getString("nombreCiudad");
            // Verifica que el ID y el nombre de la ciudad sean válidos
            if (idCiudad > 0 && nombreCiudad != null && !nombreCiudad.trim().isEmpty()) {
                Tb_CiudadVehiculo ciudad = new Tb_CiudadVehiculo();
                ciudad.setId(idCiudad);
                ciudad.setNombreCiudad(nombreCiudad);

                // Lógica para actualizar la ciudad
                ResultadoOperacion resultado = logicaCiudad.actualizarCiudad(ciudad);

                if (resultado.isExito()) {
                    System.out.println("Ciudad actualizada exitosamente.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    System.out.println("Error al actualizar la ciudad: " + resultado.getMensaje());
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para actualizar la ciudad.");
            }

        } catch (JSONException e) {
            System.err.println("Error en los datos JSON: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al actualizar la ciudad.");
        }
    }

    /**
     * Elimina una ciudad basada en el ID proporcionado en la solicitud.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param jsonObject  El objeto JSON que contiene el ID de la ciudad a eliminar.
     * @throws IOException Si ocurre un error al manejar la respuesta.
     */
    private void deleteCiudad(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int idCiudad = jsonObject.getInt("idCiudad");

            ResultadoOperacion resultado = logicaCiudad.eliminarCiudad(idCiudad);

            if (resultado.isExito()) {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
            }

        } catch (JSONException e) {
            System.err.println("Error en los datos JSON: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al eliminar la ciudad.");
        }
    }
}
