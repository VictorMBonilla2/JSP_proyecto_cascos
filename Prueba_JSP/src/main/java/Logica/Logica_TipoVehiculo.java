package Logica;

import Controlador.PersistenciaController;
import Modelo.TbTipovehiculo;
import Utilidades.ResultadoOperacion;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;


/**
 * Clase que contiene la lógica relacionada con la gestión de tipos de vehículos.
 * Proporciona métodos para crear, actualizar, eliminar y buscar tipos de vehículos.
 */
public class Logica_TipoVehiculo {

    private final PersistenciaController controladora = new PersistenciaController();

    /**
     * Busca un tipo de vehículo en la base de datos utilizando su ID.
     *
     * @param tipoVehiculo El ID del tipo de vehículo a buscar.
     * @return Un objeto {@link TbTipovehiculo} que representa el tipo de vehículo encontrado.
     */
    public TbTipovehiculo buscarTipoVehiculo(int tipoVehiculo) {
        return controladora.obtenerTipoVehiculoPorId(tipoVehiculo);
    }

    /**
     * Obtiene una lista de todos los tipos de vehículos registrados en la base de datos.
     *
     * @return Una lista de objetos {@link TbTipovehiculo}.
     */
    public List<TbTipovehiculo> ObtenerTiposVehiculo() {
        return controladora.BuscarTiposVehiculo();
    }

    /**
     * Crea un nuevo tipo de vehículo en la base de datos.
     *
     * @param tipoVehiculo El tipo de vehículo a crear.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion crearTipoVehiculo(TbTipovehiculo tipoVehiculo) {
        try {
            controladora.crearTipoVehiculo(tipoVehiculo);
            return new ResultadoOperacion(true, "El tipo de vehículo ha sido creado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al crear el tipo de vehículo: " + e.getMessage());
            return new ResultadoOperacion(false, "Hubo un error al crear el tipo de vehículo en la base de datos.");
        }
    }

    /**
     * Actualiza un tipo de vehículo existente en la base de datos.
     *
     * @param tipoVehiculo El tipo de vehículo con los datos actualizados.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion actualizarTipoVehiculo(TbTipovehiculo tipoVehiculo) {
        try {
            controladora.actualizarTipoVehiculo(tipoVehiculo);
            return new ResultadoOperacion(true, "El tipo de vehículo ha sido actualizado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al actualizar el tipo de vehículo: " + e.getMessage());
            return new ResultadoOperacion(false, "Hubo un error al actualizar el tipo de vehículo en la base de datos.");
        }
    }

    /**
     * Elimina un tipo de vehículo en la base de datos basado en su ID.
     *
     * @param idTipo El ID del tipo de vehículo que se desea eliminar.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion eliminarTipoVehiculo(int idTipo) {
        try {
            controladora.eliminarTipoVehiculo(idTipo);
            return new ResultadoOperacion(true, "El tipo de vehículo ha sido eliminado correctamente.");
        } catch (PersistenceException e) {
            // Manejar la excepción de restricción de integridad
            Throwable cause = e.getCause();
            while (cause != null) {
                if (cause instanceof ConstraintViolationException) {
                    return new ResultadoOperacion(false, "No se puede eliminar el tipo de vehículo porque está en uso.");
                }
                cause = cause.getCause();
            }
            return new ResultadoOperacion(false, "Error de persistencia al eliminar el tipo de vehículo: " + e.getMessage());
        } catch (Exception e) {
            return new ResultadoOperacion(false, "Error inesperado al eliminar el tipo de vehículo: " + e.getMessage());
        }
    }
}

