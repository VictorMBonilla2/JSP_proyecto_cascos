package Utilidades;

import Modelo.Controladora_logica;
import Modelo.TbCasillero;
import Modelo.TbEspacio;

import javax.swing.*;

public class CasilleroServices {
    Controladora_logica controladora_logica = new Controladora_logica();

    public static void main(String[] args) {
        CasilleroServices service = new CasilleroServices();
        int idCasillero = Integer.parseInt(JOptionPane.showInputDialog("ingresa el id del casillero"));

        service.crearEspaciosParaCasillero(idCasillero);
    }


    public void crearEspaciosParaCasillero(int casilleroId) {
        TbCasillero casillero = controladora_logica.ConseguirCasillero(casilleroId);

        for (int i = 0; i < casillero.getCant_espacio(); i++) {
            TbEspacio espacio = new TbEspacio();
            espacio.setCasillero(casillero);
            espacio.setEstado_espacio("Libre");
            espacio.setCantidad_cascos(0);
            espacio.setNombre("Espacio" + (i+1));
            controladora_logica.crearEspacio(espacio);
        }
    }
}
