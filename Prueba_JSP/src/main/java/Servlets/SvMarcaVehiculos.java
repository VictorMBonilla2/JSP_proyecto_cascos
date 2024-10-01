package Servlets;


import Logica.Logica_MarcaVehiculo;
import Modelo.Tb_MarcaVehiculo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "SvMarcasVehiculo", urlPatterns = {"/listaMarcas"})
public class SvMarcaVehiculos extends HttpServlet {
    Logica_MarcaVehiculo logica_marcaVehiculo = new Logica_MarcaVehiculo();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idTipoVehiculo = Integer.parseInt(request.getParameter("id_Tipo"));

        List<Tb_MarcaVehiculo> marcasVehiculo = logica_marcaVehiculo.ObtenerMarcasPorTipo(idTipoVehiculo);
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            JSONArray jsonArray = new JSONArray();
            for (Tb_MarcaVehiculo marca : marcasVehiculo) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id_Marca", marca.getId());
                jsonObject.put("nombre_Marca", marca.getNombreMarca());
                jsonArray.put(jsonObject);
            }
            PrintWriter out = response.getWriter();
            out.println(jsonArray.toString());
            out.flush();
        } catch (Exception e) {
            System.out.println("Error al parsear el JSON " + e);
        }

    }
}
