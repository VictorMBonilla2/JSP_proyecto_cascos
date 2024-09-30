package Servlets;

import Logica.Logica_Rol;
import Modelo.Roles;
import Utilidades.JsonReader;
import Utilidades.ResultadoOperacion;
import Utilidades.sendResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class SvRol extends HttpServlet {
    Logica_Rol logicaRol = new Logica_Rol();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Roles> lista = logicaRol.ObtenerRoles();
        try{
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JSONArray jsonArray = new  JSONArray();
            for (Roles rol : lista){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id_rol", rol.getId());
                jsonObject.put("nombre_rol",rol.getNombre());
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

    private void crearRol(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            // Obtener los parámetros del JSON
            String nombre_Rol = jsonObject.getString("nombreRol");

            if (nombre_Rol != null && !nombre_Rol.trim().isEmpty()) {
                // Crear el objeto TbTipoDocumento
                Roles roles = new Roles();
                roles.setNombre(nombre_Rol);

                // Intentar crear el documento utilizando logica_sectores
                ResultadoOperacion resultado = logicaRol.crearRol(roles);

                // Verificar si la creación del documento fue exitosa
                if (resultado.isExito()) {
                    System.out.println("Rol creado exitosamente.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    System.out.println("Error al crear el rol.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                // Datos no válidos, enviar una respuesta de error
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para crear el rol.");
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

    private void editRol(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            // Obtener los parámetros del JSON
            int idRol = jsonObject.getInt("idRol");
            String nombre = jsonObject.getString("nombreRol");

            // Validar los parámetros
            if (idRol > 0 && nombre != null && !nombre.trim().isEmpty()) {
                // Crear el objeto TbSectores con los datos nuevos
                Roles roles = new Roles();
                roles.setId(idRol);  // Establecer el ID del sector
                roles.setNombre(nombre);

                // Intentar actualizar el documento usando el nuevo enfoque de ResultadoOperacion
                ResultadoOperacion resultado = logicaRol.actualizarRol(roles);

                // Verificar si la operación fue exitosa
                if (resultado.isExito()) {
                    System.out.println("Rol actualizado exitosamente.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    System.out.println("Error al actualizar el rol: " + resultado.getMensaje());
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                // Si los datos no son válidos
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para actualizar el rol.");
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

    private void deleteRol(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int idRol = jsonObject.getInt("idRol");

            // Llamar al método de lógica para eliminar el sector
            ResultadoOperacion resultado = logicaRol.eliminarRol(idRol);

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
