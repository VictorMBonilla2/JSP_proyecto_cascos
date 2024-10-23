package Servlets;

import DTO.VehiculoDTO;
import Logica.Logica_Vehiculo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SvListaVehiculos", urlPatterns = {"/listaVehiculos"})
public class SvListaVehiculos extends HttpServlet {
    // Instancia de la lógica relacionada con vehículos
    Logica_Vehiculo logica_vehiculo = new Logica_Vehiculo();

    /**
     * Maneja las solicitudes GET para obtener la lista de vehículos de una persona,
     * basándose en el documento proporcionado como parámetro en la solicitud.
     * Devuelve la lista de vehículos en formato JSON.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error al manejar la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener el parámetro 'documento' de la solicitud
        String documento = request.getParameter("documento");
        System.out.println("El documento a consultar es: " + documento);

        // Obtener la lista de vehículos asociados al documento
        List<VehiculoDTO> listaVehiculos = logica_vehiculo.obtenerVehiculosDePersona(documento);

        // Si la lista está vacía, devolver un JSON vacío
        if (listaVehiculos == null || listaVehiculos.isEmpty()) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("[]"); // JSON vacío
            return;
        }

        // Convertir la lista de vehículos a formato JSON
        JSONArray jsonArray = new JSONArray();
        for (VehiculoDTO vehiculo : listaVehiculos) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", vehiculo.getId());
            jsonObject.put("placa", vehiculo.getPlaca_vehiculo());
            jsonObject.put("marca", vehiculo.getMarca_vehiculo());
            jsonObject.put("cantidad_cascos", vehiculo.getCant_cascos());
            jsonArray.put(jsonObject);
        }

        // Configurar la respuesta HTTP y enviar el JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonArray.toString());
    }
}

