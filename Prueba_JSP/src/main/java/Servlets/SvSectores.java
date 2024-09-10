package Servlets;

import Modelo.Controladora_logica;
import Modelo.TbSectores;
import Utilidades.CasilleroServices;
import Utilidades.JsonReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class SvSectores extends HttpServlet {
    Controladora_logica controladora_logica = new Controladora_logica();
    CasilleroServices casilleroServices = new CasilleroServices();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<TbSectores> Sectores= controladora_logica.ObtenerSectores();

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

        boolean result = controladora_logica.crearSector(sector);
        if (result){
            int idSector= sector.getId();
            try{
                casilleroServices.crearEspaciosParaSector(idSector);
            } catch (Exception e){

            }

        }
    }

    private void editSector(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) {
        TbSectores sector = new TbSectores();
        sector.setId(jsonObject.getInt("id_sector"));
        sector.setCant_espacio(jsonObject.getInt("cantidad_espacios"));

        boolean result = controladora_logica.actualizarSector(sector);
        if (result){
            int idSector= sector.getId();
            try{
                casilleroServices.crearEspaciosParaSector(idSector);
            } catch (Exception e){

            }

        }

    }
    private void deleteSector(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) {



    }

}
