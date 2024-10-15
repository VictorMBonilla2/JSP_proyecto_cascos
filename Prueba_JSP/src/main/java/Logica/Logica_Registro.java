package Logica;

import Controlador.PersistenciaController;
import Modelo.Persona;
import Modelo.TbRegistro;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


public class Logica_Registro {
    PersistenciaController controladora = new PersistenciaController();
    Logica_Persona logicaPersona = new Logica_Persona();

    public Map<String, Object> ObtenerRegistrosPaginados(int idUsuario, int numeroPagina) {
        int tamanioPagina = 20; // Tamaño de la página
        int data_inicio = (numeroPagina - 1) * tamanioPagina;
        List<TbRegistro> lista = null;
        long totalRegistros = 0; // Variable para almacenar el total de registros
        int totalPaginas = 0; // Variable para el total de páginas

        try {
            Persona persona = logicaPersona.buscarpersonaPorId(idUsuario);
            if (persona == null) {
                return null;
            }
            System.out.println(persona.getId());
            int rol = persona.getRol().getId();

            if (rol == 1) { // Gestor
                totalRegistros = controladora.contarRegistrosGestor(persona.getId()); // Obtener el total de registros
                lista = controladora.ObtenerRegistrosGestor(persona.getId(), data_inicio, tamanioPagina);
            } else if (rol == 2) { // Aprendiz
                totalRegistros = controladora.contarRegistrosAprendiz(persona.getId()); // Obtener el total de registros
                lista = controladora.ObtenerRegistrosAprendiz(persona.getId(), data_inicio, tamanioPagina);
            } else if (rol == 3) { // Otro rol (ej. Admin)
                totalRegistros = controladora.contarTodosLosRegistros(); // Obtener el total de registros para admin
                lista = controladora.ObtenerRegistrosAdmin(data_inicio, tamanioPagina); // Obtener registros paginados
            }

            totalPaginas = (int) Math.ceil((double) totalRegistros / tamanioPagina);

        } catch (Exception e) {
            System.err.println("Hubo un error al obtener la lista de registro correspondiente a este usuario: " + e.getMessage());
            return null;
        }

        // Crear el mapa de respuesta con los registros y la información de paginación
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("registros", lista);
        resultado.put("totalRegistros", totalRegistros);
        resultado.put("totalPaginas", totalPaginas);
        resultado.put("paginaActual", numeroPagina);

        return resultado;
    }



    public Map<String, Integer> obtenerRegistrosPorSemana() {
        List<TbRegistro> registros = controladora.ObtenerRegistros();
        System.out.println("Total registros: " + registros.size()); // Verifica la cantidad de registros obtenidos

        // Obtener la fecha de hoy
        LocalDate today = LocalDate.now();

        // Calcular el inicio y fin de la semana actual (de lunes a domingo)
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6); // Domingo de la misma semana

        System.out.println("Semana actual: " + startOfWeek + " - " + endOfWeek);

        // Agrupar registros por fecha dentro de la semana actual
        Map<LocalDate, Long> registrosPorFecha = registros.stream()
                .map(registro -> {
                    Date fecha = registro.getFechaRegistro();
                    // Convertir Date a LocalDate
                    LocalDate fechaLocal = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    System.out.println("Fecha del registro: " + fechaLocal); // Verifica cada fecha de registro
                    return fechaLocal;
                })
                .filter(fecha -> !fecha.isBefore(startOfWeek) && !fecha.isAfter(endOfWeek))
                .collect(Collectors.groupingBy(
                        fecha -> fecha,
                        Collectors.counting()
                ));


        System.out.println("Registros por fecha: " + registrosPorFecha); // Verifica los registros agrupados por fecha

        // Convertir el resultado en un Map con String y Integer usando LinkedHashMap para mantener el orden
        Map<String, Integer> registrosPorSemana = new LinkedHashMap<>();
        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            registrosPorSemana.put(date.toString(), registrosPorFecha.getOrDefault(date, 0L).intValue());
        }

        System.out.println("Registros por semana: " + registrosPorSemana); // Verifica los registros por semana

        return registrosPorSemana;
    }

    public void CrearRegistro(TbRegistro nuevoRegistro) {
        controladora.CrearRegistro(nuevoRegistro);
    }
}
