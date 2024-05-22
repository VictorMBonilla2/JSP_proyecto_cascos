package Servlets;

import Modelo.Controladora_logica;
import Modelo.Persona;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

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
        if ("login".equals(action)) {
            int documento = jsonObject.getInt("documento");
            String tipoDocumento = jsonObject.getString("TipoDocumento");
            String password = jsonObject.getString("password");
            boolean validacion = validarIngreso(documento, tipoDocumento, password);
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

        } else if("registro".equals(action)){

        String nombre = req.getParameter("Nombres");
        String apellido = req.getParameter("Apellidos");
        String TipoDocumento = req.getParameter("TipoDocumento");
        int documento = Integer.parseInt(req.getParameter("documento"));
        String correo = req.getParameter("correo");
        String clave = req.getParameter("password");
        Persona persona = new Persona();
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setTipoDocumento(TipoDocumento);
        persona.setDocumento(documento);
        persona.setCorreo(correo);
        persona.setClave(clave);
        controladora_logica.crearPersona(persona);

        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida");
        }
    }
}
