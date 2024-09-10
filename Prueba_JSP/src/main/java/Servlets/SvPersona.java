package Servlets;

import Logica.Logica_Persona;
import Logica.Logica_Rol;
import Modelo.Persona;
import Modelo.Roles;
import Utilidades.JsonReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@WebServlet(name = "SvPersona", urlPatterns = {"/SvPersona"})
public class SvPersona extends HttpServlet {
    //CONTROLADORA LOGICA
    Logica_Persona logica_persona = new Logica_Persona();
    Logica_Rol logica_rol = new Logica_Rol();
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Leer el cuerpo de la solicitud
        JSONObject jsonObject;
        try {
            jsonObject = JsonReader.parsearJson(req);
        } catch (Exception e) {
            sendErrorResponse(resp, "Error al parsear el JSON de la solicitud.");
            return;
        }

        String action = jsonObject.optString("action");
        HttpSession session = req.getSession(false);

        try {
            switch (action) {
                case "login":
                    Login(jsonObject, req, resp, session);
                    break;
                case "logout":
                    Logout(resp, session);
                    break;
                case "registro":
                    Registro(jsonObject, resp);
                    break;
                case "editPrimaryDAta":
                    EditPrimaryData(jsonObject, resp , session);
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida");
            }
        } catch (Exception e) {
            sendErrorResponse(resp, "Error interno del servidor: " + e.getMessage());
        }
    }

    private void EditPrimaryData(JSONObject jsonObject, HttpServletResponse resp, HttpSession session) throws IOException {
        // Recupera el ID del usuario desde el JSON
        int usuarioId = jsonObject.getInt("usuarioId");

        // Recupera el usuario de la base de datos utilizando el ID
        Persona user = logica_persona.buscarpersona(usuarioId);
        if (user == null) {
            sendErrorResponse(resp, "Usuario no encontrado.");
            return;
        }

        // Obtiene los datos del JSON
        String nombre = jsonObject.optString("nombre");
        String apellido = jsonObject.optString("apellido");
        String fechaNacimientoStr = jsonObject.optString("fecha");

        // Define el formato de la fecha esperado
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaNacimiento = null;

        try {
            // Convierte el String a Date
            fechaNacimiento = formatter.parse(fechaNacimientoStr);
        } catch (ParseException e) {
            sendErrorResponse(resp, "Formato de fecha inválido. Utiliza el formato dd/MM/yyyy.");
            return;
        }

        // Actualiza los datos del usuario
        user.setNombre(nombre);
        user.setApellido(apellido);
        user.setFechaNacimiento(fechaNacimiento); // Asigna la fecha convertida

        // Guarda los cambios en la base de datos
        boolean updateSuccess = logica_persona.actualizarPersona(user);

        if (updateSuccess) {
            // Actualiza el usuario en la sesión para reflejar los cambios
            session.setAttribute("user", user);

            sendSuccessResponse(resp, "Datos actualizados con éxito");
        } else {
            sendErrorResponse(resp, "Error al actualizar los datos");
        }
    }




    private void Login(JSONObject jsonObject, HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws IOException {
        int documento = jsonObject.getInt("documento");
        String tipoDocumento = jsonObject.getString("tipoDocumento");
        String password = jsonObject.getString("password");
        String rol = jsonObject.getString("rol");

        boolean validacion = logica_persona.validarIngreso(documento, tipoDocumento, password, rol);
        if (validacion) {
            try {
                // Buscar el usuario desde la lógica de negocio
                Persona user = logica_persona.buscarpersona(documento);


                // Configurar la sesión con los atributos necesarios
                session = req.getSession(true);
                session.setAttribute("documento", documento);
                session.setAttribute("user", user);

                sendSuccessResponse(resp, "Login successful");
            } catch (Exception e) {
                sendErrorResponse(resp, "Error interno del servidor: " + e.getMessage());
            }
        } else {
            sendErrorResponse(resp, "Invalid credentials");
        }
    }

    private void Logout(HttpServletResponse resp, HttpSession session) throws IOException {
        if (session != null) {
            session.invalidate();
        }
        sendSuccessResponse(resp, "Logout successful");
    }

    private void Registro(JSONObject jsonObject, HttpServletResponse resp) throws IOException {
        String nombre = jsonObject.getString("nombre");
        String apellido = jsonObject.getString("apellido");
        String tipoDocumento = jsonObject.getString("TipoDocumento");
        int documento = jsonObject.getInt("documento");
        String correo = jsonObject.getString("correo");
        String clave = jsonObject.getString("password");
        int rol = Integer.parseInt(jsonObject.getString("rol"));

        Roles roll = logica_rol.ObtenerRol(rol);
        Persona persona = new Persona();
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setTipoDocumento(tipoDocumento);
        persona.setDocumento(documento);
        persona.setCorreo(correo);
        persona.setClave(clave);
        persona.setRol(roll);

        boolean validacion = logica_persona.crearPersona(persona);

        if (validacion) {
            sendSuccessResponse(resp, "Registro successful");
        } else {
            sendErrorResponse(resp, "Error al registrar la persona");
        }
    }

    private void sendSuccessResponse(HttpServletResponse resp, String message) throws IOException {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", "success");
        jsonResponse.put("message", message);

        sendJsonResponse(resp, jsonResponse);
    }

    private void sendErrorResponse(HttpServletResponse resp, String message) throws IOException {
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", "error");
        jsonResponse.put("message", message);

        sendJsonResponse(resp, jsonResponse);
    }

    private void sendJsonResponse(HttpServletResponse resp, JSONObject jsonResponse) throws IOException {
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            out.print(jsonResponse.toString());
            out.flush();
        }
    }

}
