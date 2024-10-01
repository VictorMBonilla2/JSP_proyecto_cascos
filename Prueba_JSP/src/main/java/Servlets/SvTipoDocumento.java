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
        Logica_Documentos logicaDocumentos = new Logica_Documentos();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TbTipoDocumento> lista= logicaDocumentos.obtenerDocumentos();
        try{
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            JSONArray jsonArray = new  JSONArray();
            for (TbTipoDocumento tipo : lista){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id_documento", tipo.getId());
                jsonObject.put("nombre_documento",tipo.getNombreDocumento());
                jsonArray.put(jsonObject);
            }
            PrintWriter out = response.getWriter();
            out.println(jsonArray.toString());
            out.flush();
        }catch (Exception e){
            System.out.println("Error al parsear el JSON "+e);
        }


    }

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


    private void crearDocumento(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            // Obtener los parámetros del JSON
            String nombre_Sector = jsonObject.getString("nombreDocumento");

            if (nombre_Sector != null && !nombre_Sector.trim().isEmpty()) {
                // Crear el objeto TbTipoDocumento
                TbTipoDocumento documento = new TbTipoDocumento();
                documento.setNombreDocumento(nombre_Sector);

                // Intentar crear el documento utilizando logica_sectores
                ResultadoOperacion resultado = logicaDocumentos.crearDocumento(documento);

                // Verificar si la creación del documento fue exitosa
                if (resultado.isExito()) {
                    System.out.println("Documento creado exitosamente.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    System.out.println("Error al crear el Documento.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                // Datos no válidos, enviar una respuesta de error
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para crear el Documento.");
            }

        } catch (JSONException e) {
            // Manejar el error si los datos JSON son incorrectos
            System.err.println("Error en los datos JSON: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            // Manejar cualquier otro tipo de error
            System.err.println("Error inesperado: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al crear el Documento.");
        }
    }

    private void editDocumento(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            // Obtener los parámetros del JSON
            int idDocumento = jsonObject.getInt("idDocumento");
            String nombre = jsonObject.getString("nombreDocumento");

            // Validar los parámetros
            if (idDocumento > 0 && nombre != null && !nombre.trim().isEmpty()) {
                // Crear el objeto TbSectores con los datos nuevos
                TbTipoDocumento documento = new TbTipoDocumento();
                documento.setId(idDocumento);  // Establecer el ID del sector
                documento.setNombreDocumento(nombre);

                // Intentar actualizar el documento usando el nuevo enfoque de ResultadoOperacion
                ResultadoOperacion resultado = logicaDocumentos.actualizarDocumento(documento);

                // Verificar si la operación fue exitosa
                if (resultado.isExito()) {
                    System.out.println("Documento actualizado exitosamente.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    System.out.println("Error al actualizar el documento: " + resultado.getMensaje());
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                // Si los datos no son válidos
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para actualizar el documento.");
            }

        } catch (JSONException e) {
            // Manejar el error en los datos JSON
            System.err.println("Error en los datos JSON: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            // Manejar cualquier otro tipo de error
            System.err.println("Error inesperado: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al actualizar el documento.");
        }
    }


    private void deleteDocumento(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int idDocumento = jsonObject.getInt("idDocumento");

            // Llamar al método de lógica para eliminar el sector
            ResultadoOperacion resultado = logicaDocumentos.eliminarTipoDocumento(idDocumento);

            // Manejar la respuesta según el resultado de la operación
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
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al eliminar el tipo de documento.");
        }
    }

}
