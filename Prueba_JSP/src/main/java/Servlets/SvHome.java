package Servlets;

import Logica.Logica_Registro;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "SvHome", urlPatterns = {"/SvHome"})
public class SvHome extends HttpServlet {
    // Instancia de la lógica de registro
    Logica_Registro logica = new Logica_Registro();

    /**
     * Maneja las solicitudes GET para obtener los registros por semana.
     * Los datos se obtienen a través de la lógica de negocio y se devuelven en formato JSON.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error al manejar la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener los registros por semana a través de la lógica
        Map<String, Integer> registrosPorSemana = logica.obtenerRegistrosPorSemana();

        // Crear el objeto JSON con los datos obtenidos
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("data", registrosPorSemana);

        // Configurar la respuesta como JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Enviar la respuesta JSON al cliente
        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse.toString());
            out.flush();
        }
    }
}
