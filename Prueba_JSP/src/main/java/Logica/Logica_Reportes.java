package Logica;

import Controlador.PersistenciaController;
import Modelo.Persona;
import Modelo.TbReportes;

import java.util.List;

/**
 * Clase que contiene la lógica relacionada con la gestión de reportes.
 * Proporciona métodos para obtener y crear reportes basados en el rol del usuario.
 */
public class Logica_Reportes {

    private final PersistenciaController controladora = new PersistenciaController();
    private final Logica_Persona logicaPersona = new Logica_Persona();

    /**
     * Obtiene una lista de reportes para un usuario según su rol.
     * <p>
     * - Gestor: se obtienen reportes asociados al gestor.
     * <p>
     * - Aprendiz: se obtienen reportes asociados al aprendiz.
     * <p>
     * - Administrador: se obtienen todos los reportes.
     *
     * @param idUsuario El ID del usuario para el cual se desean obtener los reportes.
     * @return Una lista de objetos {@link TbReportes} que representan los reportes del usuario,
     *         o {@code null} si el usuario no existe o si ocurre un error.
     */
    public List<TbReportes> ObtenerReportes(int idUsuario) {
        List<TbReportes> lista = null;
        try {
            Persona persona = logicaPersona.buscarpersonaPorId(idUsuario);
            if (persona == null) {
                return null;  // Si la persona no existe, devolver null
            }

            int rol = persona.getRol().getId();

            if (rol == 1) { // Gestor
                lista = controladora.ObtenerReportesGestor(persona.getId());  // Búsqueda por ID del gestor
            } else if (rol == 2) { // Aprendiz
                lista = controladora.ObtenerReportesAprendiz(persona.getId());  // Búsqueda por ID del aprendiz
            } else if (rol == 3) { // Administrador u otro rol
                lista = controladora.ObtenerReportes();  // Obtener todos los reportes
            }

        } catch (Exception e) {
            System.err.println("Hubo un error al obtener la lista de reportes correspondiente a este usuario: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return lista;
    }

    /**
     * Crea un nuevo reporte en la base de datos.
     *
     * @param nuevoReporte El objeto {@link TbReportes} que contiene los datos del nuevo reporte a crear.
     */
    public void CrearReporte(TbReportes nuevoReporte) {
        try {
            System.out.println("Intento de crear Reporte");
            controladora.CrearReporte(nuevoReporte);
        } catch (Exception e) {
            System.err.println("Hubo un error al crear el nuevo reporte: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

