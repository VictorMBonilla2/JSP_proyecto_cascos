package Servlets;

import Logica.Logica_Sectores;
import Modelo.Persona;
import Modelo.TbSectores;
import Utilidades.EspacioServiceManager;
import Utilidades.JsonReader;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "SvSectores", urlPatterns = {"/SvSectores"})
public class SvSectores extends HttpServlet {
    // Obtener la instancia de Logica_Sectores desde EspacioServiceManager
    Logica_Sectores logica_sectores = EspacioServiceManager.getInstance().getLogicaSectores();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TbSectores> sectores = logica_sectores.ObtenerSectores();
        request.setAttribute("sectores", sectores);


        JSONArray jsonArray = new JSONArray();

        for (TbSectores sector : sectores){
            JSONObject jsonObject= new JSONObject();
            jsonObject.put("id_sector", sector.getId());
            jsonObject.put("cant_espacio", sector.getCant_espacio());
            jsonArray.put(jsonObject);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonArray.toString());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonObject = JsonReader.parsearJson(request);
        String action = jsonObject.getString("action");

        switch (action) {
            case "add":
                crearSector(request, response, jsonObject);
                break;
            case "edit":
                editSector(request, response, jsonObject);
                break;
            case "delete":
                deleteSector(request, response, jsonObject);
                break;
            default:
                System.out.println("Acción no reconocida");
        }
    }

    private void crearSector(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) {
        TbSectores sector = new TbSectores();
        sector.setCant_espacio(jsonObject.getInt("cantidad_espacios"));

        boolean result = logica_sectores.crearSector(sector);
        if (result) {
            System.out.println("Sector creado exitosamente.");
        } else {
            System.out.println("Error al crear el sector.");
        }
    }

    private void editSector(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) {
        TbSectores sector = new TbSectores();
        sector.setId(jsonObject.getInt("id_sector"));
        sector.setCant_espacio(jsonObject.getInt("cantidad_espacios"));

        boolean result = logica_sectores.actualizarSector(sector);
        if (result) {
            System.out.println("Sector actualizado exitosamente.");
        } else {
            System.out.println("Error al actualizar el sector.");
        }
    }

    private void deleteSector(HttpServletRequest request, HttpServletResponse response, JSONObject jsonObject) {
        int sectorId = jsonObject.getInt("id_sector");
        // Implementa la lógica para eliminar el sector utilizando logica_sectores o un método específico
        System.out.println("Eliminar sector con ID: " + sectorId);
    }
}