package Modelo;

import Logica.Logica_Espacios;

import java.util.Iterator;
import java.util.Set;

public class CasilleroServices {
    private Logica_Espacios logicaEspacios;

    public CasilleroServices(Logica_Espacios logicaEspacios) {
        this.logicaEspacios = logicaEspacios;
    }

    public void crearEspaciosParaSector(TbSectores sector) throws Exception {
        int cantidadEspaciosEsperada = sector.getCant_espacio();
        Set<TbEspacio> espaciosExistentes = sector.getEspacios();
        int cantidadActual = espaciosExistentes.size();

        if (cantidadActual < cantidadEspaciosEsperada) {
            for (int i = cantidadActual; i < cantidadEspaciosEsperada; i++) {
                TbEspacio espacio = new TbEspacio();
                espacio.setCasillero(sector);
                espacio.setEstado_espacio("Libre");
                espacio.setCantidad_cascos(0);
                espacio.setNombre("Espacio" + (i + 1));
                logicaEspacios.crearEspacio(espacio);
            }
            System.out.println("Se crearon " + (cantidadEspaciosEsperada - cantidadActual) + " espacios adicionales.");
        } else if (cantidadActual > cantidadEspaciosEsperada) {
            int espaciosARemover = cantidadActual - cantidadEspaciosEsperada;
            Iterator<TbEspacio> iterator = espaciosExistentes.iterator();
            while (iterator.hasNext() && espaciosARemover > 0) {
                TbEspacio espacio = iterator.next();
                if (espacio.getEstado_espacio().equals("libre")) {
                    logicaEspacios.eliminarEspacio(espacio);
                    iterator.remove();
                    espaciosARemover--;
                }
            }
            System.out.println("Se eliminaron " + (cantidadActual - cantidadEspaciosEsperada) + " espacios sobrantes.");
        } else {
            System.out.println("La cantidad de espacios es la correcta, no se realizaron modificaciones.");
        }
    }
}
