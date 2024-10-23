package Servlets;

import Logica.Logica_Ciudad;
import Logica.Logica_Persona;
import Logica.Logica_TipoVehiculo;
import Logica.Logica_Vehiculo;
import Modelo.*;
import Modelo.enums.ColorVehiculo;
import Modelo.enums.EstadoVehiculo;
import Utilidades.ResultadoOperacion;
import Utilidades.sendResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Utilidades.JsonReader.parsearJson;
import static Utilidades.sendResponse.enviarRespuesta;

@WebServlet(name = "SvVehiculoAprendiz", urlPatterns = {"/VehiculoAprendiz"})
public class SvVehiculoAprendiz extends HttpServlet {

    Logica_Vehiculo logica_vehiculo = new Logica_Vehiculo();
    Logica_Persona logica_persona = new Logica_Persona();
    Logica_Ciudad logica_ciudadVehiculo = new Logica_Ciudad();
    Logica_TipoVehiculo logicaTipoVehiculo = new Logica_TipoVehiculo();

    /**
     * Maneja las solicitudes GET para buscar vehículos basados en el documento o la placa.
     * Si no se proporciona el parámetro 'typesearch', buscará por documento.
     *
     * @param request  La solicitud HTTP recibida.
     * @param response La respuesta HTTP que se enviará.
     * @throws ServletException En caso de error en el servlet.
     * @throws IOException      En caso de error de E/S.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String documento = request.getParameter("documento");
        String placa = request.getParameter("placa");
        String typeSearch = request.getParameter("typesearch");

        // Verificar el tipo de búsqueda
        if (typeSearch == null || typeSearch.trim().isEmpty()) {
            // Caso 1: Buscar por documento si no se especifica typeSearch
            if (documento == null || documento.trim().isEmpty()) {
                enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Documento inválido proporcionado.");
                return;
            }
            buscarVehiculosPorDocumento(documento, response);
        } else {
            // Caso 2: Buscar por documento si se especifica typeSearch como "documento"
            if (typeSearch.equalsIgnoreCase("documento")) {
                if (documento == null || documento.trim().isEmpty()) {
                    enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Documento inválido proporcionado.");
                    return;
                }
                buscarVehiculosPorDocumento(documento, response);
            }
            // Caso 3: Buscar por placa si se especifica typeSearch como "placa"
            else if (typeSearch.equalsIgnoreCase("placa")) {
                if (placa == null || placa.trim().isEmpty()) {
                    enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Placa inválida proporcionada.");
                    return;
                }
                buscarVehiculosPorPlaca(placa, response);
            } else {
                enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Tipo de búsqueda inválida.");
            }
        }
    }

    /**
     * Busca los vehículos asociados a un documento y envía la respuesta en formato JSON.
     *
     * @param documento El documento del aprendiz.
     * @param response  La respuesta HTTP que se enviará.
     * @throws IOException Si ocurre un error al escribir la respuesta.
     */
    private void buscarVehiculosPorDocumento(String documento, HttpServletResponse response) throws IOException {
        List<TbVehiculo> vehiculos = logica_vehiculo.buscarVehiculoDePersona(documento);

        if (vehiculos == null || vehiculos.isEmpty()) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("[]");
            return;
        }

        JSONArray jsonArray = new JSONArray();
        for (TbVehiculo vehiculo : vehiculos) {
            JSONObject jsonObject = convertirVehiculoAJson(vehiculo);
            jsonArray.put(jsonObject);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonArray.toString());
    }

    /**
     * Busca un vehículo asociado a una placa y envía la respuesta en formato JSON.
     *
     * @param placa    La placa del vehículo.
     * @param response La respuesta HTTP que se enviará.
     * @throws IOException Si ocurre un error al escribir la respuesta.
     */
    private void buscarVehiculosPorPlaca(String placa, HttpServletResponse response) throws IOException {
        TbVehiculo vehiculo = logica_vehiculo.buscarVehiculoPorPlaca(placa);

        if (vehiculo == null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("[]");
            return;
        }

        List<TbVehiculo> vehiculos = new ArrayList<>();
        vehiculos.add(vehiculo);

        JSONArray jsonArray = new JSONArray();
        for (TbVehiculo v : vehiculos) {
            JSONObject jsonObject = convertirVehiculoAJson(v);
            jsonArray.put(jsonObject);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonArray.toString());
    }

    /**
     * Convierte un objeto de vehículo en un objeto JSON con sus propiedades.
     *
     * @param vehiculo El objeto de vehículo a convertir.
     * @return Un objeto JSON con los detalles del vehículo.
     */
    private JSONObject convertirVehiculoAJson(TbVehiculo vehiculo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id_vehiculo", vehiculo.getId_vehiculo());
        jsonObject.put("id_aprendiz", vehiculo.getPersona().getId());

        TbTipovehiculo tipoVehiculo = vehiculo.getTipovehiculo();
        Tb_MarcaVehiculo marcaVehiculo = vehiculo.getModeloVehiculo().getMarcaVehiculo();
        Tb_ModeloVehiculo modeloVehiculo = vehiculo.getModeloVehiculo();

        jsonObject.put("tipo_vehiculo", tipoVehiculo != null ? tipoVehiculo.getId() : "N/A");
        jsonObject.put("nombre_tipo_vehiculo", tipoVehiculo != null ? tipoVehiculo.getNombre() : "N/A");

        if (marcaVehiculo != null) {
            JSONObject marcaObject = new JSONObject();
            marcaObject.put("id_marca", marcaVehiculo.getId());
            marcaObject.put("nombre", marcaVehiculo.getNombreMarca());
            jsonObject.put("marca", marcaObject);
        } else {
            jsonObject.put("marca", JSONObject.NULL);
        }

        jsonObject.put("modelo", modeloVehiculo != null ? modeloVehiculo.getId() : "N/A");
        jsonObject.put("placa", vehiculo.getPlacaVehiculo());
        jsonObject.put("color_vehiculo", vehiculo.getColorVehiculo().name());
        jsonObject.put("cantidad_cascos", vehiculo.getCantCasco());
        jsonObject.put("ciudad", vehiculo.getCiudadVehiculo().getId());
        jsonObject.put("estadoVehiculo", vehiculo.getEstadoVehiculo());

        return jsonObject;
    }



    /**
     * Maneja las solicitudes POST para agregar, editar o eliminar un vehículo.
     * El tipo de acción se determina a partir del campo "action" en el JSON de la solicitud.
     *
     * @param req  La solicitud HTTP recibida, que contiene los datos JSON en su cuerpo.
     * @param resp La respuesta HTTP que se enviará al cliente.
     * @throws ServletException Si ocurre un error específico del servlet.
     * @throws IOException      Si ocurre un error de entrada/salida al procesar la solicitud o respuesta.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Obtener el JSON del cuerpo de la solicitud
            JSONObject jsonObject = parsearJson(req);

            // Procesar el JSON y determinar la acción
            String action = jsonObject.optString("action");
            System.out.println(action);

            // Ejecutar la acción correspondiente según el valor de "action"
            switch (action) {
                case "add":
                    crearVehiculo(req, resp, jsonObject); // Crear un nuevo vehículo
                    break;
                case "edit":
                    actualizarVehiculo(req, resp, jsonObject); // Editar un vehículo existente
                    break;
                case "delete":
                    borrarVehiculo(req, resp, jsonObject); // Eliminar un vehículo existente
                    break;
                default:
                    // Acción no válida, enviar respuesta con error
                    enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Acción inválida");
            }

        } catch (JSONException e) {
            // Manejar errores de formato JSON
            enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Formato JSON inválido");
        } catch (IOException e) {
            // Manejar errores de entrada/salida
            enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado en el servidor");
        }
    }

    /**
     * Actualiza los detalles de un vehículo existente con los datos proporcionados en el JSON de la solicitud.
     *
     * @param req        La solicitud HTTP.
     * @param resp       La respuesta HTTP.
     * @param jsonObject El objeto JSON que contiene los datos del vehículo.
     * @throws IOException Si hay un error al escribir la respuesta.
     */
    private void actualizarVehiculo(HttpServletRequest req, HttpServletResponse resp, JSONObject jsonObject) throws IOException {
        try {
            // Obtener parámetros del JSON
            int usuario = jsonObject.getInt("idAprendiz");
            int id_vehiculo = jsonObject.getInt("idVehiculo");
            String placa = jsonObject.getString("placaVehiculo");
            Integer cantidad_cascos = jsonObject.getInt("cantCascoVehiculo");
            int ciudadId = jsonObject.getInt("ciudadVehiculo"); // Usamos el ID de la ciudad
            String color = jsonObject.getString("colorVehiculo");
            String estado = jsonObject.getString("estadoVehiculo");

            EstadoVehiculo estadoVehiculo;
            try {
                estadoVehiculo = EstadoVehiculo.valueOf(estado);
            } catch (IllegalArgumentException e) {
                enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Estado inválido proporcionado.");
                return;
            }

            // Validar el color con el Enum antes de continuar
            ColorVehiculo colorVehiculo;
            try {
                colorVehiculo = ColorVehiculo.valueOf(color);
            } catch (IllegalArgumentException e) {
                enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Color inválido proporcionado.");
                return;
            }

            // Obtener el tipo, marca y modelo desde el JSON
            int tipo_vehiculo = jsonObject.getInt("tipoVehiculo");
            int marca_vehiculo = jsonObject.getInt("marcaVehiculo");
            int modelo_vehiculo = jsonObject.getInt("modeloVehiculo");

            // Validar que las entidades relacionadas existan
            TbTipovehiculo tipovehiculo = logicaTipoVehiculo.buscarTipoVehiculo(tipo_vehiculo);
            if (tipovehiculo == null) {
                enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Tipo de vehículo no válido.");
                return;
            }

            Tb_MarcaVehiculo marca = logica_vehiculo.buscarMarcaPorTipo(marca_vehiculo, tipo_vehiculo);
            if (marca == null) {
                enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Marca de vehículo no válida para el tipo especificado.");
                return;
            }

            Tb_ModeloVehiculo modelo = logica_vehiculo.buscarModeloPorMarcaYTipo(modelo_vehiculo, marca_vehiculo, tipo_vehiculo);
            if (modelo == null) {
                enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Modelo de vehículo no válido para la marca y tipo especificados.");
                return;
            }

            // Verificar si la ciudad existe
            Tb_CiudadVehiculo ciudadVehiculo = logica_ciudadVehiculo.buscarCiudadPorId(ciudadId); // Lógica para obtener la ciudad por ID
            if (ciudadVehiculo == null) {
                enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Ciudad inválida proporcionada.");
                return;
            }

            // Validar que el usuario/persona exista
            Persona persona = logica_persona.buscarpersonaPorId(usuario);
            if (persona == null) {
                enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Usuario no válido.");
                return;
            }

            // Crear el objeto TbVehiculo
            TbVehiculo vehiculo = new TbVehiculo();
            vehiculo.setId_vehiculo(id_vehiculo);
            vehiculo.setPlacaVehiculo(placa);
            vehiculo.setCantCasco(cantidad_cascos);
            vehiculo.setCiudadVehiculo(ciudadVehiculo); // Asignar la ciudad como entidad
            vehiculo.setColorVehiculo(colorVehiculo); // Usar el enum para el color
            vehiculo.setPersona(persona);
            vehiculo.setTipovehiculo(tipovehiculo);
            vehiculo.setModeloVehiculo(modelo);
            vehiculo.setEstadoVehiculo(estadoVehiculo);

            // Actualizar el vehículo
            ResultadoOperacion result = logica_vehiculo.actualizarVehiculo(vehiculo);
            if (result.isExito()) {
                enviarRespuesta(resp, HttpServletResponse.SC_OK, "success", result.getMensaje());
            } else {
                enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", result.getMensaje());
            }

        } catch (NumberFormatException e) {
            enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Error de formato en los datos numéricos.");
        } catch (Exception e) {
            enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error al actualizar el vehículo: " + e.getMessage());
        }
    }

    /**
     * Crea un nuevo vehículo utilizando los datos proporcionados en el JSON de la solicitud.
     *
     * @param req        La solicitud HTTP.
     * @param resp       La respuesta HTTP.
     * @param jsonObject El objeto JSON que contiene los datos del vehículo.
     * @throws IOException Si hay un error al escribir la respuesta.
     */
    private void crearVehiculo(HttpServletRequest req, HttpServletResponse resp, JSONObject jsonObject) throws IOException {
        try {
            // Obtener y validar los datos del JSON
            int usuario = jsonObject.getInt("idAprendiz"); // ID del usuario
            String placa = jsonObject.getString("placaVehiculo"); // Placa del vehículo
            Integer cantidad_cascos = jsonObject.getInt("cantCascoVehiculo"); // Cantidad de cascos
            int ciudadId = jsonObject.getInt("ciudadVehiculo"); // ID de la ciudad
            String color = jsonObject.getString("colorVehiculo"); // Color del vehículo
            String estado = jsonObject.getString("estadoVehiculo"); // Estado del vehículo

            // Validar el estado del vehículo usando un enum
            EstadoVehiculo estadoVehiculo;
            try {
                estadoVehiculo = EstadoVehiculo.valueOf(estado); // Convertir el estado a enum
            } catch (IllegalArgumentException e) {
                enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Estado inválido proporcionado.");
                return;
            }

            // Validar el enum del color del vehículo
            ColorVehiculo colorVehiculo;
            try {
                colorVehiculo = ColorVehiculo.valueOf(color); // Convertir el color a enum
            } catch (IllegalArgumentException e) {
                enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Color inválido proporcionado.");
                return;
            }

            // Obtener el tipo, marca y modelo desde el JSON
            int tipo_vehiculo = jsonObject.getInt("tipoVehiculo");
            int marca_vehiculo = jsonObject.getInt("marcaVehiculo");
            int modelo_vehiculo = jsonObject.getInt("modeloVehiculo");

            // Verificar que el tipo, la marca y el modelo son consistentes
            TbTipovehiculo tipovehiculo = logicaTipoVehiculo.buscarTipoVehiculo(tipo_vehiculo);
            Tb_MarcaVehiculo marca = logica_vehiculo.buscarMarcaPorTipo(marca_vehiculo, tipo_vehiculo);
            Tb_ModeloVehiculo modelo = logica_vehiculo.buscarModeloPorMarcaYTipo(modelo_vehiculo, marca_vehiculo, tipo_vehiculo);

            // Verificar si la ciudad existe
            Tb_CiudadVehiculo ciudadVehiculo = logica_ciudadVehiculo.buscarCiudadPorId(ciudadId); // Obtener la ciudad por ID
            if (ciudadVehiculo == null) {
                enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Ciudad inválida proporcionada.");
                return;
            }

            // Verificar si el usuario/persona existe
            Persona persona = logica_persona.buscarPersonaConDocumento(usuario);

            // Validar los datos recibidos
            if (tipovehiculo == null || marca == null || modelo == null || persona == null) {
                enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos inválidos. Verifique el tipo, la marca, el modelo o el usuario.");
                return;
            }

            // Crear el objeto TbVehiculo con los datos validados
            TbVehiculo vehiculo = new TbVehiculo();
            vehiculo.setPlacaVehiculo(placa);
            vehiculo.setCantCasco(cantidad_cascos);
            vehiculo.setCiudadVehiculo(ciudadVehiculo); // Usar la entidad Tb_CiudadVehiculo
            vehiculo.setColorVehiculo(colorVehiculo); // Usar el enum para el color
            vehiculo.setPersona(persona);
            vehiculo.setTipovehiculo(tipovehiculo);
            vehiculo.setModeloVehiculo(modelo);
            vehiculo.setEstadoVehiculo(estadoVehiculo);

            // Guardar el vehículo en la base de datos
            ResultadoOperacion result = logica_vehiculo.crearVehiculo(vehiculo);

            if (result.isExito()) {
                Persona updatePerson = logica_persona.buscarPersonaConDocumento(usuario);

                // Obtener la sesión y actualizar el atributo "user"
                HttpSession session = req.getSession(false); // Obtener la sesión actual
                if (session != null) {
                    session.setAttribute("user", updatePerson); // Actualizar el usuario en la sesión
                }
                enviarRespuesta(resp, HttpServletResponse.SC_OK, "success", result.getMensaje());
            } else {
                enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", result.getMensaje());
            }
        } catch (NumberFormatException e) {
            enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Error en los datos numéricos: " + e.getMessage());
        } catch (Exception e) {
            enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error al crear el vehículo: " + e.getMessage());
        }
    }

    /**
     * Borra un vehículo existente utilizando el ID proporcionado en el JSON de la solicitud.
     *
     * @param request     La solicitud HTTP que contiene el objeto JSON.
     * @param response    La respuesta HTTP que se enviará al cliente.
     * @param jsonObject  El objeto JSON que contiene el ID del vehículo a borrar.
     * @throws IOException Si ocurre un error al enviar la respuesta HTTP.
     */
    private void borrarVehiculo(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) throws IOException {
        try {
            // Obtener el idVehiculo desde el JSON
            int idVehiculo = jsonObject.getInt("idVehiculo");

            // Validar que el idVehiculo sea válido
            if (idVehiculo <= 0) {
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "ID de vehículo inválido.");
                return;
            }

            // Intentar borrar el vehículo
            ResultadoOperacion resultado = logica_vehiculo.borrarVehiculo(idVehiculo);
            if (resultado.isExito()) {
                System.out.println("Vehículo eliminado exitosamente.");
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_OK, "success", resultado.getMensaje());
            } else {
                System.out.println("Error al eliminar el vehículo.");
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", resultado.getMensaje());
            }

        } catch (JSONException e) {
            // Manejar el error si los datos JSON son incorrectos
            System.err.println("Error en los datos JSON: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "Datos JSON inválidos.");
        } catch (Exception e) {
            // Manejar cualquier otro tipo de error
            System.err.println("Error inesperado al eliminar el vehículo: " + e.getMessage());
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error inesperado al eliminar el vehículo.");
        }
    }

}


