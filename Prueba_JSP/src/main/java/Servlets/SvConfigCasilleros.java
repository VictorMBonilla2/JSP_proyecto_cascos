package Servlets;

import Logica.Logica_Espacios;
import Modelo.TbEspacio;
import Modelo.enums.EstadoEspacio;
import Utilidades.EspacioServiceManager;
import Utilidades.JsonReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import static Utilidades.sendResponse.enviarRespuesta;

@WebServlet(name = "SvConfigCasilleros", urlPatterns = {"/configCasilleros"})
public class SvConfigCasilleros extends HttpServlet {
    Logica_Espacios logica_espacios = EspacioServiceManager.getInstance().getLogicaEspacios();

    /**
     * Maneja las solicitudes GET para obtener los espacios disponibles de un sector específico,
     * basado en el parámetro 'idSector' proporcionado en la solicitud. La información de los
     * espacios se devuelve en formato JSON.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error al manejar la respuesta.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Obtener el parámetro 'idSector' de la solicitud
        String idSectorParam = request.getParameter("idSector");

        // Validar si el parámetro es válido y no es nulo
        if (idSectorParam != null && !idSectorParam.isEmpty()) {
            try {
                // Convertir el parámetro a un entero
                int idSector = Integer.parseInt(idSectorParam);

                // Llamar a la lógica de espacios y obtener los espacios por sector
                List<TbEspacio> listaEspacios = logica_espacios.obtnerEspaciosPorSector(idSector);

                // Configurar la respuesta en formato JSON
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                // Convertir la lista a JSON
                JSONArray jsonArray = new JSONArray();
                for (TbEspacio espacio : listaEspacios) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("idEspacio", espacio.getId_espacio());
                    jsonObject.put("estadoEspacio", espacio.getEstado_espacio().toString());
                    jsonObject.put("nombreEspacio", espacio.getNombre());
                    jsonObject.put("idSector", espacio.getSector().getId());
                    jsonArray.put(jsonObject);
                }

                // Enviar el JSON como respuesta
                PrintWriter out = response.getWriter();
                out.print(jsonArray.toString());
                out.flush();

            } catch (NumberFormatException e) {
                // Si el parámetro no es un número válido, manejar el error
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "El idSector debe ser un número válido.");
            }
        } else {
            // Si no se proporciona idSector, devolver un error
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Debe proporcionar el parámetro idSector.");
        }
    }

    /**
     * Maneja las solicitudes POST para cambiar el estado de un espacio (Reactivar o Desactivar).
     * El estado de un espacio es enviado como un parámetro JSON. Dependiendo del estado, se
     * reactiva o desactiva el espacio.
     *
     * @param req  La solicitud HTTP.
     * @param resp La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error al manejar la respuesta.
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Parsear el JSON de la solicitud
        JSONObject jsonObject = JsonReader.parsearJson(req);

        // Obtener los parámetros del JSON
        int idEspacio = jsonObject.getInt("idEspacio");
        String estadoEspacio = jsonObject.getString("estadoEspacio");

        // Buscar el espacio en la base de datos
        TbEspacio espacio = logica_espacios.buscarEspacio(idEspacio);
        try {
            // Convertir el estado a Enum
            EstadoEspacio estado = EstadoEspacio.valueOf(estadoEspacio);


            // Reactivar o desactivar el espacio según el estado proporcionado
            if (estado == EstadoEspacio.Libre) {
                logica_espacios.reactivarEspacio(espacio);
                enviarRespuesta(resp, HttpServletResponse.SC_OK, "success", "El espacio a sido Reactivado Correctamente");
            }else if (estado == EstadoEspacio.Inactivo) {
                logica_espacios.desactivarEspacio(espacio);
                enviarRespuesta(resp, HttpServletResponse.SC_OK, "success", "El espacio a sido Desactivado Correctamente");
            }
        } catch (IllegalArgumentException e) {
            // Manejar error si el estado proporcionado no es válido
            System.out.println("Estado inválido: " + estadoEspacio);
            enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Error en los datos enviados");
        }catch (Exception e){
            // Manejar errores generales
            enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error interno en el sistema");
        }
    }
}
