package Servlets;

import Modelo.Controladora_logica;
import Modelo.Persona;
import Modelo.TbTipovehiculo;
import Modelo.TbVehiculo;
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

@WebServlet(name = "SvVehiculo", urlPatterns = {"/Vehiculo"})
public class SvVehiculo extends HttpServlet {
    Controladora_logica controladora_logica = new Controladora_logica();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String documento = request.getParameter("documento");

        List<TbVehiculo> vehiculos = controladora_logica.buscarVehiculoDePersona(documento);


        // Si la lista está vacía, devolvemos un JSON vacío
        if (vehiculos == null || vehiculos.isEmpty()) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("[]"); // JSON vacío
            return;
        }

        JSONArray jsonArray = new JSONArray();
        for (TbVehiculo vehiculo : vehiculos){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id_vehiculo", vehiculo.getId_vehiculo());
            System.out.println("Tipo de vehiculo:  "+vehiculo.getTipovehiculo());
            System.out.println("Modelo de vehiculo:  "+vehiculo.getModelo_vehiculo());
            jsonObject.put("tipo_vehiculo", vehiculo.getTipovehiculo().getId());
            jsonObject.put("placa", vehiculo.getPlaca_vehiculo());
            jsonObject.put("marca", vehiculo.getMarca_vehiculo());
            jsonObject.put("modelo", vehiculo.getModelo_vehiculo());
            jsonObject.put("color_vehiculo", vehiculo.getColor_vehiculo());
            jsonObject.put("cantidad_cascos", vehiculo.getCant_casco());
            jsonObject.put("ciudad", vehiculo.getCiudad_vehiculo());
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
            int usuario = Integer.parseInt(jsonObject.optString("user"));
            int id_vehiculo = Integer.parseInt(jsonObject.optString("id_vehiculo"));
            String placa = jsonObject.optString("placa_vehiculo");
            String marca = jsonObject.optString("marca_vehiculo");
            String modelo_vehiculo = jsonObject.optString("modelo_vehiculo");
            int tipo_vehiculo = Integer.parseInt(jsonObject.optString("tipo_vehiculo"));
            Integer cantidad_cascos = Integer.parseInt(jsonObject.optString("cantidad_cascos"));
            String color_vehiculo = jsonObject.optString("color_vehiculo");
            String ciudad = jsonObject.optString("ciudad");

            TbTipovehiculo tipovehiculo = controladora_logica.buscarTipoVehiculo(tipo_vehiculo);
            Persona persona = controladora_logica.buscarusuario(usuario);
            TbVehiculo vehiculo = new TbVehiculo();
            vehiculo.setId_vehiculo(id_vehiculo);
            vehiculo.setPlaca_vehiculo(placa);
            vehiculo.setMarca_vehiculo(marca);
            vehiculo.setModelo_vehiculo(modelo_vehiculo);
            vehiculo.setColor_vehiculo(color_vehiculo);
            vehiculo.setCiudad_vehiculo(ciudad);
            vehiculo.setTipovehiculo(tipovehiculo);
            vehiculo.setCant_casco(cantidad_cascos);
            vehiculo.setPersona(persona);

            boolean updated = controladora_logica.actualizarVehiculo(vehiculo);
            if (updated) {
                enviarRespuesta(resp, HttpServletResponse.SC_OK, "success", null);
            } else {
                enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Failed to update vehicle");
            }
        } catch (Exception e) {
            enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error al actualizar el vehículo: " + e.getMessage());
        }
    }

    private void crearVehiculo(HttpServletRequest req, HttpServletResponse resp, JSONObject jsonObject) throws IOException {
        try {
            int usuario = Integer.parseInt(jsonObject.optString("user"));
            String placa = jsonObject.optString("placa_vehiculo");
            String marca = jsonObject.optString("marca_vehiculo");
            String modelo_vehiculo = jsonObject.optString("modelo_vehiculo");
            int tipo_vehiculo = Integer.parseInt(jsonObject.optString("tipo_vehiculo"));
            Integer cantidad_cascos = Integer.parseInt(jsonObject.optString("cantidad_cascos"));
            String color_vehiculo = jsonObject.optString("color");
            String ciudad = jsonObject.optString("ciudad");

            TbTipovehiculo tipovehiculo = controladora_logica.buscarTipoVehiculo(tipo_vehiculo);
            Persona persona = controladora_logica.buscarusuario(usuario);

            TbVehiculo vehiculo = new TbVehiculo();
            vehiculo.setPlaca_vehiculo(placa);
            vehiculo.setMarca_vehiculo(marca);
            vehiculo.setModelo_vehiculo(modelo_vehiculo);
            vehiculo.setColor_vehiculo(color_vehiculo);
            vehiculo.setCiudad_vehiculo(ciudad);
            vehiculo.setTipovehiculo(tipovehiculo);
            vehiculo.setCant_casco(cantidad_cascos);
            vehiculo.setPersona(persona);

            boolean created = controladora_logica.crearVehiculo(vehiculo);
            if (created) {
                enviarRespuesta(resp, HttpServletResponse.SC_OK, "success", null);
            } else {
                enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Failed to create vehicle");
            }
        } catch (Exception e) {
            enviarRespuesta(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "error", "Error al crear el vehículo: " + e.getMessage());
        }
    }

    private void enviarRespuesta(HttpServletResponse resp, int statusCode, String status, String message) throws IOException {
        resp.setContentType("application/json");
        resp.setStatus(statusCode);
        if (message != null) {
            resp.getWriter().write(String.format("{\"status\":\"%s\", \"message\":\"%s\"}", status, message));
        } else {
            resp.getWriter().write(String.format("{\"status\":\"%s\"}", status));
        }
    }
    
}


