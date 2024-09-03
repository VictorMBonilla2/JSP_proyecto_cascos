package Servlets;

import Modelo.Controladora_logica;
import Modelo.TbRegistro;
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

@WebServlet(name = "SvRegistros", urlPatterns = {"/SvRegistros"})
public class SvRegistros extends HttpServlet {
    Controladora_logica controladora_logica = new Controladora_logica();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TbRegistro> registros = controladora_logica.ObtenerRegistros();

    try{
        // Configurar la respuesta para JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        // Convertir la lista de registros a JSON
        JSONArray jsonArray = new JSONArray();
        for (TbRegistro registro : registros) {
            JSONObject jsonObject = new JSONObject();
            System.out.println(registro.getFecha_registro());
            System.out.println(registro.getEspacio().getId_espacio());
            System.out.println(registro.getVehiculo().getPlaca_vehiculo());
            System.out.println(registro.getAprendiz().getDocumento());
            System.out.println(registro.getColaborador().getDocumento());
            jsonObject.put("fecha_reporte", registro.getFecha_registro());
            jsonObject.put("id_espacio", registro.getEspacio().getId_espacio());
            jsonObject.put("placa_vehiculo", registro.getVehiculo().getPlaca_vehiculo());
            jsonObject.put("documento_aprendiz", registro.getAprendiz().getDocumento());
            jsonObject.put("documento_colaborador", registro.getColaborador().getDocumento());
            jsonArray.put(jsonObject);
        }

        // Enviar el JSON como respuesta
        PrintWriter out = response.getWriter();
        out.print(jsonArray.toString());
        out.flush();
    } catch (Exception e){
        System.out.println("Error al parsear el JSON: "+ e);
    }

    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

}
