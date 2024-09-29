package Modelo;

import Logica.Logica_Espacios;
import Modelo.enums.EstadoEspacio;
import Utilidades.ResultadoOperacion;

import java.util.Iterator;
import java.util.Set;

public class CasilleroServices {

    private Logica_Espacios logicaEspacios;

    // Inyección de Logica_Espacios, que gestionará la interacción con la base de datos a través de los controladores JPA
    public CasilleroServices(Logica_Espacios logicaEspacios) {
        this.logicaEspacios = logicaEspacios;
    }

    public ResultadoOperacion crearEspaciosParaSector(TbSectores sectorActual, TbSectores sectorModificado) {
        try {
            int cantidadEspaciosEsperada = sectorModificado.getCant_espacio();
            Set<TbEspacio> espaciosExistentes = sectorActual.getEspacios();
            int cantidadActual = espaciosExistentes.size();

            // Crear nuevos espacios si faltan
            if (cantidadActual < cantidadEspaciosEsperada) {
                for (int i = cantidadActual; i < cantidadEspaciosEsperada; i++) {
                    TbEspacio espacio = new TbEspacio();
                    espacio.setSector(sectorActual);  // Aquí usamos el sector actual
                    espacio.setEstado_espacio(EstadoEspacio.Libre);
                    espacio.setCantidad_cascos(0);
                    espacio.setNombre("Espacio" + (i + 1));

                    logicaEspacios.crearEspacio(espacio);
                    espaciosExistentes.add(espacio);
                }
                return new ResultadoOperacion(true, "Se crearon " + (cantidadEspaciosEsperada - cantidadActual) + " espacios adicionales.");
            }

            // Eliminar espacios sobrantes si el sector modificado tiene menos espacios que el actual
            else if (cantidadActual > cantidadEspaciosEsperada) {
                int espaciosARemover = cantidadActual - cantidadEspaciosEsperada;
                long espaciosLibres = espaciosExistentes.stream()
                        .filter(espacio -> espacio.getEstado_espacio() == EstadoEspacio.Libre)
                        .count();

                if (espaciosLibres < espaciosARemover) {
                    return new ResultadoOperacion(false, "No hay suficientes espacios libres para eliminar.");
                }

                Iterator<TbEspacio> iterator = espaciosExistentes.iterator();
                while (iterator.hasNext() && espaciosARemover > 0) {
                    TbEspacio espacio = iterator.next();
                    if (espacio.getEstado_espacio() == EstadoEspacio.Libre) {
                        logicaEspacios.eliminarEspacio(espacio);
                        iterator.remove();
                        espaciosARemover--;
                    }
                }
                return new ResultadoOperacion(true, "Se eliminaron " + (cantidadActual - cantidadEspaciosEsperada) + " espacios sobrantes.");
            }

            return new ResultadoOperacion(true, "La cantidad de espacios es la correcta, no se realizaron modificaciones.");
        } catch (Exception e) {
            return new ResultadoOperacion(false, "Error al crear o eliminar espacios: " + e.getMessage());
        }
    }



}
