package Servlets;

import Logica.Logica_Persona;
import Logica.Logica_Tocken;
import Utilidades.JsonReader;
import Utilidades.sendResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;

@WebServlet("/SvRecuperarContrasena")
public class SvRecuperarContrasena extends HttpServlet {
    // Instancia de la lógica para gestionar tokens de recuperación de contraseñas
    Logica_Tocken logicaTocken = new Logica_Tocken();

    /**
     * Maneja las solicitudes POST para iniciar el proceso de recuperación de contraseña.
     * Genera un token de recuperación y envía un enlace de recuperación al correo proporcionado.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error al manejar la respuesta.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Parsear el JSON de la solicitud
        JSONObject jsonObject = JsonReader.parsearJson(request);
        String email = jsonObject.getString("correo");

        try {
            // Generar el token de recuperación
            String token = logicaTocken.generarTokenRecuperacion(email);
            System.out.println("Token: " + token);

            if (token != null) {
                // Construir el enlace de recuperación
                String enlaceRecuperacion = "http://localhost:8080/Prueba_JSP_war_exploded/resetPassword?token=" + token;

                // Enviar el correo de recuperación
                logicaTocken.enviarCorreoRecuperacion(email, enlaceRecuperacion);

                // Enviar una respuesta de éxito
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", "Se ha enviado un correo de recuperación");
            } else {
                // Enviar una respuesta de error si el correo no está vinculado a una cuenta activa
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "La cuenta vinculada a este correo no existe o está inactiva.");
            }
        } catch (Exception e) {
            // Enviar una respuesta de error interno en caso de excepciones
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error interno en el sistema");
        }
    }
}

