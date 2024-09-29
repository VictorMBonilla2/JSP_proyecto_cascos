package Utilidades;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class sendResponse {
    // Método estático reutilizable para enviar una respuesta en formato JSON
    public static void enviarRespuesta(HttpServletResponse resp, int statusCode, String status, String message) throws  IOException {
        resp.setContentType("application/json");
        resp.setStatus(statusCode);
        String responseMessage = (message != null) ? String.format("{\"status\":\"%s\", \"message\":\"%s\"}", status, message) : String.format("{\"status\":\"%s\"}", status);
        System.out.println("Enviando respuesta: " + responseMessage); // Logging
        resp.getWriter().write(responseMessage);
        resp.getWriter().flush();
    }
}
