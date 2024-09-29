package Logica;

import Controlador.PersistenciaController;
import Modelo.TbTipoDocumento;

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
}
