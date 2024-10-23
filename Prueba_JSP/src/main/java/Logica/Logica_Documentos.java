package Logica;

import Controlador.PersistenciaController;
import Modelo.TbTipoDocumento;
import Utilidades.ResultadoOperacion;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;



/**
 * Clase que contiene la lógica relacionada con la gestión de tipos de documentos.
 * Proporciona métodos para crear, actualizar, eliminar y buscar tipos de documentos.
 */
public class Logica_Documentos {

    private final PersistenciaController persistenciaController = new PersistenciaController();

    /**
     * Obtiene una lista de todos los tipos de documentos.
     *
     * @return Una lista de objetos {@link TbTipoDocumento} que representan los tipos de documentos.
     */
    public List<TbTipoDocumento> obtenerDocumentos() {
        try {
            return persistenciaController.BuscarTiposDocumento();
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error: " + e);
            return null;
        }
    }

    /**
     * Busca un tipo de documento específico por su ID.
     *
     * @param idDocumento El ID del tipo de documento a buscar.
     * @return El objeto {@link TbTipoDocumento} correspondiente al ID proporcionado, o {@code null} si no se encuentra.
     */
    public TbTipoDocumento obtenerDocumentoID(int idDocumento) {
        try {
            return persistenciaController.BuscarTipoDocumentoPorId(idDocumento);
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error: " + e);
            return null;
        }
    }

    /**
     * Crea un nuevo tipo de documento en la base de datos.
     *
     * @param documento El objeto {@link TbTipoDocumento} que contiene los datos del nuevo tipo de documento.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion crearDocumento(TbTipoDocumento documento) {
        try {
            boolean result = persistenciaController.crearTipoDocumento(documento);
            if (result) {
                return new ResultadoOperacion(true, "Tipo de documento creado correctamente.");
            } else {
                return new ResultadoOperacion(false, "Error al crear el tipo de documento en la base de datos.");
            }
        } catch (Exception e) {
            System.out.println("Error al crear el tipo de documento: " + e);
            return new ResultadoOperacion(false, "Error al crear el tipo de documento: " + e.getMessage());
        }
    }

    /**
     * Actualiza un tipo de documento existente en la base de datos.
     *
     * @param documento El objeto {@link TbTipoDocumento} que contiene los datos actualizados del tipo de documento.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion actualizarDocumento(TbTipoDocumento documento) {
        try {
            boolean result = persistenciaController.ActualizarTipoDocumento(documento);
            if (result) {
                return new ResultadoOperacion(true, "Tipo de documento actualizado correctamente.");
            } else {
                return new ResultadoOperacion(false, "Error al actualizar el tipo de documento.");
            }
        } catch (Exception e) {
            System.out.println("Error al actualizar el tipo de documento: " + e);
            return new ResultadoOperacion(false, "Error inesperado al actualizar el tipo de documento: " + e.getMessage());
        }
    }

    /**
     * Elimina un tipo de documento en la base de datos por su ID.
     *
     * @param idDocumento El ID del tipo de documento que se desea eliminar.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion eliminarTipoDocumento(int idDocumento) {
        try {
            boolean result = persistenciaController.eliminarTipoDocumento(idDocumento);
            if (result) {
                return new ResultadoOperacion(true, "Tipo de documento eliminado exitosamente.");
            } else {
                return new ResultadoOperacion(false, "No se pudo eliminar el tipo de documento.");
            }
        } catch (PersistenceException e) {
            // Manejo de excepciones de restricciones de integridad
            Throwable cause = e.getCause();
            while (cause != null) {
                if (cause instanceof ConstraintViolationException) {
                    return new ResultadoOperacion(false, "No se puede eliminar el tipo de documento porque está en uso.");
                }
                cause = cause.getCause();
            }
            return new ResultadoOperacion(false, "Error de persistencia al eliminar el tipo de documento: " + e.getMessage());
        } catch (Exception e) {
            return new ResultadoOperacion(false, "Error inesperado al eliminar el tipo de documento: " + e.getMessage());
        }
    }
}
