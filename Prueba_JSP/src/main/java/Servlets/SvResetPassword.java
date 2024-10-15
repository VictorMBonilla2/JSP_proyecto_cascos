package Servlets;

import Logica.Logica_Persona;
import Logica.Logica_Tocken;
import Modelo.TbRecuperacion;
import Utilidades.JsonReader;
import Utilidades.ResultadoOperacion;
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
    Logica_Tocken logicaTocken = new Logica_Tocken();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");

        // Verifica si el token es válido
        TbRecuperacion usuario = logicaTocken.buscarPorToken(token);
        if (usuario != null && logicaTocken.isTokenValid(usuario)) {
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
        TbRecuperacion tokenObj = logicaTocken.buscarPorToken(token);
        if (tokenObj != null && logicaTocken.isTokenValid(tokenObj)) {
            System.out.println("Se consigue a la persona y valido el tocken");
            // Actualizar la contraseña
             ResultadoOperacion resultado = logicaPersona.actualizarPassword(tokenObj.getPersona(), nuevaPassword);
             if (resultado.isExito()){
                 logicaTocken.invalidarToken(tokenObj);
                 sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
             }else{
                 sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
             }

        } else {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error al cambiar de contraseña");
        }
    }

}
