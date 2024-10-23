package Servlets;

import Modelo.enums.ColorVehiculo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "SvColorVehiculo", urlPatterns = {"/listaColores"})
public class SvColorVehiculo extends HttpServlet {

    /**
     * Maneja las solicitudes GET para obtener una lista de los colores de vehículo disponibles,
     * basados en el enum ColorVehiculo, y los devuelve en formato JSON.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error al manejar la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Crear un JSONArray
        JSONArray jsonArray = new JSONArray();

        // Recorrer los valores del enum y añadirlos al JSONArray
        for (ColorVehiculo color : ColorVehiculo.values()) {
            jsonArray.put(color.name());
        }

        // Configurar la respuesta como JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Enviar el JSONArray como respuesta
        PrintWriter out = response.getWriter();
        out.println(jsonArray.toString());
        out.flush();
    }
}
