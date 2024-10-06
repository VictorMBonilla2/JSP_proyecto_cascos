package Logica;

import Controlador.PersistenciaController;
import Modelo.TbTipovehiculo;
import Utilidades.ResultadoOperacion;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public class Logica_TipoVehiculo {
    PersistenciaController controladora = new PersistenciaController();

    public TbTipovehiculo buscarTipoVehiculo(int tipoVehiculo) {
        return controladora.obtenerTipoVehiculoPorId(tipoVehiculo);
    }

    public List<TbTipovehiculo> ObtenerTiposVehiculo() {
        return controladora.BuscarTiposVehiculo();
    }

    // Método para crear un nuevo tipo de vehículo
    public ResultadoOperacion crearTipoVehiculo(TbTipovehiculo tipoVehiculo) {
        try {
            // Lógica para crear el tipo de vehículo en la base de datos
            controladora.crearTipoVehiculo(tipoVehiculo);
            return new ResultadoOperacion(true, "El tipo de vehículo ha sido creado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al crear el tipo de vehículo: " + e.getMessage());
            return new ResultadoOperacion(false, "Hubo un error al crear el tipo de vehículo en la base de datos.");
        }
    }

    // Método para actualizar un tipo de vehículo existente
    public ResultadoOperacion actualizarTipoVehiculo(TbTipovehiculo tipoVehiculo) {
        try {
            // Lógica para actualizar el tipo de vehículo en la base de datos
            controladora.actualizarTipoVehiculo(tipoVehiculo);
            return new ResultadoOperacion(true, "El tipo de vehículo ha sido actualizado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al actualizar el tipo de vehículo: " + e.getMessage());
            return new ResultadoOperacion(false, "Hubo un error al actualizar el tipo de vehículo en la base de datos.");
        }
    }

    // Método para eliminar un tipo de vehículo basado en su ID
    public ResultadoOperacion eliminarTipoVehiculo(int idTipo) {
        try {
            controladora.eliminarTipoVehiculo(idTipo);  // La excepción se propagará si ocurre un error en la persistencia
            return new ResultadoOperacion(true, "El tipo de vehículo ha sido eliminado correctamente.");
        } catch (PersistenceException e) {
            // Verificamos si hay una excepción anidada de tipo ConstraintViolationException
            Throwable cause = e.getCause();
            while (cause != null) {
                if (cause instanceof ConstraintViolationException) {
                    return new ResultadoOperacion(false, "No se puede eliminar el tipo de vehículo porque está en uso.");
                }
                cause = cause.getCause(); // Navegar a través de la cadena de excepciones
            }
            return new ResultadoOperacion(false, "Error de persistencia al eliminar el tipo de vehículo: " + e.getMessage());
        } catch (Exception e) {
            return new ResultadoOperacion(false, "Error inesperado al eliminar el tipo de vehículo: " + e.getMessage());
        }
    }



}
