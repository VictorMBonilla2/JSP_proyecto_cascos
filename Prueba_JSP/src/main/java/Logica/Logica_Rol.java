package Logica;

import Controlador.PersistenciaController;
import Modelo.Roles;
import Utilidades.ResultadoOperacion;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public class Logica_Rol {
    PersistenciaController controladora = new PersistenciaController();

    public Roles ObtenerRol(int rol) {
        Roles roles = null;
        try{
            roles = controladora.ObtenerRol(rol);
        } catch (Exception e){
            System.err.println("Error al obtener rol " + e.getMessage());
            e.printStackTrace();
            return roles;
        }
        return roles;
    }

    public List<Roles> ObtenerRoles(){
        List<Roles> roles= null;
        try{
            roles = controladora.obtenerRoles();
        } catch (Exception e){
            System.err.println("Error al obtener roles "+ e.getMessage());
            return roles;
        }
        return roles;
    }

    public ResultadoOperacion crearRol(Roles roles) {
        try {
            boolean result = controladora.crearRol(roles);
            if(result){
                return new ResultadoOperacion(true,"El rol ha sido creado correctamente");
            }else {
                return new ResultadoOperacion(false, "Error al crear el rol en la base de datos");
            }
        }catch (Exception e){
            System.out.println("Error al crear el rol: " + e);
            return new ResultadoOperacion(false, "Error al crear el rol: " + e.getMessage());
        }

    }

    public ResultadoOperacion actualizarRol(Roles roles) {
        try {
            boolean result = controladora.ActualizarRol(roles);
            if(result){
                return new ResultadoOperacion(true, "Rol actualizado correctamente.");
            }else{
                return new ResultadoOperacion(false, "Error al actualizar el rol");
            }
        } catch (Exception e){
            System.out.println("Error al actualizar el rol: " + e);
            return new ResultadoOperacion(false, "Error inesperado al actualizar el rol: " + e.getMessage());
        }
    }

    public ResultadoOperacion eliminarRol(int idRol) {
        try {
            boolean result = controladora.eliminarRol(idRol);
            if (result) {
                return new ResultadoOperacion(true, "Rol eliminado exitosamente.");
            } else {
                return new ResultadoOperacion(false, "No se pudo eliminar el rol.");
            }
        } catch (PersistenceException e) {
            // Verificamos si hay una excepción anidada de tipo ConstraintViolationException
            Throwable cause = e.getCause();
            while (cause != null) {
                if (cause instanceof ConstraintViolationException) {
                    return new ResultadoOperacion(false, "No se puede eliminar el rol porque está en uso.");
                }
                cause = cause.getCause(); // Navegar a través de la cadena de excepciones
            }
            return new ResultadoOperacion(false, "Error de persistencia al eliminar el rol: " + e.getMessage());
        } catch (Exception e) {
            return new ResultadoOperacion(false, "Error inesperado al eliminar el rol: " + e.getMessage());
        }
    }

}
