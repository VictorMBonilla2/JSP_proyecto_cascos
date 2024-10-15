package Servicios;

import Logica.Logica_Espacios;
import Modelo.TbEspacio;
import Modelo.TbSectores;
import Modelo.enums.EstadoEspacio;
import Utilidades.ResultadoOperacion;

import java.util.*;
import java.util.stream.Collectors;

public class CasilleroServices {

    private Logica_Espacios logicaEspacios;

    // Inyección de Logica_Espacios, que gestionará la interacción con la base de datos a través de los controladores JPA
    public CasilleroServices(Logica_Espacios logicaEspacios) {
        this.logicaEspacios = logicaEspacios;
    }

    public ResultadoOperacion crearEspaciosParaSector(TbSectores sectorActual, TbSectores sectorModificado) {
        try {
            // Validaciones iniciales
            if (sectorActual == null || sectorModificado == null) {
                return new ResultadoOperacion(false, "Los parámetros de sector no pueden ser nulos.");
            }

            int cantidadEspaciosEsperada = sectorModificado.getCant_espacio();
            if (cantidadEspaciosEsperada < 0) {
                return new ResultadoOperacion(false, "La cantidad esperada de espacios no puede ser negativa.");
            }

            Set<TbEspacio> espaciosExistentes = sectorActual.getEspacios();
            List<TbEspacio> listaEspacios = new ArrayList<>(espaciosExistentes);

            // Ordenar la lista original por el número en el nombre del espacio (ascendente)
            listaEspacios.sort(Comparator.comparingInt(espacio -> {
                String nombre = espacio.getNombre();
                try {
                    return Integer.parseInt(nombre.replaceAll("[^0-9]", ""));
                } catch (NumberFormatException e) {
                    // Manejar casos donde el nombre no sigue el formato esperado
                    return 0;
                }
            }));

            // Separar espacios activos e inactivos
            List<TbEspacio> espaciosActivos = new ArrayList<>();
            List<TbEspacio> espaciosInactivos = new ArrayList<>();

            for (TbEspacio espacio : listaEspacios) {
                if (espacio.getEstado_espacio() == EstadoEspacio.Inactivo) {
                    espaciosInactivos.add(espacio);
                } else {
                    espaciosActivos.add(espacio);
                }
            }

            // Invertir solo espaciosActivos para desactivar desde el último hacia el primero
            Collections.reverse(espaciosActivos);
            // No invertir espaciosInactivos para reactivar desde el primero hacia el último

            int cantidadActualActiva = espaciosActivos.size();
            int cantidadInactivos = espaciosInactivos.size();

            // Paso 1: Desactivar espacios si hay más activos de los necesarios
            if (cantidadActualActiva > cantidadEspaciosEsperada) {
                int espaciosARemover = cantidadActualActiva - cantidadEspaciosEsperada;
                long espaciosLibres = espaciosActivos.stream()
                        .filter(espacio -> espacio.getEstado_espacio() == EstadoEspacio.Libre)
                        .count();

                if (espaciosLibres < espaciosARemover) {
                    return new ResultadoOperacion(false, "No hay suficientes espacios libres para desactivar.");
                }

                int desactivados = 0;
                // Iterar desde el último hacia el primero (lista invertida)
                for (int i = 0; i < espaciosActivos.size() && desactivados < espaciosARemover; i++) {
                    TbEspacio espacio = espaciosActivos.get(i);
                    if (espacio.getEstado_espacio() == EstadoEspacio.Libre) {
                        System.out.println("Desactivando espacio: " + espacio.getNombre());
                        logicaEspacios.desactivarEspacio(espacio);
                        desactivados++;
                        // Opcional: Remover el espacio de la lista si ya no está activo
                        // espaciosActivos.remove(i);
                    }
                }

                // Actualizar la cantidad activa después de desactivar
                cantidadActualActiva -= desactivados;

                // Retornar el resultado parcial si ya no es necesario continuar
                if (cantidadActualActiva == cantidadEspaciosEsperada) {
                    return new ResultadoOperacion(true, "Se desactivaron " + desactivados + " espacios.");
                }
                // Si aún se necesita reactivar o crear, continuar con los siguientes pasos
            }

            // Paso 2: Reactivar espacios inactivos hasta alcanzar la cantidad esperada
            int espaciosNecesarios = cantidadEspaciosEsperada - cantidadActualActiva;
            int espaciosAReactivar = Math.min(espaciosNecesarios, cantidadInactivos);

            if (espaciosAReactivar > 0) {
                for (int i = 0; i < espaciosAReactivar; i++) {
                    TbEspacio espacio = espaciosInactivos.get(i);
                    System.out.println("Reactivando espacio: " + espacio.getNombre());
                    logicaEspacios.reactivarEspacio(espacio);
                    espaciosActivos.add(espacio);  // Actualizar la lista de activos
                }
                cantidadActualActiva += espaciosAReactivar;
                // Retornar si ya se ha alcanzado la cantidad esperada
                if (cantidadActualActiva == cantidadEspaciosEsperada) {
                    return new ResultadoOperacion(true, "Se reactivaron " + espaciosAReactivar + " espacios.");
                }
            }

            // Paso 3: Crear nuevos espacios si aún faltan
            espaciosNecesarios = cantidadEspaciosEsperada - cantidadActualActiva;
            if (espaciosNecesarios > 0) {
                for (int i = 0; i < espaciosNecesarios; i++) {
                    TbEspacio nuevoEspacio = new TbEspacio();
                    nuevoEspacio.setSector(sectorActual);
                    nuevoEspacio.setEstado_espacio(EstadoEspacio.Libre);
                    nuevoEspacio.setCantidad_cascos(0);
                    // Generar un nombre único para el nuevo espacio
                    String nombreEspacio = "Espacio" + (listaEspacios.size() + 1 + i);
                    nuevoEspacio.setNombre(nombreEspacio);
                    System.out.println("Creando nuevo espacio: " + nombreEspacio);
                    logicaEspacios.crearEspacio(nuevoEspacio);
                    espaciosActivos.add(nuevoEspacio);  // Actualizar la lista de activos
                }
                return new ResultadoOperacion(true, "Se reactivaron " + espaciosAReactivar + " espacios y se crearon " + espaciosNecesarios + " nuevos espacios.");
            }

            // Paso 4: Verificar si no se realizaron modificaciones
            return new ResultadoOperacion(true, "La cantidad de espacios es la correcta, no se realizaron modificaciones.");
        } catch (Exception e) {
            return new ResultadoOperacion(false, "Error al crear o eliminar espacios: " + e.getMessage());
        }
    }


}
