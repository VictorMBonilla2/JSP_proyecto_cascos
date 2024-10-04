package Logica;

import Controlador.PersistenciaController;
import Modelo.Persona;
import Modelo.TbRegistro;
import Modelo.TbReportes;

import java.util.List;

public class Logica_Reportes {
    PersistenciaController controladora = new PersistenciaController();
    Logica_Persona logicaPersona = new Logica_Persona();
    public List<TbReportes> ObtenerReportes(int idUsuario) {
        List<TbReportes> lista=null;
        try{
            Persona persona = logicaPersona.buscarpersonaPorId(idUsuario);
            if(persona == null){
                return null;
            }
            int rol = persona.getRol().getId();
            if(rol == 1){ //Gestor
                lista= controladora.ObtenerReportesGestor(persona.getDocumento());
            }
            if(rol== 2){ //Aprendiz
                lista= controladora.ObtenerReportesAprendiz(persona.getDocumento());
            }
            if(rol == 3 ){
                lista= controladora.ObtenerReportes();
            }
        }catch (Exception e){
            System.err.println("Hubo un error al obtener la lista de registro correspondiente a este usuario: " + e.getMessage());
            return null;
        }
        return lista;
    }

    public void CrearReporte(TbReportes nuevoReporte) {

        controladora.CrearReporte(nuevoReporte);
    }
}
