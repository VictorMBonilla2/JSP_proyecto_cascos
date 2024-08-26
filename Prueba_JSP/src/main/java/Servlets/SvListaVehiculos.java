package Servlets;

import DTO.VehiculoDTO;
import Modelo.Controladora_logica;
import Modelo.TbVehiculo;
import Utilidades.JsonReader;
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
    Controladora_logica controladora_logica = new Controladora_logica();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String documento = request.getParameter("documento");
        System.out.println("El documento a consultar es: " + documento);

        List<VehiculoDTO> listaVehiculos = controladora_logica.obtenerVehiculosDePersona(documento);

        // Si la lista está vacía, devolvemos un JSON vacío
        if (listaVehiculos == null || listaVehiculos.isEmpty()) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("[]"); // JSON vacío
            return;
        }

        // Convertir la lista a JSON
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
