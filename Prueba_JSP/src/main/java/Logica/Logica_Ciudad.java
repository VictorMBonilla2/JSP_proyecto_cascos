package Logica;

import Controlador.PersistenciaController;
import Modelo.Tb_CiudadVehiculo;
import Utilidades.ResultadoOperacion;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;



/**
 * Clase que contiene la lógica relacionada con la gestión de ciudades.
 * Proporciona métodos para crear, actualizar, eliminar y buscar ciudades en la base de datos.
 */
public class Logica_Ciudad {

    private final PersistenciaController controladora = new PersistenciaController();

    /**
     * Obtiene una lista de todas las ciudades registradas en la base de datos.
     *
     * @return Una lista de objetos {@link Tb_CiudadVehiculo} que representan las ciudades, o {@code null} si ocurre un error.
     */
    public List<Tb_CiudadVehiculo> obtenerCiudades() {
        try {
            return controladora.ObtenerCiudades();
        } catch (Exception e) {
            System.err.println("Error al obtener las ciudades: " + e.getMessage());
            return null;
        }
    }

    /**
     * Actualiza una ciudad en la base de datos.
     *
     * @param ciudad El objeto {@link Tb_CiudadVehiculo} que contiene los datos actualizados de la ciudad.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion actualizarCiudad(Tb_CiudadVehiculo ciudad) {
        try {
            controladora.ActualizarCiudad(ciudad);
            return new ResultadoOperacion(true, "La ciudad ha sido actualizada correctamente.");
        } catch (Exception e) {
            System.err.println("Error al actualizar la ciudad: " + e.getMessage());
            return new ResultadoOperacion(false, "Hubo un error al actualizar la ciudad en la base de datos.");
        }
    }

    /**
     * Elimina una ciudad en la base de datos por su ID.
     *
     * @param idCiudad El ID de la ciudad que se desea eliminar.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion eliminarCiudad(int idCiudad) {
        try {
            controladora.EliminarCiudad(idCiudad);
            return new ResultadoOperacion(true, "La ciudad ha sido eliminada correctamente.");
        } catch (PersistenceException e) {
            // Manejo de excepciones de restricciones de integridad
            Throwable cause = e.getCause();
            while (cause != null) {
                if (cause instanceof ConstraintViolationException) {
                    return new ResultadoOperacion(false, "No se puede eliminar la ciudad porque está en uso.");
                }
                cause = cause.getCause();
            }
            return new ResultadoOperacion(false, "Error de persistencia al eliminar la ciudad: " + e.getMessage());
        } catch (Exception e) {
            return new ResultadoOperacion(false, "Error inesperado al eliminar la ciudad: " + e.getMessage());
        }
    }

    /**
     * Crea una nueva ciudad en la base de datos.
     *
     * @param ciudad El objeto {@link Tb_CiudadVehiculo} que contiene los datos de la nueva ciudad.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion crearCiudad(Tb_CiudadVehiculo ciudad) {
        try {
            controladora.CrearCiudad(ciudad);
            return new ResultadoOperacion(true, "La ciudad ha sido creada correctamente.");
        } catch (Exception e) {
            System.err.println("Error al crear la ciudad: " + e.getMessage());
            return new ResultadoOperacion(false, "Hubo un error al crear la ciudad en la base de datos.");
        }
    }

    /**
     * Busca una ciudad específica por su ID.
     *
     * @param ciudadId El ID de la ciudad a buscar.
     * @return El objeto {@link Tb_CiudadVehiculo} correspondiente al ID proporcionado, o {@code null} si no se encuentra.
     */
    public Tb_CiudadVehiculo buscarCiudadPorId(int ciudadId) {
        try {
            return controladora.BuscarCiudad(ciudadId);
        } catch (Exception e) {
            System.err.println("Error al buscar la ciudad: " + e.getMessage());
            return null;
        }
    }
}
