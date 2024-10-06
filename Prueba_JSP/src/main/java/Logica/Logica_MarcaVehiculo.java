package Logica;

import Controlador.PersistenciaController;
import Modelo.TbTipovehiculo;
import Modelo.Tb_MarcaVehiculo;
import Utilidades.ResultadoOperacion;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public class Logica_MarcaVehiculo {
    PersistenciaController controladora = new PersistenciaController();
    public List<Tb_MarcaVehiculo> ObtenerMarcasPorTipo(int idTipoVehiculo) {
        try {
            return controladora.buscarMarcasPorTipo(idTipoVehiculo);
        }catch (Exception e){
            System.err.println("El documento proporcionado no es un número válido: " + e.getMessage());
            // Manejo adicional si es necesario, como lanzar una excepción personalizada o devolver una lista vacía.
        }

        return null;
    }

    public ResultadoOperacion crearMarcaVehiculo(Tb_MarcaVehiculo marca) {
        try{
            controladora.CrearMarca(marca);
            return new ResultadoOperacion(true, "La marca ha sido creada correctamente");
        }catch (Exception e) {
            System.err.println("Error al crear al vehiculo " + e.getMessage());
            return new ResultadoOperacion(false,"Hubo un error al crear la marca en la base de datos");
        }

    }

    public ResultadoOperacion actualizarMarcaVehiculo(Tb_MarcaVehiculo marca) {
        try{
            controladora.ActualizarMarca(marca);
            return new ResultadoOperacion(true, "La marca ha sido actualizada correctamente");
        }catch (Exception e) {
            System.err.println("Error al crear al vehiculo " + e.getMessage());
            return new ResultadoOperacion(false,"Hubo un error al actualizadar la marca en la base de datos");
        }

    }

    public ResultadoOperacion eliminarMarcaVehiculo(int idMarca) {
        try {
            controladora.eliminarMarca(idMarca);  // La excepción se propagará desde la capa de persistencia si ocurre un error
            return new ResultadoOperacion(true, "La marca ha sido eliminada correctamente.");
        } catch (PersistenceException e) {
            // Verificamos si hay una excepción anidada de tipo ConstraintViolationException
            Throwable cause = e.getCause();
            while (cause != null) {
                if (cause instanceof    ConstraintViolationException) {
                    return new ResultadoOperacion(false, "No se puede eliminar la marca porque está en uso.");
                }
                cause = cause.getCause(); // Navegar a través de la cadena de excepciones
            }
            return new ResultadoOperacion(false, "Error de persistencia al eliminar la marca: " + e.getMessage());
        } catch (Exception e) {
            return new ResultadoOperacion(false, "Error inesperado al eliminar la marca: " + e.getMessage());
        }
    }



    public Tb_MarcaVehiculo buscarMarcaPorId(int idMarcaVehiculo) {
        try{
            return controladora.buscarMarcasPorId(idMarcaVehiculo);
        }catch (Exception e) {
            System.err.println("Error al crear al vehiculo " + e.getMessage());
            return null;
        }
    }
}
