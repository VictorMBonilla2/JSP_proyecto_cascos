package Logica;

import Controlador.PersistenciaController;
import Modelo.Tb_CiudadVehiculo;
import Utilidades.ResultadoOperacion;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public class Logica_Ciudad {
    PersistenciaController controladora = new PersistenciaController();


    public List<Tb_CiudadVehiculo> obtenerCiudades(){
        try{
            return controladora.ObtenerCiudades();
        } catch (Exception e){
            System.err.println("El documento proporcionado no es un número válido: " + e.getMessage());
            return null;
            // Manejo adicional si es necesario, como lanzar una excepción personalizada o devolver una lista vacía.
        }
    }

    public ResultadoOperacion actualizarCiudad(Tb_CiudadVehiculo ciudad) {
        try {
            controladora.ActualizarCiudad(ciudad);
            return new ResultadoOperacion(true, "La ciudad ha sido actualizada correctamente");
        } catch (Exception e) {
            System.err.println("Error al actualizar la ciudad " + e.getMessage());
            return new ResultadoOperacion(false, "Hubo un error al actualizar la ciudad en la base de datos");
        }
    }

    public ResultadoOperacion eliminarCiudad(int idCiudad) {
        try {
            controladora.EliminarCiudad(idCiudad);  // La excepción se propagará si ocurre un error en la persistencia
            return new ResultadoOperacion(true, "La ciudad ha sido eliminada correctamente.");
        } catch (PersistenceException e) {
            // Verificamos si hay una excepción anidada de tipo ConstraintViolationException
            Throwable cause = e.getCause();
            while (cause != null) {
                if (cause instanceof ConstraintViolationException) {
                    return new ResultadoOperacion(false, "No se puede eliminar la ciudad porque está en uso.");
                }
                cause = cause.getCause(); // Navegar a través de la cadena de excepciones
            }
            return new ResultadoOperacion(false, "Error de persistencia al eliminar la ciudad: " + e.getMessage());
        } catch (Exception e) {
            return new ResultadoOperacion(false, "Error inesperado al eliminar la ciudad: " + e.getMessage());
        }
    }


    public ResultadoOperacion crearCiudad(Tb_CiudadVehiculo ciudad) {
        try {
            controladora.CrearCiudad(ciudad);
            return new ResultadoOperacion(true, "La ciudad ha sido creada correctamente");
        } catch (Exception e) {
            System.err.println("Error al crear la ciudad " + e.getMessage());
            return new ResultadoOperacion(false, "Hubo un error al crear la ciudad en la base de datos");
        }
    }

    public Tb_CiudadVehiculo buscarCiudadPorId(int ciudadId) {
        try {
            return controladora.BuscarCiudad(ciudadId);
        } catch (Exception e) {
            System.err.println("Error al buscar la ciudad: " + e.getMessage());
            return null;
        }
    }

}
