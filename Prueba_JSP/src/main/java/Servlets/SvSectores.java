package Servlets;

import Logica.Logica_Sectores;
import Modelo.TbSectores;
import Utilidades.JsonReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class SvSectores extends HttpServlet {
    Logica_Sectores logica_sectores = new Logica_Sectores();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<TbSectores> Sectores= logica_sectores.ObtenerSectores();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        JSONObject jsonObject= JsonReader.parsearJson(request);

        String action = jsonObject.getString("action");

        switch (action){
            case "add":
                crearSector(request,response,jsonObject);
                break;
            case "edit":
                editSector(request,response,jsonObject);
                break;
            case "delete":
                deleteSector(request,response,jsonObject);
                break;
            default:
                System.out.println("Accion no reconocida");

        }
    }



    private void crearSector(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) {
        TbSectores sector = new TbSectores();

        sector.setCant_espacio(jsonObject.getInt("cantidad_espacios"));

        boolean result = logica_sectores.crearSector(sector);

    }

    private void editSector(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) {
        TbSectores sector = new TbSectores();
        sector.setId(jsonObject.getInt("id_sector"));
        sector.setCant_espacio(jsonObject.getInt("cantidad_espacios"));

        boolean result = logica_sectores.actualizarSector(sector);


    }
    private void deleteSector(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) {



    }

}
