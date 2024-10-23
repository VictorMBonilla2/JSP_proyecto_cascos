package Logica;

import Controlador.PersistenciaController;
import Modelo.Roles;
import Utilidades.ResultadoOperacion;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;


/**
 * Clase que contiene la lógica relacionada con la gestión de roles.
 * Proporciona métodos para crear, actualizar, eliminar y obtener roles.
 */
public class Logica_Rol {

    private final PersistenciaController controladora = new PersistenciaController();

    /**
     * Obtiene un rol específico en la base de datos por su ID.
     *
     * @param rol El ID del rol a obtener.
     * @return El objeto {@link Roles} correspondiente al rol solicitado, o {@code null} si ocurre un error.
     */
    public Roles ObtenerRol(int rol) {
        Roles roles = null;
        try {
            roles = controladora.ObtenerRol(rol);
        } catch (Exception e) {
            System.err.println("Error al obtener rol: " + e.getMessage());
            e.printStackTrace();
            return roles;
        }
        return roles;
    }

    /**
     * Obtiene todos los roles registrados en la base de datos.
     *
     * @return Una lista de objetos {@link Roles} que representan los roles registrados, o {@code null} si ocurre un error.
     */
    public List<Roles> ObtenerRoles() {
        List<Roles> roles = null;
        try {
            roles = controladora.obtenerRoles();
        } catch (Exception e) {
            System.err.println("Error al obtener roles: " + e.getMessage());
            return roles;
        }
        return roles;
    }

    /**
     * Crea un nuevo rol en la base de datos.
     *
     * @param roles El objeto {@link Roles} que contiene los datos del nuevo rol.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion crearRol(Roles roles) {
        try {
            boolean result = controladora.crearRol(roles);
            if (result) {
                return new ResultadoOperacion(true, "El rol ha sido creado correctamente.");
            } else {
                return new ResultadoOperacion(false, "Error al crear el rol en la base de datos.");
            }
        } catch (Exception e) {
            System.out.println("Error al crear el rol: " + e);
            return new ResultadoOperacion(false, "Error al crear el rol: " + e.getMessage());
        }
    }

    /**
     * Actualiza un rol existente en la base de datos.
     *
     * @param roles El objeto {@link Roles} que contiene los datos actualizados del rol.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion actualizarRol(Roles roles) {
        try {
            boolean result = controladora.ActualizarRol(roles);
            if (result) {
                return new ResultadoOperacion(true, "Rol actualizado correctamente.");
            } else {
                return new ResultadoOperacion(false, "Error al actualizar el rol.");
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar el rol: " + e);
            return new ResultadoOperacion(false, "Error inesperado al actualizar el rol: " + e.getMessage());
        }
    }

    /**
     * Elimina un rol en la base de datos por su ID, manejando errores de restricciones de integridad si el rol está en uso.
     *
     * @param idRol El ID del rol que se desea eliminar.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion eliminarRol(int idRol) {
        try {
            boolean result = controladora.eliminarRol(idRol);
            if (result) {
                return new ResultadoOperacion(true, "Rol eliminado exitosamente.");
            } else {
                return new ResultadoOperacion(false, "No se pudo eliminar el rol.");
            }
        } catch (PersistenceException e) {
            // Manejo de excepciones de restricciones de integridad
            Throwable cause = e.getCause();
            while (cause != null) {
                if (cause instanceof ConstraintViolationException) {
                    return new ResultadoOperacion(false, "No se puede eliminar el rol porque está en uso.");
                }
                cause = cause.getCause();
            }
            return new ResultadoOperacion(false, "Error de persistencia al eliminar el rol: " + e.getMessage());
        } catch (Exception e) {
            return new ResultadoOperacion(false, "Error inesperado al eliminar el rol: " + e.getMessage());
        }
    }
}
