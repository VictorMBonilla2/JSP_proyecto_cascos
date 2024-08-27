package Servlets;

import Modelo.Controladora_logica;
import Modelo.TbVehiculo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SvVehiculo", urlPatterns = {"/Vehiculo"})
public class SvVehiculo extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Controladora_logica controladora_logica = new Controladora_logica();
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


            jsonObject.put("cantidad_cascos", vehiculo.getCant_casco());
            jsonArray.put(jsonObject);
        }

        // Configurar la respuesta HTTP y enviar el JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonArray.toString());

    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

}
