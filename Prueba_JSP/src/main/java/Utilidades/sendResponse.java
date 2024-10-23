package Utilidades;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Clase utilitaria para enviar respuestas HTTP en formato JSON.
 */
public class sendResponse {

    /**
     * Envía una respuesta HTTP en formato JSON con un código de estado, estado de respuesta y un mensaje opcional.
     *
     * @param resp       El objeto HttpServletResponse para enviar la respuesta.
     * @param statusCode El código de estado HTTP (por ejemplo, 200 para éxito, 404 para no encontrado).
     * @param status     El estado de la respuesta, generalmente una cadena que describe el resultado (por ejemplo, "success" o "error").
     * @param message    Un mensaje opcional adicional que proporciona más contexto sobre la respuesta.
     * @throws IOException Si ocurre un error al escribir la respuesta en el cliente.
     */
    public static void enviarRespuesta(HttpServletResponse resp, int statusCode, String status, String message) throws IOException {
        // Configura el tipo de contenido de la respuesta como JSON
        resp.setContentType("application/json");

        // Establece el código de estado HTTP de la respuesta
        resp.setStatus(statusCode);

        // Construye el mensaje de respuesta en formato JSON
        String responseMessage = (message != null)
                ? String.format("{\"status\":\"%s\", \"message\":\"%s\"}", status, message)
                : String.format("{\"status\":\"%s\"}", status);

        // Logging de la respuesta que se va a enviar
        System.out.println("Enviando respuesta: " + responseMessage);

        // Envía el mensaje JSON en la respuesta
        resp.getWriter().write(responseMessage);
        resp.getWriter().flush();
    }
}
