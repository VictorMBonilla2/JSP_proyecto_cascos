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
    Logica_Persona Logica_Persona = new Logica_Persona();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener el ID del informe desde los parámetros de la solicitud
        String informeIdParam = request.getParameter("idInforme");

        if (informeIdParam == null || informeIdParam.isEmpty()) {
            sendResponse.enviarRespuesta(response,HttpServletResponse.SC_BAD_REQUEST,"error", "El codigo es requerido");
            return;
        }

        try {
            TbInformesUsuarios informe = Logica_Persona.findInformeByCode(informeIdParam);

            if (informe == null) {
                // Si no se encuentra el informe, devolver un mensaje de error en formato JSON
                sendResponse.enviarRespuesta(response,HttpServletResponse.SC_BAD_REQUEST,"error", "No se encontro informes por ese codigo.");
            } else {
                // Si se encuentra el informe, configurar la respuesta para la descarga del archivo
                response.setContentType(informe.getFileType());
                response.setHeader("Content-Disposition", "attachment; filename=\"" + informe.getFileName() + "\"");

                // Escribir los bytes del archivo PDF al flujo de salida de la respuesta
                OutputStream out = response.getOutputStream();
                out.write(informe.getFileData());
                out.flush();
                out.close(); // Asegúrate de cerrar el flujo de salida
            }
        } catch (NumberFormatException e) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"error\": \"ID de informe inválido\"}");
        }
    }
}