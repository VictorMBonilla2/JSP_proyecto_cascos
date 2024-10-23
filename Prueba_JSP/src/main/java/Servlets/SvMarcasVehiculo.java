package Servlets;


import Logica.Logica_MarcaVehiculo;
import Logica.Logica_TipoVehiculo;
import Modelo.TbTipovehiculo;
import Modelo.Tb_MarcaVehiculo;
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

@WebServlet(name = "SvMarcasVehiculo", urlPatterns = "/listaMarcas")
public class SvMarcasVehiculo extends HttpServlet {
    // Instancia de la lógica relacionada con marcas de vehículos
    Logica_MarcaVehiculo logica_marcaVehiculo = new Logica_MarcaVehiculo();
    // Instancia de la lógica relacionada con tipos de vehículos
    Logica_TipoVehiculo logicaTipoVehiculo = new Logica_TipoVehiculo();

    /**
     * Maneja las solicitudes GET para obtener la lista de marcas de vehículos, filtradas
     * por el tipo de vehículo, y devuelve la información en formato JSON.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error al manejar la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener el parámetro 'id_Tipo' para filtrar las marcas por tipo de vehículo
        int idTipoVehiculo = Integer.parseInt(request.getParameter("id_Tipo"));

        // Obtener las marcas de vehículos filtradas por el tipo de vehículo
        List<Tb_MarcaVehiculo> marcasVehiculo = logica_marcaVehiculo.ObtenerMarcasPorTipo(idTipoVehiculo);
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Crear un array JSON para las marcas
            JSONArray jsonArray = new JSONArray();
            for (Tb_MarcaVehiculo marca : marcasVehiculo) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id_Marca", marca.getId());
                jsonObject.put("nombre_Marca", marca.getNombreMarca());
                jsonArray.put(jsonObject);
            }

            // Escribir la respuesta JSON
            PrintWriter out = response.getWriter();
            out.println(jsonArray.toString());
            out.flush();
        } catch (Exception e) {
            System.out.println("Error al parsear el JSON: " + e);
        }
    }

    /**
     * Maneja las solicitudes POST para realizar acciones sobre las marcas de vehículos,
     * como crear, editar o eliminar marcas, dependiendo del parámetro 'action' en la solicitud.
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
                crearMarcaVehiculo(request, response, jsonObject);
                break;
            case "edit":
                editMarcaVehiculo(request, response, jsonObject);
                break;
            case "delete":
                deleteMarcaVehiculo(request, response, jsonObject);
                break;
            default:
                System.out.println("Acción no reconocida");
        }
    }
    /**
     * Crea una nueva marca de vehículo con los datos proporcionados en el JSON de la solicitud.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param jsonObject  El objeto JSON con los datos de la nueva marca.
     * @throws IOException Si ocurre un error al manejar la respuesta.
     */
    private void crearMarcaVehiculo(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            String nombreMarca = jsonObject.getString("nombreMarca");
            int idTipoVehiculo = jsonObject.getInt("idTipoVehiculo");

            // Validar los datos antes de crear la marca
            if (nombreMarca != null && !nombreMarca.trim().isEmpty()) {
                Tb_MarcaVehiculo marca = new Tb_MarcaVehiculo();
                marca.setNombreMarca(nombreMarca);

                // Asignar la relación con el tipo de vehículo
                TbTipovehiculo tipoVehiculo = logicaTipoVehiculo.buscarTipoVehiculo(idTipoVehiculo);
                marca.setTipoVehiculo(tipoVehiculo);

                ResultadoOperacion resultado = logica_marcaVehiculo.crearMarcaVehiculo(marca);

                if (resultado.isExito()) {
                    System.out.println("Marca de vehículo creada exitosamente.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    System.out.println("Error al crear la marca de vehículo.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para crear la marca.");
            }

        } catch (JSONException e) {
            System.err.println("Error en los datos JSON: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al crear la marca.");
        }
    }

    /**
     * Edita una marca de vehículo existente con los datos proporcionados en el JSON de la solicitud.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param jsonObject  El objeto JSON con los datos actualizados de la marca.
     * @throws IOException Si ocurre un error al manejar la respuesta.
     */
    private void editMarcaVehiculo(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int idMarca = jsonObject.getInt("idMarca");
            String nombreMarca = jsonObject.getString("nombreMarca");
            int idTipoVehiculo = jsonObject.getInt("idTipoVehiculo");

            // Validar los datos antes de actualizar la marca
            if (idMarca > 0 && nombreMarca != null && !nombreMarca.trim().isEmpty()) {
                Tb_MarcaVehiculo marca = new Tb_MarcaVehiculo();
                marca.setId(idMarca);
                marca.setNombreMarca(nombreMarca);

                // Asignar la relación con el tipo de vehículo
                TbTipovehiculo tipoVehiculo = logicaTipoVehiculo.buscarTipoVehiculo(idTipoVehiculo);
                marca.setTipoVehiculo(tipoVehiculo);

                ResultadoOperacion resultado = logica_marcaVehiculo.actualizarMarcaVehiculo(marca);

                if (resultado.isExito()) {
                    System.out.println("Marca de vehículo actualizada exitosamente.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    System.out.println("Error al actualizar la marca: " + resultado.getMensaje());
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para actualizar la marca.");
            }

        } catch (JSONException e) {
            System.err.println("Error en los datos JSON: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al actualizar la marca.");
        }
    }
    /**
     * Elimina una marca de vehículo con el ID proporcionado en el JSON de la solicitud.
     *
     * @param request     La solicitud HTTP.
     * @param response    La respuesta HTTP.
     * @param jsonObject  El objeto JSON con el ID de la marca a eliminar.
     * @throws IOException Si ocurre un error al manejar la respuesta.
     */
    private void deleteMarcaVehiculo(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int idMarca = jsonObject.getInt("idMarca");

            // Eliminar la marca de vehículo
            ResultadoOperacion resultado = logica_marcaVehiculo.eliminarMarcaVehiculo(idMarca);

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
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al eliminar la marca.");
        }
    }
}
