package Servlets;

import Logica.Logica_Ciudad;
import Modelo.Tb_CiudadVehiculo;
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

@WebServlet(name = "SvCiudades", urlPatterns = "/listaCiudades")
public class SvCiudades extends HttpServlet {
    Logica_Ciudad logicaCiudad = new Logica_Ciudad();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener la lista de ciudades desde la lógica
        List<Tb_CiudadVehiculo> ciudades = logicaCiudad.obtenerCiudades();

        try {
            // Configurar el tipo de contenido de la respuesta como JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Crear un arreglo JSON para las ciudades
            JSONArray jsonArray = new JSONArray();

            // Recorrer la lista de ciudades y crear objetos JSON
            for (Tb_CiudadVehiculo ciudad : ciudades) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id_Ciudad", ciudad.getId());
                jsonObject.put("nombre_Ciudad", ciudad.getNombreCiudad());
                jsonArray.put(jsonObject); // Añadir el objeto al array JSON
            }

            // Escribir la respuesta
            PrintWriter out = response.getWriter();
            out.println(jsonArray.toString());
            out.flush();
        } catch (Exception e) {
            System.out.println("Error al parsear el JSON " + e);
        }
    }
}
