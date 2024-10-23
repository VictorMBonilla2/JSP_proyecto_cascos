package Servlets;

import Logica.Logica_Persona;
import Modelo.TbInformesUsuarios;
import Utilidades.sendResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

@WebServlet("/descargarInforme")
public class SvInformes extends HttpServlet {
    // Instancia de la lógica relacionada con personas
    Logica_Persona Logica_Persona = new Logica_Persona();

    /**
     * Maneja las solicitudes GET para descargar un informe en formato PDF.
     * Busca el informe por el código proporcionado como parámetro y si lo encuentra,
     * envía el archivo al cliente como una descarga. Si no se encuentra o el código es inválido,
     * devuelve una respuesta de error en formato JSON.
     *
     * @param request  La solicitud HTTP.
     * @param response La respuesta HTTP.
     * @throws ServletException Si ocurre un error en el servlet.
     * @throws IOException      Si ocurre un error al manejar la respuesta.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener el ID del informe desde los parámetros de la solicitud
        String informeIdParam = request.getParameter("idInforme");

        // Validar si el parámetro 'idInforme' está presente y no es vacío
        if (informeIdParam == null || informeIdParam.isEmpty()) {
            sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "El código es requerido");
            return;
        }

        try {
            // Buscar el informe utilizando la lógica
            TbInformesUsuarios informe = Logica_Persona.findInformeByCode(informeIdParam);

            if (informe == null) {
                // Si no se encuentra el informe, devolver un mensaje de error en formato JSON
                sendResponse.enviarRespuesta(response, HttpServletResponse.SC_BAD_REQUEST, "error", "No se encontró informe con ese código.");
            } else {
                // Si se encuentra el informe, configurar la respuesta para la descarga del archivo
                response.setContentType(informe.getFileType());
                response.setHeader("Content-Disposition", "attachment; filename=\"" + informe.getFileName() + "\"");

                // Escribir los bytes del archivo al flujo de salida de la respuesta
                OutputStream out = response.getOutputStream();
                out.write(informe.getFileData());
                out.flush();
                out.close(); // Cerrar el flujo de salida para evitar problemas de recursos
            }
        } catch (NumberFormatException e) {
            // Manejar el caso de un código de informe inválido
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"ID de informe inválido\"}");
        }
    }
}
