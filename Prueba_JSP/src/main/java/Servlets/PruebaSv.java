package Servlets;


import java.net.HttpURLConnection;
import java.net.URL;

public class PruebaSv {
    public static void main(String[] args) throws Exception {
        String servletUrl = "http://localhost:8080/Prueba_JSP_war_exploded/SvCasillero";

        URL url = new URL(servletUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET"); // o "POST" si tu servlet lo requiere
        int responseCode = connection.getResponseCode();

        System.out.println("Respuesta del servlet: " + responseCode);
    }
}