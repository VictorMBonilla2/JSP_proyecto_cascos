package Logica;

import Controlador.PersistenciaController;
import Modelo.TbReportes;

import java.util.List;

public class Logica_Reportes {
    PersistenciaController controladora = new PersistenciaController();
    Logica_Persona persona = new Logica_Persona();
    public List<TbReportes> ObtenerReportes() {
        List<TbReportes> reportes = controladora.ObtenerReportes();

        for (TbReportes reporte : reportes) {
            int numeroAPrendiz= reporte.getAprendiz().getDocumento();
            int numeroGestor=reporte.getColaborador().getDocumento();
            reporte.setAprendiz(persona.buscarpersona(numeroAPrendiz)); ;
            reporte.setColaborador(persona.buscarpersona(numeroGestor));
        }

        return reportes;
    }

    public void CrearReporte(TbReportes nuevoReporte) {

        controladora.CrearReporte(nuevoReporte);
    }
}
