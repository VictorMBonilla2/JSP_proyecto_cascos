package Servlets;

import Logica.Logica_Documentos;
import Logica.Logica_Persona;
import Logica.Logica_Rol;
import Modelo.Persona;
import Modelo.Roles;
import Modelo.TbTipoDocumento;
import Modelo.enums.EstadoUsuario;
import Utilidades.JsonReader;
import Utilidades.ResultadoOperacion;
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
import java.util.Map;

import static Utilidades.sendResponse.enviarRespuesta;

@WebServlet(urlPatterns = "/usuarios")
public class SvUsuarios extends HttpServlet {
    Logica_Persona logica_persona = new Logica_Persona();
    Logica_Rol logica_rol = new Logica_Rol();
    Logica_Documentos logicaDocumentos= new Logica_Documentos();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String paginaParam = request.getParameter("Pagination");
        String estadoParam = request.getParameter("estado"); // Estado para filtrar activos o inactivos

        int numeroPagina;
        try {
            numeroPagina = (paginaParam != null) ? Integer.parseInt(paginaParam) : 1;
            if (numeroPagina < 1) {
                numeroPagina = 1;
            }
        } catch (NumberFormatException e) {
            numeroPagina = 1;
        }

        // Crear un mapa para obtener los resultados
        Map<String, Object> resultado;

        if (estadoParam != null && estadoParam.equalsIgnoreCase("activo")) {
            resultado = logica_persona.findUsuariosActivos(numeroPagina); // Obtener usuarios activos
        } else if (estadoParam != null && estadoParam.equalsIgnoreCase("inactivo")) {
            resultado = logica_persona.findUsuariosInactivos(numeroPagina); // Obtener usuarios inactivos
        } else {
            resultado = logica_persona.ObtenerUsuariosPorPagina(numeroPagina); // Obtener todos los usuarios si no se especifica el estado
        }

        // Convertir los resultados a JSON
        JSONObject respuestaJson = new JSONObject();

        List<Persona> usuarios = (List<Persona>) resultado.get("usuarios");
        JSONArray jsonArray = new JSONArray();

        if (usuarios != null && !usuarios.isEmpty()) {
            for (Persona usuario : usuarios) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("idUser", usuario.getId());
                jsonObject.put("documento", usuario.getDocumento());
                jsonObject.put("nombre", usuario.getNombre());
                jsonObject.put("apellido", usuario.getApellido());
                jsonObject.put("correo", usuario.getCorreo());
                jsonObject.put("numero_documento", usuario.getId());
                jsonObject.put("fecha_nacimiento", usuario.getFechaNacimiento());
                jsonObject.put("numeroCelular",usuario.getCelular());
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

                jsonObject.put("doc", docObject);  // Añadir el objeto documento desglosado
                jsonObject.put("rol", rolObject);  // Añadir el objeto rol desglosado

                jsonArray.put(jsonObject);  // Añadir el objeto completo al array JSON
            }
        } else {
            // Devolver una lista vacía si no hay usuarios
            jsonArray = new JSONArray();
        }

// Añadir la lista de usuarios y otros datos a la respuesta JSON
        respuestaJson.put("usuarios", jsonArray);
        respuestaJson.put("totalPaginas", resultado.get("totalPaginas"));
        respuestaJson.put("paginaActual", resultado.get("paginaActual"));
        respuestaJson.put("totalRegistros", resultado.get("totalRegistros"));

        // Configurar la respuesta
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(respuestaJson.toString());
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
            case "change":
                cambiarEstadoUsuario(request, response, jsonObject);
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
            persona.setCelular(jsonObject.getString("numeroCelular"));
            persona.setDocumento(Integer.parseInt(jsonObject.getString("numeroDocumento")));
            persona.setEstadoUsuario(EstadoUsuario.ACTIVO);
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

            ResultadoOperacion resultado = logica_persona.actualizarPersona(persona);
            if (resultado.isExito()){
                enviarRespuesta(response,HttpServletResponse.SC_OK,"success", resultado.getMensaje() );
            }else{
                enviarRespuesta(response,HttpServletResponse.SC_BAD_REQUEST,"error", resultado.getMensaje());
            }

        } catch (ParseException e) {
            enviarRespuesta(response,HttpServletResponse.SC_BAD_REQUEST,"error","Formato de fecha incorrecto.");
        } catch (Exception e) {
            enviarRespuesta(response,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"error","Error al actualizar usuario.");
        }
    }
    private void crearUsuario(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        Persona persona = new Persona();
        try {
            persona.setNombre(jsonObject.getString("nombre"));
            persona.setApellido(jsonObject.getString("apellido"));
            persona.setCorreo(jsonObject.getString("correo"));
            persona.setDocumento(Integer.parseInt(jsonObject.getString("numeroDocumento")));
            persona.setCelular(jsonObject.getString("numeroCelular"));
            persona.setClave(jsonObject.getString("password"));
            persona.setEstadoUsuario(EstadoUsuario.ACTIVO);
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

            ResultadoOperacion resultado= logica_persona.crearPersona(persona);
            if (resultado.isExito()){
                enviarRespuesta(response,HttpServletResponse.SC_OK,"success", resultado.getMensaje() );
            }else {
                enviarRespuesta(response,HttpServletResponse.SC_BAD_REQUEST,"error", resultado.getMensaje() );
            }
        } catch (ParseException e) {
            enviarRespuesta(response,HttpServletResponse.SC_BAD_REQUEST,"error","Formato de fecha incorrecto.");
        } catch (Exception e) {
            enviarRespuesta(response,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"error","Error al actualizar usuario.");
        }
    }
    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject)  throws IOException {
        try{
            int id= jsonObject.getInt("id");
            ResultadoOperacion resultado= logica_persona.borrarUsuario(id);
            if(resultado.isExito()){
                enviarRespuesta(response,HttpServletResponse.SC_OK,"success", resultado.getMensaje());
            }else {
                enviarRespuesta(response,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"error", resultado.getMensaje());
            }
        } catch (Exception e){
            enviarRespuesta(response,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"error","Error al actualizar usuario.");
        }
    }
    private void cambiarEstadoUsuario(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try{
            int id= jsonObject.getInt("id");
            ResultadoOperacion resultado= logica_persona.cambiarEstadoUsuario(id);
            if(resultado.isExito()){
                enviarRespuesta(response,HttpServletResponse.SC_OK,"success", resultado.getMensaje());
            }else {
                enviarRespuesta(response,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"error", resultado.getMensaje());
            }
        } catch (Exception e){
            enviarRespuesta(response,HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"error","Error al actualizar usuario.");
        }
    }

}
