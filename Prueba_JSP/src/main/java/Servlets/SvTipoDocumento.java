package Servlets;

import Controlador.PersistenciaController;
import Logica.Logica_Documentos;
import Modelo.TbTipoDocumento;
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

@WebServlet(name = "tipodocumento", urlPatterns = "/tipoDoc")
public class SvTipoDocumento extends HttpServlet {
        Logica_Documentos logicaDocumentos = new Logica_Documentos();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TbTipoDocumento> lista= logicaDocumentos.obtenerDocumentos();
        try{
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            JSONArray jsonArray = new  JSONArray();
            for (TbTipoDocumento tipo : lista){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id_Tipo", tipo.getId());
                jsonObject.put("nombre_Tipo",tipo.getNombreDocumento());
                jsonArray.put(jsonObject);
            }
            PrintWriter out = response.getWriter();
            out.println(jsonArray.toString());
            out.flush();
        }catch (Exception e){
            System.out.println("Error al parsear el JSON "+e);
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


    }
}
