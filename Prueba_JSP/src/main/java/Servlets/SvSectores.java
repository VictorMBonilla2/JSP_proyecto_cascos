package Servlets;

import Logica.Logica_Sectores;
import Modelo.Persona;
import Modelo.TbSectores;
import Utilidades.EspacioServiceManager;
import Utilidades.JsonReader;
import Utilidades.ResultadoOperacion;
import Utilidades.sendResponse;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.hibernate.query.sqm.produce.function.ArgumentTypesValidator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SvSectores", urlPatterns = {"/SvSector"})
public class SvSectores extends HttpServlet {
    // Obtener la instancia de Logica_Sectores desde EspacioServiceManager
    Logica_Sectores logica_sectores = EspacioServiceManager.getInstance().getLogicaSectores();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TbSectores> sectores = logica_sectores.ObtenerSectores();
        request.setAttribute("sectores", sectores);


        JSONArray jsonArray = new JSONArray();

        for (TbSectores sector : sectores){
            JSONObject jsonObject= new JSONObject();
            jsonObject.put("id_sector", sector.getId());
            jsonObject.put("cant_espacio", sector.getCant_espacio());
            jsonObject.put("nombre_sector", sector.getNombreSector());
            jsonArray.put(jsonObject);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonArray.toString());
    }
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
    private void crearSector(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            // Obtener los parámetros del JSON
            int cantidad_Espacios = jsonObject.getInt("cantidadEspacio");
            String nombre_Sector = jsonObject.getString("nombreSector");

            // Verificar que cantidad_Espacios sea mayor que 0 y que nombre_Sector no sea null ni esté vacío
            if (cantidad_Espacios > 0 && nombre_Sector != null && !nombre_Sector.trim().isEmpty()) {
                // Crear el objeto TbSectores
                TbSectores sector = new TbSectores();
                sector.setCant_espacio(cantidad_Espacios);
                sector.setNombreSector(nombre_Sector);

                // Intentar crear el sector utilizando logica_sectores
                ResultadoOperacion resultado = logica_sectores.crearSector(sector);

                // Verificar si la creación del sector fue exitosa
                if (resultado.isExito()) {
                    System.out.println("Sector creado exitosamente.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    System.out.println("Error al crear el sector.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                // Datos no válidos, enviar una respuesta de error
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para crear el sector.");
            }

        } catch (JSONException e) {
            // Manejar el error si los datos JSON son incorrectos
            System.err.println("Error en los datos JSON: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            // Manejar cualquier otro tipo de error
            System.err.println("Error inesperado: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al crear el sector.");
        }
    }
    private void editSector(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            // Obtener los parámetros del JSON
            int idSector = jsonObject.getInt("idSector");
            int cantEspacio = jsonObject.getInt("cantidadEspacio");
            String nombre = jsonObject.getString("nombreSector");

            // Validar los parámetros
            if (idSector > 0 && cantEspacio > 0 && nombre != null && !nombre.trim().isEmpty()) {
                // Crear el objeto TbSectores con los datos nuevos
                TbSectores sector = new TbSectores();
                sector.setId(idSector);  // Establecer el ID del sector
                sector.setCant_espacio(cantEspacio);
                sector.setNombreSector(nombre);

                // Intentar actualizar el sector usando el nuevo enfoque de ResultadoOperacion
                ResultadoOperacion resultado = logica_sectores.actualizarSector(sector);

                // Verificar si la operación fue exitosa
                if (resultado.isExito()) {
                    System.out.println("Sector actualizado exitosamente.");
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
                } else {
                    System.out.println("Error al actualizar el sector: " + resultado.getMensaje());
                    sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
                }
            } else {
                // Si los datos no son válidos
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos para actualizar el sector.");
            }

        } catch (JSONException e) {
            // Manejar el error en los datos JSON
            System.err.println("Error en los datos JSON: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            // Manejar cualquier otro tipo de error
            System.err.println("Error inesperado: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al actualizar el sector.");
        }
    }
    private void deleteSector(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            int idSector = jsonObject.getInt("idSector");

            // Llamar al método de lógica para eliminar el sector
            ResultadoOperacion resultado = logica_sectores.eliminarSector(idSector);

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
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al eliminar el sector.");
        }
    }
}