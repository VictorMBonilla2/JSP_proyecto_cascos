package Servlets;

import Logica.Logica_Documentos;
import Logica.Logica_Persona;
import Logica.Logica_Rol;
import Modelo.Persona;
import Modelo.Roles;
import Modelo.TbTipoDocumento;
import Utilidades.JsonReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet(urlPatterns = "/usuarios")
public class SvUsuarios extends HttpServlet {
    Logica_Persona logica_persona = new Logica_Persona();
    Logica_Rol logica_rol = new Logica_Rol();
    Logica_Documentos logicaDocumentos= new Logica_Documentos();
    protected void doGet(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        String paginaParam = request.getParameter("Pagination");

        int numeroPagina;
        try {
            numeroPagina = (paginaParam != null) ? Integer.parseInt(paginaParam) : 1;
            if (numeroPagina < 1) {
                numeroPagina = 1;
            }
        } catch (NumberFormatException e) {
            numeroPagina = 1;
        }

//        List<Persona> Lista= controladora_logica.ObtenerUsuariosPorPagina(numeroPagina);
        List<Persona> Lista= logica_persona.ObtenerUsuariosPorPagina(numeroPagina);
        JSONArray jsonArray = new JSONArray();

        for (Persona usuario : Lista){
            JSONObject jsonObject= new JSONObject();
            jsonObject.put("idUser", usuario.getId());
            jsonObject.put("documento", usuario.getDocumento());
            jsonObject.put("nombre", usuario.getNombre());
            jsonObject.put("apellido", usuario.getApellido());
            jsonObject.put("correo", usuario.getCorreo());
            jsonObject.put("numero_documento", usuario.getId());
            jsonObject.put("fecha_nacimineto", usuario.getFechaNacimiento());
            JSONObject rolObject = new JSONObject();
            if (usuario.getRol() != null) {
                rolObject.put("idRol", usuario.getRol().getId());
                rolObject.put("nombre", usuario.getRol().getNombre());
            }
            JSONObject docObject = new JSONObject();
            if (usuario.getTipoDocumento() != null) {
                docObject.put("idDocumento", usuario.getTipoDocumento().getId());
                docObject.put("nombreDocumento", usuario.getTipoDocumento().getNombreDocumento());
            }
            jsonObject.put("doc", docObject);
            jsonObject.put("rol", rolObject); // Añadir el objeto rol desglosado
            jsonArray.put(jsonObject);
       }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonArray.toString());

    }



    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = JsonReader.parsearJson(request);
        String action = jsonObject.getString("action");

        switch (action) {
            case "add":
                crearUsuario(request, response, jsonObject);
                break; // Importante para evitar que continúe en "edit"
            case "edit":
                editarUsuario(request, response, jsonObject);
                break;
            case "delete":
                eliminarUsuario(request, response, jsonObject);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida");
                break;
        }
    }
    private void editarUsuario(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        Persona persona = new Persona();
        try {
            persona.setId(jsonObject.getInt("idUser"));
            persona.setNombre(jsonObject.getString("nombre"));
            persona.setApellido(jsonObject.getString("apellido"));
            persona.setCorreo(jsonObject.getString("correo"));
            persona.setDocumento(Integer.parseInt(jsonObject.getString("numeroDocumento")));
            System.out.println(Integer.parseInt(jsonObject.getString("numeroDocumento")));
            String fechaNacimientoStr = jsonObject.optString("fechaNacimiento");
            System.out.println("fechaNacimientoStr = " + fechaNacimientoStr);
            
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaNacimiento = formatter.parse(fechaNacimientoStr);
            persona.setFechaNacimiento(fechaNacimiento);

            int rol = Integer.parseInt(jsonObject.getString("rol"));
            Roles roles = logica_rol.ObtenerRol(rol);
            persona.setRol(roles);

            int idDocumento= jsonObject.getInt("tipoDocumneto");
            TbTipoDocumento documento = logicaDocumentos.obtenerDocumentoID(idDocumento);
            persona.setTipoDocumento(documento);

            logica_persona.actualizarPersona(persona);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"status\":\"success\", \"message\":\"Usuario actualizado correctamente\"}");
        } catch (ParseException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Formato de fecha incorrecto.\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Error al actualizar usuario.\"}");
        }
    }
    private void crearUsuario(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        Persona persona = new Persona();
        try {
            persona.setNombre(jsonObject.getString("nombre"));
            persona.setApellido(jsonObject.getString("apellido"));
            persona.setCorreo(jsonObject.getString("correo"));
            persona.setDocumento(Integer.parseInt(jsonObject.getString("numeroDocumento")));
            persona.setClave(jsonObject.getString("password"));
            System.out.println(Integer.parseInt(jsonObject.getString("numeroDocumento")));
            String fechaNacimientoStr = jsonObject.optString("fechaNacimiento");
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaNacimiento = formatter.parse(fechaNacimientoStr);
            persona.setFechaNacimiento(fechaNacimiento);

            int rol = Integer.parseInt(jsonObject.getString("rol"));
            Roles roles = logica_rol.ObtenerRol(rol);
            persona.setRol(roles);

            int idDocumento= jsonObject.getInt("tipoDocumneto");
            TbTipoDocumento documento = logicaDocumentos.obtenerDocumentoID(idDocumento);
            persona.setTipoDocumento(documento);

            logica_persona.crearPersona(persona);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"status\":\"success\", \"message\":\"Usuario actualizado correctamente\"}");
        } catch (ParseException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Formato de fecha incorrecto.\"}");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Error al actualizar usuario.\"}");
        }
    }
    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject)  throws IOException {

        int documneto= jsonObject.getInt("id");

        try{
            logica_persona.borrarUsuario(documneto);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"status\":\"success\", \"message\":\"Usuario eliminado correctamente\"}");
        } catch (Exception e){
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"status\":\"error\", \"message\":\"Error al actualizar usuario.\"}");
        }



    }
}
