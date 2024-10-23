package Utilidades;

import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Clase utilitaria para leer y parsear JSON desde una solicitud HTTP (HttpServletRequest).
 */
public class JsonReader {

    /**
     * Parsea el cuerpo de una solicitud HTTP (HttpServletRequest) y lo convierte en un objeto JSON.
     *
     * @param request La solicitud HTTP desde la cual se leerá el JSON.
     * @return Un objeto JSONObject que representa el cuerpo del request en formato JSON.
     * @throws IOException Si ocurre un error al leer el cuerpo de la solicitud.
     * @throws JSONException Si el contenido no es un JSON válido.
     */
    public static JSONObject parsearJson(HttpServletRequest request) throws IOException, JSONException {
        BufferedReader reader = request.getReader();
        StringBuilder jsonBuilder = new StringBuilder();
        String line;

        // Lee el cuerpo de la solicitud línea por línea
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }

        // Convierte el cuerpo leído a un string JSON y lo retorna como JSONObject
        String json = jsonBuilder.toString();
        return new JSONObject(json);
    }
}
