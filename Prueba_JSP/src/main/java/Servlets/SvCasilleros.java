package Servlets;

import Modelo.Controladora_logica;
import Modelo.Persona;
import Modelo.TbCasillero;
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

        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("hola");
            List<Integer> CantidadCascos= controladora_logica.ObtenerEspacios();

            for (Integer c : CantidadCascos) {
                System.out.println(c.toString());
            }
        }
    }
}
