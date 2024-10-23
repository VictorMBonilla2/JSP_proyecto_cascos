package Logica;

import Controlador.PersistenciaController;
import Modelo.Tb_ModeloVehiculo;
import Utilidades.ResultadoOperacion;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;


/**
 * Clase que contiene la lógica relacionada con la gestión de modelos de vehículos.
 * Proporciona métodos para crear, actualizar, eliminar y buscar modelos de vehículos.
 */
public class Logica_Modelo {

    private final PersistenciaController controladora = new PersistenciaController();

    /**
     * Obtiene una lista de modelos de vehículos según la marca y el tipo de vehículo.
     *
     * @param idMarcaVehiculo El ID de la marca del vehículo.
     * @param idTipoVehiculo  El ID del tipo de vehículo.
     * @return Una lista de objetos {@link Tb_ModeloVehiculo} que representan los modelos de la marca y tipo especificados.
     */
    public List<Tb_ModeloVehiculo> ObtenerModelosPorMarcaYTipo(int idMarcaVehiculo, int idTipoVehiculo) {
        try {
            return controladora.ObtenerModelosPorMarcaYTipo(idMarcaVehiculo, idTipoVehiculo);
        } catch (Exception e) {
            System.err.println("Error al obtener modelos por marca y tipo: " + e.getMessage());
        }
        return null;
    }

    /**
     * Crea un nuevo modelo de vehículo en la base de datos.
     *
     * @param modelo El objeto {@link Tb_ModeloVehiculo} que contiene los datos del nuevo modelo.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion crearModelo(Tb_ModeloVehiculo modelo) {
        try {
            controladora.CrearModelo(modelo);
            return new ResultadoOperacion(true, "El modelo ha sido creado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al crear el modelo de vehículo: " + e.getMessage());
            return new ResultadoOperacion(false, "Hubo un error al crear el modelo en la base de datos.");
        }
    }

    /**
     * Actualiza un modelo de vehículo existente en la base de datos.
     *
     * @param modelo El objeto {@link Tb_ModeloVehiculo} que contiene los datos actualizados del modelo.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion actualizarModelo(Tb_ModeloVehiculo modelo) {
        try {
            controladora.ActualizarModelo(modelo);
            return new ResultadoOperacion(true, "El modelo ha sido actualizado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al actualizar el modelo de vehículo: " + e.getMessage());
            return new ResultadoOperacion(false, "Hubo un error al actualizar el modelo en la base de datos.");
        }
    }

    /**
     * Busca un modelo de vehículo por su ID.
     *
     * @param idModelo El ID del modelo a buscar.
     * @return El objeto {@link Tb_ModeloVehiculo} correspondiente al ID proporcionado, o {@code null} si no se encuentra.
     */
    public Tb_ModeloVehiculo buscarModeloPorId(int idModelo) {
        try {
            return controladora.buscarModeloPorId(idModelo);
        } catch (Exception e) {
            System.err.println("Error al buscar el modelo de vehículo: " + e.getMessage());
            return null;
        }
    }

    /**
     * Elimina un modelo de vehículo en la base de datos por su ID.
     *
     * @param idModelo El ID del modelo que se desea eliminar.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion eliminarModelo(int idModelo) {
        try {
            controladora.eliminarModelo(idModelo);
            return new ResultadoOperacion(true, "El modelo ha sido eliminado correctamente.");
        } catch (PersistenceException e) {
            // Manejo de excepciones de restricciones de integridad
            Throwable cause = e.getCause();
            while (cause != null) {
                if (cause instanceof ConstraintViolationException) {
                    return new ResultadoOperacion(false, "No se puede eliminar el modelo porque está en uso.");
                }
                cause = cause.getCause();
            }
            return new ResultadoOperacion(false, "Error de persistencia al eliminar el modelo: " + e.getMessage());
        } catch (Exception e) {
            return new ResultadoOperacion(false, "Error inesperado al eliminar el modelo: " + e.getMessage());
        }
    }
}

