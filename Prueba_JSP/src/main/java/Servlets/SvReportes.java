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
    // Instancia de la lógica de reportes
    Logica_Reportes logica_reportes = new Logica_Reportes();

    /**
     * Maneja las solicitudes GET para obtener una lista de reportes asociados a un usuario específico.
     * Los reportes se devuelven en formato JSON.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error al manejar la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idUsuario = Integer.parseInt(request.getParameter("iduser"));

        // Obtener la lista de reportes asociados al usuario
        List<TbReportes> reportes = logica_reportes.ObtenerReportes(idUsuario);

        try {
            // Configurar la respuesta en formato JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Convertir la lista de reportes a JSON
            JSONArray jsonArray = new JSONArray();
            for (TbReportes reporte : reportes) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("fecha_reporte", reporte.getFecha_reporte());
                jsonObject.put("nombre_reporte", reporte.getNombre_reporte());
                jsonObject.put("descripcion_reporte", reporte.getDescripcion_reporte());
                jsonObject.put("tipo_reporte", reporte.getTipoReporte());
                jsonObject.put("nombre_aprendiz", reporte.getVehiculo().getPersona().getNombre());
                jsonObject.put("documento_aprendiz", reporte.getVehiculo().getPersona().getDocumento());
                jsonObject.put("placa_vehiculo", reporte.getVehiculo().getPlacaVehiculo());
                jsonObject.put("documento_colaborador", reporte.getGestor().getDocumento());
                jsonObject.put("nombre_colaborador", reporte.getGestor().getNombre());
                jsonArray.put(jsonObject);
            }

            // Enviar el JSON como respuesta
            PrintWriter out = response.getWriter();
            out.print(jsonArray.toString());
            out.flush();
        } catch (Exception e) {
            System.out.println("Error al parsear el JSON: " + e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }


    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

}
