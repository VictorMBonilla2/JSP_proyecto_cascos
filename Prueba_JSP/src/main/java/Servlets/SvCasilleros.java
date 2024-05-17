package Servlets;

import Modelo.*;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static Modelo.Controladora_logica.validarIngreso;

public class SvCasilleros {
    @WebServlet(name = "SvCasillero", urlPatterns = {"/SvCasillero"})

    public static class SvCasillero extends HttpServlet {
        Controladora_logica controladora_logica = new Controladora_logica();

        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException {

            List<TbEspacio> DatosEspacio= controladora_logica.DatosEspacio();
            for (TbEspacio espacio : DatosEspacio) {

                TbCasco casco = espacio.getCasco();
                if (casco != null) {
                    // Si hay un casco asociado al espacio, obtener la placa
                    String placaCasco = casco.getPlaca_casco();
                    // Ahora puedes hacer lo que necesites con la placa del casco
                    System.out.println("Para el espacio " + espacio.getId() + ", la placa del casco es: " + placaCasco);
                } else {
                    // No hay ningún casco asociado a este espacio
                    System.out.println("Para el espacio " + espacio.getId() + ", no hay ningún casco asociado.");
                }
            }

            Integer CantidadCascos= controladora_logica.ObtenerEspacios(1);

            System.out.println("hola");
            request.setAttribute("Espacios", DatosEspacio);
            request.setAttribute("Casilleros", CantidadCascos);
            RequestDispatcher dispatcher = request.getRequestDispatcher("Casilleros.jsp");
            dispatcher.forward(request, response);

        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {



        }
    }
}
