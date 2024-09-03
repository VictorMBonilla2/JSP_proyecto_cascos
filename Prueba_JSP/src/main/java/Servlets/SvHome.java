package Servlets;

import Modelo.Controladora_logica;
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Controladora_logica controladora = new Controladora_logica();
        Map<String, Integer> registrosPorSemana = controladora.obtenerRegistrosPorSemana();

        // Crear el objeto JSON
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("data", registrosPorSemana);

        // Configurar la respuesta HTTP
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse.toString());
            out.flush();
        }
    }
}
