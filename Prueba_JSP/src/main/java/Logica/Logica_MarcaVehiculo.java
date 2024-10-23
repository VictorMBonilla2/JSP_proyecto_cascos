package Logica;

import Controlador.PersistenciaController;
import Modelo.Tb_MarcaVehiculo;
import Utilidades.ResultadoOperacion;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

/**
 * Clase que contiene la lógica relacionada con la gestión de marcas de vehículos.
 * Proporciona métodos para crear, actualizar, eliminar y buscar marcas de vehículos.
 */
public class Logica_MarcaVehiculo {

    private final PersistenciaController controladora = new PersistenciaController();

    /**
     * Obtiene una lista de marcas de vehículos según el tipo de vehículo.
     *
     * @param idTipoVehiculo El ID del tipo de vehículo.
     * @return Una lista de objetos {@link Tb_MarcaVehiculo} que representan las marcas asociadas al tipo de vehículo, o {@code null} si ocurre un error.
     */
    public List<Tb_MarcaVehiculo> ObtenerMarcasPorTipo(int idTipoVehiculo) {
        try {
            return controladora.buscarMarcasPorTipo(idTipoVehiculo);
        } catch (Exception e) {
            System.err.println("Error al obtener marcas por tipo de vehículo: " + e.getMessage());
            return null;
        }
    }

    /**
     * Crea una nueva marca de vehículo en la base de datos.
     *
     * @param marca El objeto {@link Tb_MarcaVehiculo} que contiene los datos de la nueva marca.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion crearMarcaVehiculo(Tb_MarcaVehiculo marca) {
        try {
            controladora.CrearMarca(marca);
            return new ResultadoOperacion(true, "La marca ha sido creada correctamente.");
        } catch (Exception e) {
            System.err.println("Error al crear la marca de vehículo: " + e.getMessage());
            return new ResultadoOperacion(false, "Hubo un error al crear la marca en la base de datos.");
        }
    }

    /**
     * Actualiza una marca de vehículo existente en la base de datos.
     *
     * @param marca El objeto {@link Tb_MarcaVehiculo} que contiene los datos actualizados de la marca.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion actualizarMarcaVehiculo(Tb_MarcaVehiculo marca) {
        try {
            controladora.ActualizarMarca(marca);
            return new ResultadoOperacion(true, "La marca ha sido actualizada correctamente.");
        } catch (Exception e) {
            System.err.println("Error al actualizar la marca de vehículo: " + e.getMessage());
            return new ResultadoOperacion(false, "Hubo un error al actualizar la marca en la base de datos.");
        }
    }

    /**
     * Elimina una marca de vehículo en la base de datos por su ID.
     *
     * @param idMarca El ID de la marca que se desea eliminar.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion eliminarMarcaVehiculo(int idMarca) {
        try {
            controladora.eliminarMarca(idMarca);
            return new ResultadoOperacion(true, "La marca ha sido eliminada correctamente.");
        } catch (PersistenceException e) {
            // Manejo de excepciones de restricciones de integridad
            Throwable cause = e.getCause();
            while (cause != null) {
                if (cause instanceof ConstraintViolationException) {
                    return new ResultadoOperacion(false, "No se puede eliminar la marca porque está en uso.");
                }
                cause = cause.getCause();
            }
            return new ResultadoOperacion(false, "Error de persistencia al eliminar la marca: " + e.getMessage());
        } catch (Exception e) {
            return new ResultadoOperacion(false, "Error inesperado al eliminar la marca: " + e.getMessage());
        }
    }

    /**
     * Busca una marca de vehículo por su ID.
     *
     * @param idMarcaVehiculo El ID de la marca que se desea buscar.
     * @return El objeto {@link Tb_MarcaVehiculo} correspondiente al ID proporcionado, o {@code null} si no se encuentra.
     */
    public Tb_MarcaVehiculo buscarMarcaPorId(int idMarcaVehiculo) {
        try {
            return controladora.buscarMarcasPorId(idMarcaVehiculo);
        } catch (Exception e) {
            System.err.println("Error al buscar la marca de vehículo: " + e.getMessage());
            return null;
        }
    }
}

