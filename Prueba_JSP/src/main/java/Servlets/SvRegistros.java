package Servlets;

import Modelo.Controladora_logica;
import Modelo.Persona;
import Modelo.TbRegistro;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "SvRegistros", urlPatterns = {"/SvRegistros"})
public class SvRegistros extends HttpServlet {
    Controladora_logica controladora_logica = new Controladora_logica();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TbRegistro> registros = controladora_logica.ObtenerRegistros();

        // Configurar la respuesta para JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Convertir la lista de registros a JSON
        JSONArray jsonArray = new JSONArray();
        for (TbRegistro registro : registros) {
            JSONObject jsonObject = new JSONObject();
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

    }
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

}
