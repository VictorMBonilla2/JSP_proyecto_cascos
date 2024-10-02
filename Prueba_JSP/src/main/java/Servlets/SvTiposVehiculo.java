package Servlets;


import Logica.Logica_TipoVehiculo;
import Logica.Logica_Vehiculo;
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

    Logica_TipoVehiculo logicaTipoVehiculo = new Logica_TipoVehiculo();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TbTipovehiculo> tiposVehiculo = logicaTipoVehiculo.ObtenerTiposVehiculo();
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            JSONArray jsonArray = new JSONArray();
            for (TbTipovehiculo tipo : tiposVehiculo) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id_Tipo", tipo.getId());
                jsonObject.put("nombre_Tipo", tipo.getNombre());
                jsonArray.put(jsonObject);
            }
            PrintWriter out = response.getWriter();
            out.println(jsonArray.toString());
            out.flush();
        } catch (Exception e) {
            System.out.println("Error al parsear el JSON " + e);
        }
    }

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

    // Método para crear un nuevo tipo de vehículo
    private void crearTipoVehiculo(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            String nombre_Tipo = jsonObject.getString("nombreVehiculo");

            if (nombre_Tipo != null && !nombre_Tipo.trim().isEmpty()) {
                TbTipovehiculo tipoVehiculo = new TbTipovehiculo();
                tipoVehiculo.setNombre(nombre_Tipo);

                ResultadoOperacion resultado = logicaTipoVehiculo.crearTipoVehiculo(tipoVehiculo);

                if (resultado.isExito()) {
                    System.out.println("Tipo de vehículo creado exitosamente.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    System.out.println("Error al crear el tipo de vehículo.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para crear el tipo de vehículo.");
            }
        } catch (JSONException e) {
            System.err.println("Error en los datos JSON: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al crear el tipo de vehículo.");
        }
    }

    // Método para editar un tipo de vehículo existente
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
                    System.out.println("Tipo de vehículo actualizado exitosamente.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    System.out.println("Error al actualizar el tipo de vehículo.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para actualizar el tipo de vehículo.");
            }
        } catch (JSONException e) {
            System.err.println("Error en los datos JSON: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al actualizar el tipo de vehículo.");
        }
    }

    // Método para eliminar un tipo de vehículo
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
            System.err.println("Error en los datos JSON: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al eliminar el tipo de vehículo.");
        }
    }
}
