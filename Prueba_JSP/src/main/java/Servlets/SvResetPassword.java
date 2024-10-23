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
    // Instancias de lógica para gestionar personas y tokens de recuperación
    Logica_Persona logicaPersona = new Logica_Persona();
    Logica_Tocken logicaTocken = new Logica_Tocken();

    /**
     * Maneja las solicitudes GET para verificar la validez de un token de restablecimiento de contraseña.
     * Si el token es válido, redirige a una página con un formulario para restablecer la contraseña.
     * Si el token es inválido o ha expirado, redirige a una página de error.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error al manejar la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");

        // Verifica si el token es válido
        TbRecuperacion usuario = logicaTocken.buscarPorToken(token);
        if (usuario != null && logicaTocken.isTokenValid(usuario)) {
            // Redirige a la página de restablecimiento de contraseña con el token válido
            request.setAttribute("token", token);
            request.getRequestDispatcher("/restablecerPassword.jsp").forward(request, response);
        } else {
            // Token inválido o expirado, redirige a la página de error
            response.sendRedirect("errorToken.jsp");
        }
    }

    /**
     * Maneja las solicitudes POST para restablecer la contraseña de un usuario.
     * Verifica la validez del token y actualiza la contraseña en la base de datos.
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

        String token = jsonObject.getString("token");
        String nuevaPassword = jsonObject.getString("nuevaPassword");

        // Verificar si el token es válido
        TbRecuperacion tokenObj = logicaTocken.buscarPorToken(token);
        if (tokenObj != null && logicaTocken.isTokenValid(tokenObj)) {
            // Actualizar la contraseña en la base de datos
            ResultadoOperacion resultado = logicaPersona.actualizarPassword(tokenObj.getPersona(), nuevaPassword);
            if (resultado.isExito()) {
                // Invalidar el token después de cambiar la contraseña
                logicaTocken.invalidarToken(tokenObj);
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
            } else {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
            }
        } else {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error al cambiar de contraseña");
        }
    }
}
