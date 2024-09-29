package Logica;

import Controlador.PersistenciaController;
import Modelo.TbReportes;

import java.util.List;

public class Logica_Reportes {
    PersistenciaController controladora = new PersistenciaController();
    Logica_Persona persona = new Logica_Persona();
    public List<TbReportes> ObtenerReportes() {

        return controladora.ObtenerReportes();
    }

    public void CrearReporte(TbReportes nuevoReporte) {

        controladora.CrearReporte(nuevoReporte);
    }
}
