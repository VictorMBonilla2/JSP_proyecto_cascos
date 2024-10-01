package Servlets;


import Logica.Logica_Reportes;
import Modelo.TbReportes;
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

@WebServlet(name = "SvReportes", urlPatterns = {"/SvReportes"})
public class SvReportes extends HttpServlet {
    Logica_Reportes logica_reportes = new Logica_Reportes();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TbReportes> reportes = logica_reportes.ObtenerReportes();
        try {
        // Configurar la respuesta para JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Convertir la lista de registros a JSON
        JSONArray jsonArray = new JSONArray();
        for (TbReportes reporte : reportes) {

            JSONObject jsonObject = new JSONObject();
            System.out.println(reporte.getFecha_reporte());
            System.out.println(reporte.getNombre_reporte());
            System.out.println(reporte.getDescripcion_reporte());
            System.out.println(reporte.getTipoReporte());
            System.out.println(reporte.getDocumentoAprendiz());
            jsonObject.put("fecha_reporte", reporte.getFecha_reporte());
            jsonObject.put("nombre_reporte", reporte.getNombre_reporte());
            jsonObject.put("descripcion_reporte", reporte.getDescripcion_reporte());
            jsonObject.put("tipo_reporte", reporte.getTipoReporte());
            jsonObject.put("documento_aprendiz", reporte.getDocumentoAprendiz());
            jsonObject.put("placa_vehiculo", reporte.getPlacaVehiculo());
            jsonObject.put("documento_colaborador", reporte.getDocumentoColaborador());
            jsonArray.put(jsonObject);
        }

        // Enviar el JSON como respuesta
        PrintWriter out = response.getWriter();
        out.print(jsonArray.toString());
        out.flush();
        }catch (Exception e){
            System.out.println("Error al parsear el JSON: "+ e);
        }
    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

}
