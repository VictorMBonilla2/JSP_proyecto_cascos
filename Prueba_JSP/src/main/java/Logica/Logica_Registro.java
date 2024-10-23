package Logica;

import Controlador.PersistenciaController;
import Modelo.Persona;
import Modelo.TbRegistro;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Clase que contiene la lógica relacionada con la gestión de registros.
 * Proporciona métodos para obtener registros paginados, crear registros y obtener registros por semana.
 */
public class Logica_Registro {

    private final PersistenciaController controladora = new PersistenciaController();
    private final Logica_Persona logicaPersona = new Logica_Persona();

    /**
     * Obtiene registros paginados según el rol del usuario.
     * <p>
     * - Gestor: obtiene registros asociados al gestor.
     * <p>
     * - Aprendiz: obtiene registros asociados al aprendiz.
     * <p>
     * - Administrador (u otros roles): obtiene todos los registros.
     *
     * @param idUsuario   El ID del usuario para el cual se desean obtener los registros.
     * @param numeroPagina El número de la página actual.
     * @return Un mapa que contiene la lista de registros, el total de registros, el total de páginas y la página actual.
     */
    public Map<String, Object> ObtenerRegistrosPaginados(int idUsuario, int numeroPagina) {
        int tamanioPagina = 20; // Tamaño de la página
        int data_inicio = (numeroPagina - 1) * tamanioPagina;
        List<TbRegistro> lista = null;
        long totalRegistros = 0;
        int totalPaginas = 0;

        try {
            Persona persona = logicaPersona.buscarpersonaPorId(idUsuario);
            if (persona == null) {
                return null;
            }
            int rol = persona.getRol().getId();

            if (rol == 1) { // Gestor
                totalRegistros = controladora.contarRegistrosGestor(persona.getId());
                lista = controladora.ObtenerRegistrosGestor(persona.getId(), data_inicio, tamanioPagina);
            } else if (rol == 2) { // Aprendiz
                totalRegistros = controladora.contarRegistrosAprendiz(persona.getId());
                lista = controladora.ObtenerRegistrosAprendiz(persona.getId(), data_inicio, tamanioPagina);
            } else if (rol == 3) { // Administrador u otro rol
                totalRegistros = controladora.contarTodosLosRegistros();
                lista = controladora.ObtenerRegistrosAdmin(data_inicio, tamanioPagina);
            }

            totalPaginas = (int) Math.ceil((double) totalRegistros / tamanioPagina);

        } catch (Exception e) {
            System.err.println("Hubo un error al obtener la lista de registro correspondiente a este usuario: " + e.getMessage());
            return null;
        }

        Map<String, Object> resultado = new HashMap<>();
        resultado.put("registros", lista);
        resultado.put("totalRegistros", totalRegistros);
        resultado.put("totalPaginas", totalPaginas);
        resultado.put("paginaActual", numeroPagina);

        return resultado;
    }

    /**
     * Obtiene el número de registros por día durante la semana actual (lunes a domingo).
     *
     * @return Un mapa que contiene las fechas de la semana actual como claves y el número de registros por día como valores.
     */
    public Map<String, Integer> obtenerRegistrosPorSemana() {
        List<TbRegistro> registros = controladora.ObtenerRegistros();
        System.out.println("Total registros: " + registros.size());

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        System.out.println("Semana actual: " + startOfWeek + " - " + endOfWeek);

        Map<LocalDate, Long> registrosPorFecha = registros.stream()
                .map(registro -> {
                    Date fecha = registro.getFechaRegistro();
                    LocalDate fechaLocal = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return fechaLocal;
                })
                .filter(fecha -> !fecha.isBefore(startOfWeek) && !fecha.isAfter(endOfWeek))
                .collect(Collectors.groupingBy(
                        fecha -> fecha,
                        Collectors.counting()
                ));

        System.out.println("Registros por fecha: " + registrosPorFecha);

        Map<String, Integer> registrosPorSemana = new LinkedHashMap<>();
        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            registrosPorSemana.put(date.toString(), registrosPorFecha.getOrDefault(date, 0L).intValue());
        }

        System.out.println("Registros por semana: " + registrosPorSemana);

        return registrosPorSemana;
    }

    /**
     * Crea un nuevo registro en la base de datos.
     *
     * @param nuevoRegistro El objeto {@link TbRegistro} que contiene los datos del nuevo registro a crear.
     */
    public void CrearRegistro(TbRegistro nuevoRegistro) {
        controladora.CrearRegistro(nuevoRegistro);
    }
}
