package Logica;

import Controlador.PersistenciaController;
import Modelo.TbTipoDocumento;
import Utilidades.ResultadoOperacion;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public class Logica_Documentos {

    PersistenciaController persistenciaController = new PersistenciaController();


    public List<TbTipoDocumento> obtenerDocumentos(){
        try{
            return persistenciaController.BuscarTiposDocumento();
        } catch (Exception e){
            System.out.println("Ha ocurrido un error: " + e);
            return null;
        }

    }

    public TbTipoDocumento obtenerDocumentoID(int idDocumento) {
        try {
            return persistenciaController.BuscarTipoDocumentoPorId(idDocumento);
        } catch (Exception e){
            System.out.println("Ha ocurrido un error: " + e);
            return null;
        }
    }

    public ResultadoOperacion crearDocumento(TbTipoDocumento documento) {
        try{

            boolean result = persistenciaController.crearTipoDocumento(documento);
            if (result){
                return new ResultadoOperacion(true, "Tipo de documento creado correctamente.");
            }else{
                return new ResultadoOperacion(false, "Error al crear el tipo de documento en la base de datos.");
            }
        }catch (Exception e){
            System.out.println("Error al crear el tipo de documento: " + e);
            return new ResultadoOperacion(false, "Error al crear el tipo de documento: " + e.getMessage());
        }
    }

    public ResultadoOperacion actualizarDocumento(TbTipoDocumento documento) {
        try {
            boolean result = persistenciaController.ActualizarTipoDocumento(documento);
            if(result){
                return new ResultadoOperacion(true, "Tipo de documento actualizado correctamente.");
            }else{
                return new ResultadoOperacion(false, "Error al actualizar el tipo de documento");
            }
        }catch (Exception e){
            System.out.println("Error al actualizar el tipo de documento: " + e);
            return new ResultadoOperacion(false, "Error inesperado al actualizar el tipo de documento: " + e.getMessage());
        }
    }

    public ResultadoOperacion eliminarTipoDocumento(int idDocumento) {
        try {
            boolean result = persistenciaController.eliminarTipoDocumento(idDocumento);
            if (result) {
                return new ResultadoOperacion(true, "Tipo de documento eliminado exitosamente.");
            } else {
                return new ResultadoOperacion(false, "No se pudo eliminar el tipo de documento.");
            }
        } catch (PersistenceException e) {
            // Verificamos si hay una excepción anidada de tipo ConstraintViolationException
            Throwable cause = e.getCause();
            while (cause != null) {
                if (cause instanceof ConstraintViolationException) {
                    return new ResultadoOperacion(false, "No se puede eliminar el tipo de documento porque está en uso.");
                }
                cause = cause.getCause(); // Navegar a través de la cadena de excepciones
            }
            return new ResultadoOperacion(false, "Error de persistencia al eliminar el tipo de documento: " + e.getMessage());
        } catch (Exception e) {
            return new ResultadoOperacion(false, "Error inesperado al eliminar el tipo de documento: " + e.getMessage());
        }
    }

}
