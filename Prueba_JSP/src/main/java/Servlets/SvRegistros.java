package Servlets;

import Logica.Logica_Persona;
import Logica.Logica_Registro;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet(name = "SvRegistros", urlPatterns = {"/SvRegistros"})
public class SvRegistros extends HttpServlet {
    // Instancias de la lógica para gestionar registros y personas
    Logica_Registro logica_registro = new Logica_Registro();
    Logica_Persona logicaPersona = new Logica_Persona();

    /**
     * Maneja las solicitudes GET para obtener registros de vehículos asociados a un usuario,
     * presentando la información de manera paginada. Los registros se devuelven en formato JSON.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error al manejar la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String paginaParam = request.getParameter("Pagination");
        int idUsuario = Integer.parseInt(request.getParameter("iduser"));

        int numeroPagina;
        try {
            // Obtener el número de página, si no se proporciona, se asigna la página 1
            numeroPagina = (paginaParam != null) ? Integer.parseInt(paginaParam) : 1;
            if (numeroPagina < 1) {
                numeroPagina = 1;
            }
        } catch (NumberFormatException e) {
            numeroPagina = 1;
        }

        // Obtener los registros paginados del usuario especificado
        Map<String, Object> resultado = logica_registro.ObtenerRegistrosPaginados(idUsuario, numeroPagina);

        if (resultado == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Extraer los registros y la información de paginación del mapa
        List<TbRegistro> registros = (List<TbRegistro>) resultado.get("registros");
        long totalRegistros = (long) resultado.get("totalRegistros");
        int totalPaginas = (int) resultado.get("totalPaginas");
        int paginaActual = (int) resultado.get("paginaActual");

        // Crear el JSONArray para los registros
        JSONArray jsonArray = new JSONArray();

        try {
            // Configurar la respuesta en formato JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Iterar sobre los registros y construir objetos JSON
            for (TbRegistro registro : registros) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("fecha_registro", registro.getFechaRegistro());
                jsonObject.put("placa_vehiculo", registro.getVehiculo().getPlacaVehiculo());
                jsonObject.put("id_espacio", registro.getEspacio().getId_espacio());
                jsonObject.put("fecha_entrada", registro.getFechaEntrada());
                jsonObject.put("documento_aprendiz", registro.getVehiculo().getPersona().getDocumento());
                jsonObject.put("documento_colaborador", registro.getGestor().getDocumento());
                jsonArray.put(jsonObject);
            }

            // Crear el objeto JSON que contiene los registros y la información de paginación
            JSONObject jsonResponse = new JSONObject();
            jsonResponse.put("registros", jsonArray);
            jsonResponse.put("totalRegistros", totalRegistros);
            jsonResponse.put("totalPaginas", totalPaginas);
            jsonResponse.put("paginaActual", paginaActual);

            // Enviar el JSON como respuesta
            PrintWriter out = response.getWriter();
            out.print(jsonResponse.toString());
            out.flush();
        } catch (Exception e) {
            System.out.println("Error al parsear el JSON: " + e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }




    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

}
