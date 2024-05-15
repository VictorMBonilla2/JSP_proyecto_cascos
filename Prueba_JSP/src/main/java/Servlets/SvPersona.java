package Servlets;

import Modelo.Controladora_logica;
import Modelo.LoginDTO;
import Modelo.Persona;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.sql.InFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Modelo.Controladora_logica.validarIngreso;


@WebServlet(name = "SvPersona", urlPatterns = {"/SvPersona"})
public class SvPersona extends HttpServlet {
    //CONTROLADORA LOGICA
    Controladora_logica controladora_logica = new Controladora_logica();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if("login".equals(action)){
            int Documento= Integer.parseInt(req.getParameter("documento"));
            String TipoDocumento = req.getParameter("TipoDocumento");
            String clave = req.getParameter("password");
            List<Object> Ingresado = new ArrayList<>();
            Ingresado.add(Documento);
            Ingresado.add(TipoDocumento);
            Ingresado.add(clave);
            for (Object item : Ingresado){
                System.out.println(item);
            }
            boolean validacion = false;
            validacion = validarIngreso(Documento, TipoDocumento, clave);

            if (validacion){
                HttpSession session = req.getSession(true);
                session.setAttribute("Ingresado",Documento);
                resp.sendRedirect("Home.jsp");
            }
            else {

                System.out.println("Error al iniciar secion");
                req.setAttribute("error", "Credenciales inválidas. Por favor, intente nuevamente.");
                RequestDispatcher dispatcher = req.getRequestDispatcher("index.jsp");
                dispatcher.forward(req, resp);

            }


        } else if("registro".equals(action)){

        String nombre = req.getParameter("Nombres");
        String apellido = req.getParameter("Apellidos");
        String TipoDocumento = req.getParameter("TipoDocumento");
        int documento = Integer.parseInt(req.getParameter("documento"));
        String correo = req.getParameter("correo");
        String clave = req.getParameter("password");
        Persona persona = new Persona();
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setTipoDocumento(TipoDocumento);
        persona.setDocumento(documento);
        persona.setCorreo(correo);
        persona.setClave(clave);
        controladora_logica.crearPersona(persona);

        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida");
        }
    }
}
