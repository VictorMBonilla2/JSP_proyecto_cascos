package Servlets;

import Logica.Logica_Persona;
import Modelo.Persona;
import Utilidades.JsonReader;
import Utilidades.sendResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
@WebServlet("/resetPassword")
public class SvResetPassword extends HttpServlet {
    Logica_Persona logicaPersona = new Logica_Persona();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");

        // Verifica si el token es válido
        Persona usuario = logicaPersona.buscarPorToken(token);
        if (usuario != null && logicaPersona.isTokenValid(usuario)) {
            // Redirige a la página de restablecimiento de contraseña con un formulario
            request.setAttribute("token", token);
            request.getRequestDispatcher("/restablecerPassword.jsp").forward(request, response);
        } else {
            // Token inválido o expirado
            response.sendRedirect("errorToken.jsp");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = JsonReader.parsearJson(request);

        String token = jsonObject.getString("token");
        String nuevaPassword = jsonObject.getString("nuevaPassword");
        System.out.println("Token: "+ token);
        System.out.println("NuevaContraseña: "+ nuevaPassword);
        // Verificar si el token es válido
        System.out.println("Se busca persona por token");
        Persona usuario = logicaPersona.buscarPorToken(token);
        if (usuario != null && logicaPersona.isTokenValid(usuario)) {
            System.out.println("Se consigue a la persona y valido el tocken");
            // Actualizar la contraseña
            logicaPersona.actualizarPassword(usuario, nuevaPassword);
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", "Cambio de contraseña exitoso");
        } else {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error al cambiar de contraseña");
        }
    }

}
