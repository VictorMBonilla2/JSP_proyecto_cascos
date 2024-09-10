package Utilidades;

import Logica.Controladora_logica;
import Modelo.TbSectores;
import Modelo.TbEspacio;

import java.util.Iterator;
import java.util.Set;

public class CasilleroServices {
    Controladora_logica controladora_logica = new Controladora_logica();


    public void crearEspaciosParaSector(int sectorId) throws Exception {
        TbSectores sector = controladora_logica.ConseguirCasillero(sectorId);

        int cantidadEspaciosEsperada = sector.getCant_espacio();
        Set<TbEspacio> espaciosExistentes = sector.getEspacios();
        int cantidadActual = espaciosExistentes.size();

        if (cantidadActual < cantidadEspaciosEsperada) {
            // Crear los espacios necesarios para alcanzar la cantidad deseada
            for (int i = cantidadActual; i < cantidadEspaciosEsperada; i++) {
                TbEspacio espacio = new TbEspacio();
                espacio.setCasillero(sector);
                espacio.setEstado_espacio("Libre");
                espacio.setCantidad_cascos(0);
                espacio.setNombre("Espacio" + (i + 1));
                controladora_logica.crearEspacio(espacio);
            }
            System.out.println("Se crearon " + (cantidadEspaciosEsperada - cantidadActual) + " espacios adicionales.");
        } else if (cantidadActual > cantidadEspaciosEsperada) {
            // Reducir la cantidad de espacios al eliminar los sobrantes
            int espaciosARemover = cantidadActual - cantidadEspaciosEsperada;
            Iterator<TbEspacio> iterator = espaciosExistentes.iterator();
            while (iterator.hasNext() && espaciosARemover > 0) {
                TbEspacio espacio = iterator.next();
                if( espacio.getEstado_espacio().equals("libre")){
                    controladora_logica.eliminarEspacio(espacio);
                    iterator.remove();
                    espaciosARemover--;
                }
            }
            System.out.println("Se eliminaron " + (cantidadActual - cantidadEspaciosEsperada) + " espacios sobrantes.");
        } else {
            // Cantidades son iguales, no se hacen modificaciones
            System.out.println("La cantidad de espacios es la correcta, no se realizaron modificaciones.");
        }
    }

}
