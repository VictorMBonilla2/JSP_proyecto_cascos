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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Tb_CiudadVehiculo> listaCiudades = logicaCiudad.obtenerCiudades();
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JSONArray jsonArray = new JSONArray();
            for (Tb_CiudadVehiculo ciudad : listaCiudades) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id_Ciudad", ciudad.getId());
                jsonObject.put("nombre_Ciudad", ciudad.getNombreCiudad());
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

    private void crearCiudad(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            String nombre_Ciudad = jsonObject.getString("nombreCiudad");

            if (nombre_Ciudad != null && !nombre_Ciudad.trim().isEmpty()) {
                Tb_CiudadVehiculo ciudad = new Tb_CiudadVehiculo();
                ciudad.setNombreCiudad(nombre_Ciudad);

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

    private void editCiudad(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int idCiudad = jsonObject.getInt("idCiudad");
            String nombreCiudad = jsonObject.getString("nombreCiudad");

            if (idCiudad > 0 && nombreCiudad != null && !nombreCiudad.trim().isEmpty()) {
                Tb_CiudadVehiculo ciudad = new Tb_CiudadVehiculo();
                ciudad.setId(idCiudad);
                ciudad.setNombreCiudad(nombreCiudad);

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
