package Servlets;

import Logica.Logica_MarcaVehiculo;
import Logica.Logica_Modelo;
import Logica.Logica_TipoVehiculo;
import Modelo.TbTipovehiculo;
import Modelo.Tb_MarcaVehiculo;
import Modelo.Tb_ModeloVehiculo;
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

@WebServlet(name = "SvModeloVehiculo", urlPatterns = {"/listaModelo"})
public class SvModeloVehiculo extends HttpServlet {

    Logica_Modelo logicaModelo = new Logica_Modelo();
    Logica_MarcaVehiculo logicaMarcaVehiculo = new Logica_MarcaVehiculo();
    Logica_TipoVehiculo logicaTipoVehiculo = new Logica_TipoVehiculo();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Validar los parámetros de entrada
            String idMarcaParam = request.getParameter("id_Marca");
            String idTipoParam = request.getParameter("id_Tipo");

            if (idMarcaParam == null || idTipoParam == null) {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Faltan parámetros requeridos");
                return;
            }

            int idMarcaVehiculo = Integer.parseInt(idMarcaParam);
            int idTipoVehiculo = Integer.parseInt(idTipoParam);

            // Obtener los modelos por marca y tipo
            List<Tb_ModeloVehiculo> modelosVehiculo = logicaModelo.ObtenerModelosPorMarcaYTipo(idMarcaVehiculo, idTipoVehiculo);

            // Crear el array JSON
            JSONArray jsonArray = new JSONArray();
            for (Tb_ModeloVehiculo modelo : modelosVehiculo) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id_Modelo", modelo.getId());
                jsonObject.put("nombre_Modelo", modelo.getNombreModelo());
                jsonArray.put(jsonObject);
            }

            // Enviar respuesta JSON
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", jsonArray.toString());

        } catch (NumberFormatException e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Parámetros inválidos");
        } catch (Exception e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error al procesar la solicitud: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = JsonReader.parsearJson(request);
        String action = jsonObject.getString("action");

        switch (action) {
            case "add":
                crearModelo(request, response, jsonObject);
                break;
            case "edit":
                editModelo(request, response, jsonObject);
                break;
            case "delete":
                deleteModelo(request, response, jsonObject);
                break;
            default:
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Acción no reconocida");
        }
    }

    private void crearModelo(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            String nombreModelo = jsonObject.getString("nombreModelo");
            int idMarcaVehiculo = jsonObject.getInt("id_Marca");
            int idTipoVehiculo = jsonObject.getInt("id_Tipo");

            // Validar los datos
            if (nombreModelo != null && !nombreModelo.trim().isEmpty()) {
                Tb_MarcaVehiculo marcaVehiculo = logicaMarcaVehiculo.buscarMarcaPorId(idMarcaVehiculo);
                TbTipovehiculo tipoVehiculo = logicaTipoVehiculo.buscarTipoVehiculo(idTipoVehiculo);

                if (marcaVehiculo == null || tipoVehiculo == null) {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Marca o Tipo de vehículo inválidos.");
                    return;
                }

                Tb_ModeloVehiculo modelo = new Tb_ModeloVehiculo();
                modelo.setNombreModelo(nombreModelo);
                modelo.setMarcaVehiculo(marcaVehiculo);

                // Crear el modelo
                ResultadoOperacion resultado = logicaModelo.crearModelo(modelo);

                if (resultado.isExito()) {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para crear el modelo.");
            }
        } catch (JSONException e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Error en los datos JSON");
        }
    }

    private void editModelo(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int idModelo = jsonObject.getInt("id_Modelo");
            String nombreModelo = jsonObject.getString("nombreModelo");

            if (idModelo > 0 && nombreModelo != null && !nombreModelo.trim().isEmpty()) {
                Tb_ModeloVehiculo modelo = logicaModelo.buscarModeloPorId(idModelo);
                modelo.setNombreModelo(nombreModelo);

                // Actualizar modelo
                ResultadoOperacion resultado = logicaModelo.actualizarModelo(modelo);

                if (resultado.isExito()) {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para actualizar el modelo.");
            }

        } catch (JSONException e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Error en los datos JSON");
        }
    }

    private void deleteModelo(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int idModelo = jsonObject.getInt("id_Modelo");

            // Eliminar modelo
            ResultadoOperacion resultado = logicaModelo.eliminarModelo(idModelo);

            if (resultado.isExito()) {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
            }

        } catch (JSONException e) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Error en los datos JSON");
        }
    }
}

