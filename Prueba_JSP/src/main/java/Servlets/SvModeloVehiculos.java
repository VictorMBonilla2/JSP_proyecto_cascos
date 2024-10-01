package Servlets;

import Logica.Logica_Modelo;
import Modelo.Tb_ModeloVehiculo;
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

@WebServlet(name = "SvModeloVehiculo", urlPatterns = {"/listaModelo"})
public class SvModeloVehiculos extends HttpServlet {

    Logica_Modelo logicaModelo = new Logica_Modelo();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idMarcaVehiculo = Integer.parseInt(request.getParameter("id_Marca"));
        int idTipoVehiculo = Integer.parseInt(request.getParameter("id_Tipo"));

        List<Tb_ModeloVehiculo> modelosVehiculo = logicaModelo.ObtenerModelosPorMarcaYTipo(idMarcaVehiculo, idTipoVehiculo);
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            JSONArray jsonArray = new JSONArray();
            for (Tb_ModeloVehiculo modelo : modelosVehiculo) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id_Modelo", modelo.getId());
                jsonObject.put("nombre_Modelo", modelo.getNombreModelo());
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
