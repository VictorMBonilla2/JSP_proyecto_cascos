package Servlets;

import Logica.Logica_Sectores;
import Modelo.TbSectores;
import Utilidades.EspacioServiceManager;
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
import java.util.List;

@WebServlet(name = "SvSectores", urlPatterns = {"/SvSector"})
public class SvSectores extends HttpServlet {
    // Instancia de la lógica para gestionar sectores
    Logica_Sectores logica_sectores = EspacioServiceManager.getInstance().getLogicaSectores();

    /**
     * Maneja las solicitudes GET para obtener una lista de sectores.
     * Los sectores se devuelven en formato JSON.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error al manejar la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TbSectores> sectores = logica_sectores.ObtenerSectores();
        request.setAttribute("sectores", sectores);

        // Crear el JSONArray para almacenar los sectores
        JSONArray jsonArray = new JSONArray();
        for (TbSectores sector : sectores) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id_sector", sector.getId());
            jsonObject.put("cant_espacio", sector.getCant_espacio());
            jsonObject.put("nombre_sector", sector.getNombreSector());
            jsonArray.put(jsonObject);
        }

        // Configurar la respuesta en formato JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonArray.toString());
    }

    /**
     * Maneja las solicitudes POST para crear, editar o eliminar sectores según la acción especificada.
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
                crearSector(request, response, jsonObject);
                break;
            case "edit":
                editSector(request, response, jsonObject);
                break;
            case "delete":
                deleteSector(request, response, jsonObject);
                break;
            default:
                System.out.println("Acción no reconocida");
        }
    }

    /**
     * Crea un nuevo sector en el sistema basado en los datos proporcionados en el JSON.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param jsonObject  El objeto JSON que contiene los datos del nuevo sector.
     * @throws IOException Si ocurre un error al manejar la respuesta.
     */
    private void crearSector(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int cantidad_Espacios = jsonObject.getInt("cantidadEspacio");
            String nombre_Sector = jsonObject.getString("nombreSector");

            if (cantidad_Espacios > 0 && nombre_Sector != null && !nombre_Sector.trim().isEmpty()) {
                TbSectores sector = new TbSectores();
                sector.setCant_espacio(cantidad_Espacios);
                sector.setNombreSector(nombre_Sector);

                ResultadoOperacion resultado = logica_sectores.crearSector(sector);

                if (resultado.isExito()) {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para crear el sector.");
            }
        } catch (JSONException e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al crear el sector.");
        }
    }

    /**
     * Edita un sector existente en el sistema basado en los datos proporcionados en el JSON.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param jsonObject  El objeto JSON que contiene los datos actualizados del sector.
     * @throws IOException Si ocurre un error al manejar la respuesta.
     */
    private void editSector(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int idSector = jsonObject.getInt("idSector");
            int cantEspacio = jsonObject.getInt("cantidadEspacio");
            String nombre = jsonObject.getString("nombreSector");

            if (idSector > 0 && cantEspacio > 0 && nombre != null && !nombre.trim().isEmpty()) {
                TbSectores sector = new TbSectores();
                sector.setId(idSector);
                sector.setCant_espacio(cantEspacio);
                sector.setNombreSector(nombre);

                ResultadoOperacion resultado = logica_sectores.actualizarSector(sector);

                if (resultado.isExito()) {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para actualizar el sector.");
            }
        } catch (JSONException e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al actualizar el sector.");
        }
    }

    /**
     * Elimina un sector existente en el sistema basado en el ID proporcionado en el JSON.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param jsonObject  El objeto JSON que contiene el ID del sector a eliminar.
     * @throws IOException Si ocurre un error al manejar la respuesta.
     */
    private void deleteSector(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int idSector = jsonObject.getInt("idSector");

            ResultadoOperacion resultado = logica_sectores.eliminarSector(idSector);

            if (resultado.isExito()) {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
            }
        } catch (JSONException e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al eliminar el sector.");
        }
    }
}
