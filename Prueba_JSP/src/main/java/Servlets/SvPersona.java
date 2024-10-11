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
import Utilidades.sendResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@WebServlet(name = "SvPersona", urlPatterns = {"/SvPersona"})
public class SvPersona extends HttpServlet {
    //CONTROLADORA LOGICA
    Logica_Persona logica_persona = new Logica_Persona();
    Logica_Rol logica_rol = new Logica_Rol();
    Logica_Documentos documentos = new Logica_Documentos();
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id_usuario = Integer.parseInt(request.getParameter("id_usuario"));

        Persona persona = logica_persona.buscarpersonaPorId(id_usuario);

        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("idUsuario", persona.getId());
            jsonObject.put("nombreUsuario", persona.getNombre());
            jsonObject.put("ApellidoUsuario", persona.getApellido());
            jsonObject.put("fechaNacimiento", persona.getFechaNacimiento());
            JSONObject jsonObjectrol = new JSONObject();

            jsonObjectrol.put("idRol", persona.getRol().getId());
            jsonObjectrol.put("nombreRol", persona.getRol().getNombre());
            jsonObject.put("rol",jsonObjectrol);

            jsonObject.put("correoUsuario", persona.getCorreo());
            jsonObject.put("tipoDocumento", persona.getTipoDocumento().getId());
            JSONObject jsonObjectdoc = new JSONObject();

            jsonObjectdoc.put("idTipoDoc", persona.getRol().getId());
            jsonObjectdoc.put("nombreTipoDoc", persona.getRol().getNombre());
            jsonObject.put("tipoDoc",jsonObjectdoc);
            jsonObject.put("documento", persona.getDocumento());
            jsonObject.put("numeroCelular",persona.getCelular());

            // Escribir la respuesta JSON
            PrintWriter out = response.getWriter();
            out.println(jsonObject.toString());
            out.flush();
        } catch (Exception e) {
            System.out.println("Error al parsear el JSON: " + e);
        }

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Leer el cuerpo de la solicitud
        JSONObject jsonObject;
        try {
            jsonObject = JsonReader.parsearJson(req);
        } catch (JSONException e) {
            sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "error al parsear el json");
            return;
        }

        String action = jsonObject.optString("action");
        HttpSession session = req.getSession(false);
        System.out.println("Accion encontrada: "+ action);
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
                    break;
                case "disable":
                    DeshabilarCuenta(jsonObject, resp , session);
                    break;
                case "infoAccount":
                    InfoDeUsuario(jsonObject, resp , session);
                    break;
                default:
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida");
            }
        } catch (Exception e) {
            sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error interno del servidor");
        }
    }




    private void EditPrimaryData(JSONObject jsonObject, HttpServletResponse resp, HttpSession session) throws IOException {
        // Logs para ver los datos recibidos
        System.out.println("EditPrimaryData llamado");
        System.out.println("Datos recibidos: " + jsonObject.toString());

        try {
            // Recupera el ID del usuario desde el JSON
            int usuarioId = jsonObject.getInt("usuarioId");
            System.out.println("Usuario ID: " + usuarioId);

            // Recupera el usuario de la base de datos utilizando el ID
            Persona user = logica_persona.buscarpersonaPorId(usuarioId);
            if (user == null) {
                System.out.println("Usuario no encontrado con ID: " + usuarioId);
                sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Usuario no encontrado");
                return;
            }

            // Obtiene los datos del JSON
            String nombre = jsonObject.getString("nombre");
            String apellido = jsonObject.getString("apellido");
            String fechaNacimientoStr = jsonObject.getString("fecha");
            String correo = jsonObject.getString("correo");
            String numeroCelular = jsonObject.getString("numeroCelular");

            System.out.println("Datos recibidos: nombre=" + nombre + ", apellido=" + apellido + ", fecha=" + fechaNacimientoStr + ", correo=" + correo);

            // Define el formato de la fecha esperado
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaNacimiento = null;

            try {
                // Convierte el String a Date
                fechaNacimiento = formatter.parse(fechaNacimientoStr);
                System.out.println("Fecha convertida: " + fechaNacimiento);
            } catch (ParseException e) {
                System.out.println("Error al convertir la fecha: " + fechaNacimientoStr);
                sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Formato de fecha inválido. Utiliza el formato dd/MM/yyyy.");
                return;
            }

            // Actualiza los datos del usuario
            user.setNombre(nombre);
            user.setApellido(apellido);
            user.setFechaNacimiento(fechaNacimiento); // Asigna la fecha convertida
            user.setCorreo(correo);
            user.setCelular(numeroCelular);

            // Logs para confirmar antes de actualizar
            System.out.println("Datos actualizados para usuario con ID: " + usuarioId);

            // Guarda los cambios en la base de datos
            ResultadoOperacion resultado = logica_persona.actualizarPersona(user);
            if (resultado.isExito()) {
                // Actualiza el usuario en la sesión para reflejar los cambios
                session.setAttribute("user", user);
                sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
            } else {
                sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
            }
        } catch (JSONException e) {
            // Capturar error al obtener datos del JSON
            System.out.println("Error al procesar el JSON: " + e.getMessage());
            sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Error en los datos proporcionados.");
        } catch (NullPointerException e) {
            // Captura de posibles errores de objetos nulos
            System.out.println("Error de NullPointerException: " + e.getMessage());
            sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error interno: Datos faltantes.");
        } catch (Exception e) {
            // Captura de cualquier otra excepción inesperada
            System.out.println("Error inesperado: " + e.getMessage());
            e.printStackTrace(); // Esto ayudará a tener más detalles del error en los logs
            sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error interno del servidor.");
        }
    }

    private void Login(JSONObject jsonObject, HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws IOException {
        int documento = jsonObject.getInt("documento");
        int tipoDocumento = jsonObject.getInt("tipoDocumento");
        String password = jsonObject.getString("password");
        String rol = jsonObject.getString("rol");

        ResultadoOperacion validacion = logica_persona.validarIngreso(documento, tipoDocumento, password, rol);
        if (validacion.isExito()) {
            try {
                // Buscar el usuario por su documento
                Persona user = logica_persona.buscarPersonaConDocumento(documento);

                // Configurar la sesión con los atributos necesarios
                session = req.getSession(true);
                session.setAttribute("documento", user.getDocumento());
                session.setAttribute("user", user);

                sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_OK, "success", validacion.getMensaje());
            } catch (Exception e) {
                // Enviar una respuesta de error interno del servidor en caso de excepciones
                sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error interno del servidor.");
            }
        } else {
            // Enviar una respuesta con error de datos incorrectos si la validación falla
            sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", validacion.getMensaje());
        }


    }

    private void Logout(HttpServletResponse resp, HttpSession session) throws IOException {
        if (session != null) {
            session.invalidate();
        }
        sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_OK, "success", "Desautenticado.");
    }

    private void Registro(JSONObject jsonObject, HttpServletResponse resp) throws IOException, ParseException {
        try{
            String nombre = jsonObject.getString("nombre");
            String apellido = jsonObject.getString("apellido");
            int idDocumento = jsonObject.getInt("TipoDocumento");
            int documento = jsonObject.getInt("documento");
            String correo = jsonObject.getString("correo");
            String clave = jsonObject.getString("password");
            String celular = jsonObject.getString("numeroCelular");
            // Extraer el valor de fecha desde el JSON
            String fecha = jsonObject.getString("fechaNacimiento");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date fechaNacimiento = formatter.parse(fecha);
            TbTipoDocumento tipoDocumento = documentos.obtenerDocumentoID(idDocumento);
            Roles rol = logica_rol.ObtenerRol(2);/*Debe ser el id de rol de aprendiz*/
            Persona persona = new Persona();
            persona.setNombre(nombre);
            persona.setApellido(apellido);
            persona.setTipoDocumento(tipoDocumento);
            persona.setDocumento(documento);
            persona.setCelular(celular);
            persona.setCorreo(correo);
            persona.setClave(clave);
            persona.setRol(rol);
            persona.setFechaNacimiento(fechaNacimiento);
            ResultadoOperacion resultado = logica_persona.crearPersona(persona);

            if (resultado.isExito()) {
                System.out.println("Aprendiz creada exitosamente.");
                sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
            } else {
                System.out.println("Error al crear al Aprendiz.");
                sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
            }
        } catch (ParseException e) {
            sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Formato de fecha incorrectos.");
        } catch (Exception e) {
            sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Error al actualizar usuario.");
        }
    }

    private void DeshabilarCuenta(JSONObject jsonObject, HttpServletResponse resp, HttpSession session) throws IOException {

        int idUsuario= jsonObject.getInt("idUsuario");

        ResultadoOperacion resultado=  logica_persona.cambiarEstadoUsuario(idUsuario);

        if (resultado.isExito()){
            Logout(resp, session);
        } else {
            sendResponse.enviarRespuesta(resp,HttpServletResponse.SC_BAD_REQUEST,"error", resultado.getMensaje());
        }

    }

    private void InfoDeUsuario(JSONObject jsonObject, HttpServletResponse resp, HttpSession session) throws IOException {
        try {
            int idUsuario = jsonObject.getInt("idUsuario");
            String resultado = logica_persona.generarInforme(idUsuario);

            // Comprobar si el resultado es válido
            if (resultado != null && !resultado.isEmpty()) {
                JSONObject jsonResponse = new JSONObject();

                jsonResponse.put("message", "El informe ha sido creado con éxito.");
                jsonResponse.put("status", "success");
                jsonResponse.put("codigoInforme", resultado);  // Retornar el código del informe
                jsonResponse.put("ok", true);  // Indicar que fue exitoso

                // Establecer el estado de la respuesta como exitosa
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");

                // Escribir la respuesta JSON al cliente
                resp.getWriter().write(jsonResponse.toString());
            } else {
                // Enviar una respuesta de error si no se pudo generar el informe
                sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Error al crear el informe");
            }
        } catch (Exception e) {
            System.err.println("Error al crear info del usuario: " + e.getMessage());

            // Enviar respuesta de error en caso de excepción
            sendResponse.enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error interno del servidor");
        }
    }



}
