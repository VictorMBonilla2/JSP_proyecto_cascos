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
    Logica_Persona logicaPersona = new Logica_Persona();
    Logica_Tocken logicaTocken = new Logica_Tocken();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JSONObject jsonObject = JsonReader.parsearJson(request);
        String email = jsonObject.getString("correo");

        try {
            String token = logicaTocken.generarTokenRecuperacion(email); // Genera un token único

            System.out.println("TOken: " +token);
            if (token != null) {
                // Construir el enlace de recuperación
                String enlaceRecuperacion = "http://localhost:8080/Prueba_JSP_war_exploded/resetPassword?token=" + token;

                // Llamar al método que envía el correo electrónico
                logicaTocken.enviarCorreoRecuperacion(email, enlaceRecuperacion);


                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", "Se ha enviado un correo de recuperación");
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "La cuenta vinculada a este correo no existe o esta inactiva.");
            }
        } catch (Exception e) {

            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error interno en el sistema");
        }
    }
}
