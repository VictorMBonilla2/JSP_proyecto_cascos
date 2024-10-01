package Servlets;

import Logica.Logica_Ciudad;
import Logica.Logica_Persona;
import Logica.Logica_Vehiculo;
import Modelo.*;
import Modelo.enums.ColorVehiculo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static Utilidades.JsonReader.parsearJson;
import static Utilidades.sendResponse.enviarRespuesta;

@WebServlet(name = "SvVehiculoAprendiz", urlPatterns = {"/VehiculoAprendiz"})
public class SvVehiculoAprendiz extends HttpServlet {
    Logica_Vehiculo logica_vehiculo = new Logica_Vehiculo();
    Logica_Persona logica_persona = new Logica_Persona();
    Logica_Ciudad logica_ciudadVehiculo = new Logica_Ciudad();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String documento = request.getParameter("documento");

        // Verificar si el parámetro 'documento' no es nulo ni vacío
        if (documento == null || documento.trim().isEmpty()) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\":\"Documento inválido\"}");
            return;
        }

        List<TbVehiculo> vehiculos = logica_vehiculo.buscarVehiculoDePersona(documento);

        // Si la lista está vacía, devolvemos un JSON vacío
        if (vehiculos == null || vehiculos.isEmpty()) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("[]");
            return;
        }

        JSONArray jsonArray = new JSONArray();
        for (TbVehiculo vehiculo : vehiculos) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id_vehiculo", vehiculo.getId_vehiculo());
            jsonObject.put("id_aprendiz", vehiculo.getPersona().getDocumento());
            // Obtener tipo, marca, y modelo de las relaciones
            TbTipovehiculo tipoVehiculo = vehiculo.getTipovehiculo();
            Tb_MarcaVehiculo marcaVehiculo = vehiculo.getMarcaVehiculo();
            Tb_ModeloVehiculo modeloVehiculo = vehiculo.getModeloVehiculo();

            jsonObject.put("tipo_vehiculo", tipoVehiculo != null ? tipoVehiculo.getId() : "N/A");
            jsonObject.put("marca", marcaVehiculo != null ? marcaVehiculo.getId() : "N/A");
            jsonObject.put("modelo", modeloVehiculo != null ? modeloVehiculo.getId() : "N/A");
            jsonObject.put("placa", vehiculo.getPlacaVehiculo());
            jsonObject.put("color_vehiculo", vehiculo.getColorVehiculo().name()); // Usamos el enum
            jsonObject.put("cantidad_cascos", vehiculo.getCantCasco());
            jsonObject.put("ciudad", vehiculo.getCiudadVehiculo().getId());

            jsonArray.put(jsonObject);
        }

        // Configurar la respuesta HTTP y enviar el JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonArray.toString());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Obtener el JSON del cuerpo de la solicitud
            JSONObject jsonObject = parsearJson(req);

            // Procesar el JSON
            String action = jsonObject.optString("action");
            System.out.println(action);

            switch (action) {
                case "add":
                    crearVehiculo(req, resp, jsonObject);
                    break;
                case "edit":
                    actualizarVehiculo(req, resp, jsonObject);
                    break;
                default:
                    enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Invalid action");
            }

        } catch (JSONException e) {
            // Manejar errores de JSON
            enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Invalid JSON format");
        } catch (IOException e) {
            // Manejar errores de IO
            enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Server error");
        }
    }

    private void actualizarVehiculo(HttpServletRequest req, HttpServletResponse resp, JSONObject jsonObject) throws IOException {
        try {
            // Obtener parámetros del JSON
            int usuario = jsonObject.getInt("idAprendiz");
            int id_vehiculo = jsonObject.getInt("idVehiculo");
            String placa = jsonObject.getString("placaVehiculo");
            Integer cantidad_cascos = jsonObject.getInt("cantCascoVehiculo");
            int ciudadId = jsonObject.getInt("ciudadVehiculo"); // Usamos el ID de la ciudad
            String color = jsonObject.getString("colorVehiculo");

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
            TbTipovehiculo tipovehiculo = logica_vehiculo.buscarTipoVehiculo(tipo_vehiculo);
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
            Persona persona = logica_persona.buscarpersona(usuario);
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
            vehiculo.setMarcaVehiculo(marca);
            vehiculo.setModeloVehiculo(modelo);

            // Actualizar el vehículo
            boolean updated = logica_vehiculo.actualizarVehiculo(vehiculo);
            if (updated) {
                enviarRespuesta(resp, HttpServletResponse.SC_OK, "success", "Vehículo actualizado correctamente.");
            } else {
                enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error al actualizar el vehículo.");
            }

        } catch (NumberFormatException e) {
            enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Error de formato en los datos numéricos.");
        } catch (Exception e) {
            enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error al actualizar el vehículo: " + e.getMessage());
        }
    }



    private void crearVehiculo(HttpServletRequest req, HttpServletResponse resp, JSONObject jsonObject) throws IOException {
        try {
            // Obtener y validar los datos del JSON
            int usuario = Integer.parseInt(jsonObject.optString("user"));
            String placa = jsonObject.optString("placa_vehiculo");
            Integer cantidad_cascos = Integer.parseInt(jsonObject.optString("cantidad_cascos"));
            int ciudadId = Integer.parseInt(jsonObject.optString("ciudad")); // Suponemos que se envía un ID de ciudad
            String color = jsonObject.optString("color_vehiculo");

            // Validar el enum del color del vehículo
            ColorVehiculo colorVehiculo;
            try {
                colorVehiculo = ColorVehiculo.valueOf(color.toUpperCase()); // Convertir a mayúsculas para evitar errores
            } catch (IllegalArgumentException e) {
                enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Color inválido proporcionado.");
                return;
            }

            // Obtener el tipo, marca y modelo desde el JSON
            int tipo_vehiculo = Integer.parseInt(jsonObject.optString("tipo_vehiculo"));
            int marca_vehiculo = Integer.parseInt(jsonObject.optString("marca_vehiculo"));
            int modelo_vehiculo = Integer.parseInt(jsonObject.optString("modelo_vehiculo"));

            // Verificar que el tipo, la marca y el modelo son consistentes
            TbTipovehiculo tipovehiculo = logica_vehiculo.buscarTipoVehiculo(tipo_vehiculo);
            Tb_MarcaVehiculo marca = logica_vehiculo.buscarMarcaPorTipo(marca_vehiculo, tipo_vehiculo);
            Tb_ModeloVehiculo modelo = logica_vehiculo.buscarModeloPorMarcaYTipo(modelo_vehiculo, marca_vehiculo, tipo_vehiculo);

            // Verificar si la ciudad existe
            Tb_CiudadVehiculo ciudadVehiculo = logica_ciudadVehiculo.buscarCiudadPorId(ciudadId); // Lógica para obtener la ciudad por ID
            if (ciudadVehiculo == null) {
                enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Ciudad inválida proporcionada.");
                return;
            }

            // Verificar si el usuario/persona existe
            Persona persona = logica_persona.buscarpersona(usuario);

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
            vehiculo.setMarcaVehiculo(marca);
            vehiculo.setModeloVehiculo(modelo);

            // Guardar el vehículo en la base de datos
            boolean created = logica_vehiculo.crearVehiculo(vehiculo);

            if (created) {
                enviarRespuesta(resp, HttpServletResponse.SC_OK, "success", "Vehículo creado correctamente.");
            } else {
                enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error al crear el vehículo.");
            }
        } catch (NumberFormatException e) {
            enviarRespuesta(resp, HttpServletResponse.SC_BAD_REQUEST, "error", "Error en los datos numéricos: " + e.getMessage());
        } catch (Exception e) {
            enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error al crear el vehículo: " + e.getMessage());
        }
    }





}


