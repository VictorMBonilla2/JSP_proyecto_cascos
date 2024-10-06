package Servlets;

import Logica.Logica_Ciudad;
import Logica.Logica_Persona;
import Logica.Logica_TipoVehiculo;
import Logica.Logica_Vehiculo;
import Modelo.*;
import Modelo.enums.ColorVehiculo;
import Utilidades.ResultadoOperacion;
import Utilidades.sendResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener parámetros de la solicitud
        String documento = request.getParameter("documento");
        String placa = request.getParameter("placa");
        String typeSearch = request.getParameter("typesearch");

        // Verificar el tipo de búsqueda
        if (typeSearch == null || typeSearch.trim().isEmpty()) {
            // Caso 1: Si no se especifica "typesearch", usar búsqueda por "documento"
            if (documento == null || documento.trim().isEmpty()) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"error\":\"Documento inválido\"}");
                return;
            }

            // Buscar vehículos por documento
            buscarVehiculosPorDocumento(documento, response);
        } else {
            // Caso 2: Si "typesearch" es "documento" y hay parámetro "documento", buscar por documento
            if (typeSearch.equalsIgnoreCase("documento")) {
                if (documento == null || documento.trim().isEmpty()) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"error\":\"Documento inválido\"}");
                    return;
                }

                // Buscar vehículos por documento
                buscarVehiculosPorDocumento(documento, response);

                // Caso 3: Si "typesearch" es "placa" y hay parámetro "placa", buscar por placa
            } else if (typeSearch.equalsIgnoreCase("placa")) {
                if (placa == null || placa.trim().isEmpty()) {
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"error\":\"Placa inválida\"}");
                    return;
                }

                // Buscar vehículos por placa
                buscarVehiculosPorPlaca(placa, response);
            } else {
                // Si el "typesearch" no es válido, devolver un error
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"error\":\"Tipo de búsqueda inválido\"}");
            }
        }
    }

    // Método para buscar vehículos por documento
    private void buscarVehiculosPorDocumento(String documento, HttpServletResponse response) throws IOException {
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
            JSONObject jsonObject = convertirVehiculoAJson(vehiculo);
            jsonArray.put(jsonObject);
        }

        // Configurar la respuesta HTTP y enviar el JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonArray.toString());
    }

    // Método para buscar vehículos por placa
    private void buscarVehiculosPorPlaca(String placa, HttpServletResponse response) throws IOException {
        // Buscar el vehículo por placa (sabemos que es único, pero lo ponemos en una lista para consistencia)
        List<TbVehiculo> vehiculos = new ArrayList<>();
        TbVehiculo vehiculo = logica_vehiculo.buscarVehiculoPorPlaca(placa);

        // Si el vehículo no se encuentra, devolver un JSON vacío
        if (vehiculo == null) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("[]");
            return;
        }

        // Añadir el vehículo a la lista
        vehiculos.add(vehiculo);

        // Crear un array JSON para almacenar la lista de vehículos
        JSONArray jsonArray = new JSONArray();
        for (TbVehiculo v : vehiculos) {
            JSONObject jsonObject = convertirVehiculoAJson(v);
            jsonArray.put(jsonObject);
        }

        // Configurar la respuesta HTTP y enviar el JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonArray.toString());
    }


    // Método auxiliar para convertir un vehículo en un objeto JSON
    private JSONObject convertirVehiculoAJson(TbVehiculo vehiculo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id_vehiculo", vehiculo.getId_vehiculo());
        jsonObject.put("id_aprendiz", vehiculo.getPersona().getId());

        TbTipovehiculo tipoVehiculo = vehiculo.getTipovehiculo();
        Tb_MarcaVehiculo marcaVehiculo = vehiculo.getMarcaVehiculo();
        Tb_ModeloVehiculo modeloVehiculo = vehiculo.getModeloVehiculo();

        jsonObject.put("tipo_vehiculo", tipoVehiculo != null ? tipoVehiculo.getId() : "N/A");

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

        return jsonObject;
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
                case "delete":
                    borrarVehiculo(req,resp,jsonObject);
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
            Persona persona = logica_persona.buscarPersonaConDocumento(usuario);
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
            int usuario = jsonObject.getInt("idAprendiz");
            String placa = jsonObject.getString("placaVehiculo");
            Integer cantidad_cascos = jsonObject.getInt("cantCascoVehiculo");
            int ciudadId = jsonObject.getInt("ciudadVehiculo"); // Suponemos que se envía un ID de ciudad
            String color = jsonObject.getString("colorVehiculo");

            // Validar el enum del color del vehículo
            ColorVehiculo colorVehiculo;
            try {
                colorVehiculo = ColorVehiculo.valueOf(color); // Convertir a mayúsculas para evitar errores
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
            Tb_CiudadVehiculo ciudadVehiculo = logica_ciudadVehiculo.buscarCiudadPorId(ciudadId); // Lógica para obtener la ciudad por ID
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


