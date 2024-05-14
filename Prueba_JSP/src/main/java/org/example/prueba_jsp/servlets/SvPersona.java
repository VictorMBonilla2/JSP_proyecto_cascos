package org.example.prueba_jsp.servlets;

import Modelo.Controladora_logica;
import Modelo.LoginDTO;
import Modelo.Persona;
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


@WebServlet(name = "SvPersona", urlPatterns = {"/SvPersona"})
public class SvPersona extends HttpServlet {
    Controladora_logica controladora_logica = new Controladora_logica();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if("login".equals(action)){
            int Documento= Integer.parseInt(req.getParameter("Documento"));
            String TipoDocumento = req.getParameter("TipoDocumento");
            String clave = req.getParameter("clave");
            List<Object> Ingresado= new ArrayList<>();
            Ingresado.add(Documento);
            Ingresado.add(TipoDocumento);
            Ingresado.add(clave);

            List<LoginDTO> infoUser = controladora_logica.login(Documento);

            if(Ingresado.equals(infoUser){


            }

        } else if("registro".equals(action)){

        String nombre = req.getParameter("Nombres");
        String apellido = req.getParameter("Apellidos");
        String TipoDocumento = req.getParameter("TipoDocumento");
        Integer documento = Integer.valueOf(req.getParameter("documento"));
        String correo = req.getParameter("correo");
        String clave = req.getParameter("password");
        Persona persona = new Persona();
        persona.setNombre(nombre);
        persona.setApellido(apellido);
        persona.setTipoDocumento(TipoDocumento);
        persona.setDocumento(documento);
        persona.setCorreo(correo);
        controladora_logica.crearPersona(persona);

        }
        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida");
        }
    }
}
