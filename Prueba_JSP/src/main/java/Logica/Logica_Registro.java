package Logica;

import Controlador.PersistenciaController;
import Modelo.TbRegistro;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Logica_Registro {
    PersistenciaController controladora = new PersistenciaController();
    public List<TbRegistro> ObtenerRegistros() {

        return controladora.ObtenerRegistros();
    }

    public Map<String, Integer> obtenerRegistrosPorSemana() {
        List<TbRegistro> registros = ObtenerRegistros();
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
                    LocalDate fecha = registro.getFecha_registro().toLocalDate();
                    System.out.println("Fecha del registro: " + fecha); // Verifica cada fecha de registro
                    return fecha;
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
