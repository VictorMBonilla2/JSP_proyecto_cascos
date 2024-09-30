package Logica;

import Controlador.PersistenciaController;
import Modelo.TbTipoDocumento;
import Utilidades.ResultadoOperacion;

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
        try{
            boolean result = persistenciaController.eliminarTipoDocumento(idDocumento);
            if (result) {
                return new ResultadoOperacion(true, "Tipo de documento eliminado exitosamente."); // Retornar true si se eliminó con éxito
            } else {
                return new ResultadoOperacion(false, "No se pudo eliminar el tipo de documento ."); // Retornar false si no se pudo eliminar
            }

        }catch (Exception e){
            return new ResultadoOperacion(false, "Error al eliminar el tipo de documento: " + e.getMessage());
        }
    }
}
