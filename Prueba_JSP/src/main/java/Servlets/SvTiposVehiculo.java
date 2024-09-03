package Servlets;


import Modelo.Controladora_logica;
import Modelo.TbTipovehiculo;
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

@WebServlet(name = "SvTiposVehiculo", urlPatterns = {"/listaTiposVehiculos"})
public class SvTiposVehiculo extends HttpServlet {
    Controladora_logica controladora_logica = new Controladora_logica();
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TbTipovehiculo> tiposVehiculo = controladora_logica.ObtenerTiposVehiculo();

        try{
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            JSONArray jsonArray = new  JSONArray();
            for (TbTipovehiculo tipo : tiposVehiculo){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id_Tipo", tipo.getId());
                jsonObject.put("nombre_Tipo",tipo.getNombre());
                jsonArray.put(jsonObject);
            }
            PrintWriter out = response.getWriter();
            out.println(jsonArray.toString());
            out.flush();
        }catch (Exception e){
            System.out.println("Error al parsear el JSON "+e);
        }

    }
}
