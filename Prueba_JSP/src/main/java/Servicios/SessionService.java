package Servicios;

import DTO.UsuarioSesion;
import Utilidades.sendResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

public class SessionService {

    // Método para verificar la sesión y obtener el documento y el rol
    public UsuarioSesion obtenerUsuariodesdeSesion(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false); // false para no crear una nueva sesión si no existe

        if (session == null) {
            sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Sesión no encontrada.");
            return null;
        }

        Integer documentosesionactual = (Integer) session.getAttribute("documento");
        String rol = (String) session.getAttribute("rol"); // Asumiendo que el rol está almacenado en la sesión también

        if (documentosesionactual == null) {
            sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Documento no encontrado en la sesión.");
            return null;
        }

        if (rol == null) {
            sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Rol no encontrado en la sesión.");
            return null;
        }

        // Si todo está bien, retorna el objeto UsuarioSesion
        return new UsuarioSesion(documentosesionactual, rol);
    }

    // Método para verificar si el rol del usuario es correcto
    public boolean verificarRol(HttpServletRequest req, HttpServletResponse resp, String rolEsperado) throws IOException {
        UsuarioSesion usuarioSesion = obtenerUsuariodesdeSesion(req, resp);

        if (usuarioSesion != null && usuarioSesion.getRol().equals(rolEsperado)) {
            return true;
        }

        // Si el rol no coincide, enviar respuesta de error
        sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_UNAUTHORIZED, "error", "Acceso denegado: Rol incorrecto.");
        return false;
    }
}
