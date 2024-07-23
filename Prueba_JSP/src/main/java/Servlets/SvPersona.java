package Servlets;

import Modelo.Controladora_logica;
import Modelo.Persona;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import static Modelo.Controladora_logica.validarIngreso;

@WebServlet(name = "SvPersona", urlPatterns = {"/SvPersona"})
public class SvPersona extends HttpServlet {
    //CONTROLADORA LOGICA
    Controladora_logica controladora_logica = new Controladora_logica();
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Leer el cuerpo de la solicitud
        BufferedReader reader = req.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String json = jsonBuilder.toString();

        // Parsear el JSON usando org.json
        JSONObject jsonObject = new JSONObject(json);

        String action = jsonObject.getString("action");
        HttpSession session = req.getSession(false);
        if ("login".equals(action)) {
            int documento = jsonObject.getInt("documento");
            String tipoDocumento = jsonObject.getString("TipoDocumento");
            System.out.println(tipoDocumento);
            String password = jsonObject.getString("password");
            boolean validacion = validarIngreso(documento, tipoDocumento, password);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();

            JSONObject jsonResponse = new JSONObject();
            if (validacion) {
                Persona user = controladora_logica.buscarusuario(documento);

                session = req.getSession(true);
                session.setAttribute("documento", documento);
                session.setAttribute("user", user);
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "Login successful");
            } else {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Invalid credentials");
            }

            out.print(jsonResponse.toString());
            out.flush();

        } else if ("logout".equals(action)) {
            JSONObject jsonResponse = new JSONObject();
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            if (session != null) {
                session.invalidate();
            }
            jsonResponse.put("status", "success");
            out.print(jsonResponse.toString());
            out.flush();
        } else if ("registro".equals(action)) {

            String nombre = jsonObject.getString("nombre");
            String apellido = jsonObject.getString("apellido");
            String TipoDocumento = jsonObject.getString("TipoDocumento");
            int documento = jsonObject.getInt("documento");
            String correo = jsonObject.getString("correo");
            String clave = jsonObject.getString("password");
            String rol = jsonObject.getString("rol");
            System.out.println("Nombre: " + nombre);
            System.out.println("Apellido: " + apellido);
            System.out.println("TipoDocumento: " + TipoDocumento);
            System.out.println("Documento: " + documento);
            System.out.println("Correo: " + correo);
            System.out.println("Clave: " + clave);
            System.out.println("Rol: " + rol);
            Persona persona = new Persona();
            persona.setNombre(nombre);
            persona.setApellido(apellido);
            persona.setTipoDocumento(TipoDocumento);
            persona.setDocumento(documento);
            persona.setCorreo(correo);
            persona.setClave(clave);
            persona.setRol(rol);

            boolean validacion = controladora_logica.crearPersona(persona);
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();

            JSONObject jsonResponse = new JSONObject();
            if (validacion) {
                jsonResponse.put("status", "success");
                jsonResponse.put("message", "Login successful");
            } else {
                jsonResponse.put("status", "error");
                jsonResponse.put("message", "Invalid credentials");
            }

            out.print(jsonResponse.toString());
            out.flush();
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida");
        }
    }
}
