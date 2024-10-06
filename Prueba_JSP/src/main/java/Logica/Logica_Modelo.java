package Logica;

import Controlador.PersistenciaController;
import Modelo.Tb_ModeloVehiculo;
import Utilidades.ResultadoOperacion;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public class Logica_Modelo {
    PersistenciaController controladora = new PersistenciaController();
    public List<Tb_ModeloVehiculo> ObtenerModelosPorMarcaYTipo(int idMarcaVehiculo, int idTipoVehiculo) {
        try{
           return   controladora.ObtenerModelosPorMarcaYTipo(idMarcaVehiculo,idTipoVehiculo);
        }catch (Exception e){
            System.err.println("El documento proporcionado no es un número válido: " + e.getMessage());
            // Manejo adicional si es necesario, como lanzar una excepción personalizada o devolver una lista vacía.
        }

        return null;
    }
    public ResultadoOperacion crearModelo(Tb_ModeloVehiculo modelo) {
        try {
            controladora.CrearModelo(modelo);
            return new ResultadoOperacion(true, "El modelo ha sido creado correctamente");
        } catch (Exception e) {
            System.err.println("Error al crear el modelo de vehículo " + e.getMessage());
            return new ResultadoOperacion(false, "Hubo un error al crear el modelo en la base de datos");
        }
    }

    public ResultadoOperacion actualizarModelo(Tb_ModeloVehiculo modelo) {
        try {
            controladora.ActualizarModelo(modelo);
            return new ResultadoOperacion(true, "El modelo ha sido actualizado correctamente");
        } catch (Exception e) {
            System.err.println("Error al actualizar el modelo de vehículo " + e.getMessage());
            return new ResultadoOperacion(false, "Hubo un error al actualizar el modelo en la base de datos");
        }
    }

    public Tb_ModeloVehiculo buscarModeloPorId(int idModelo) {
        try {
            return controladora.buscarModeloPorId(idModelo);
        } catch (Exception e) {
            System.err.println("Error al buscar el modelo de vehículo " + e.getMessage());
            return null;
        }
    }

    public ResultadoOperacion eliminarModelo(int idModelo) {
        try {
            controladora.eliminarModelo(idModelo);  // La excepción se propagará si ocurre un error en la persistencia
            return new ResultadoOperacion(true, "El modelo ha sido eliminado correctamente.");
        } catch (PersistenceException e) {
            // Verificamos si hay una excepción anidada de tipo ConstraintViolationException
            Throwable cause = e.getCause();
            while (cause != null) {
                if (cause instanceof ConstraintViolationException) {
                    return new ResultadoOperacion(false, "No se puede eliminar el modelo porque está en uso.");
                }
                cause = cause.getCause(); // Navegar a través de la cadena de excepciones
            }
            return new ResultadoOperacion(false, "Error de persistencia al eliminar el modelo: " + e.getMessage());
        } catch (Exception e) {
            return new ResultadoOperacion(false, "Error inesperado al eliminar el modelo: " + e.getMessage());
        }
    }


}
