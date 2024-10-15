package Logica;

import Controlador.PersistenciaController;
import Modelo.Persona;
import Modelo.TbReportes;

import java.util.List;

public class Logica_Reportes {
    PersistenciaController controladora = new PersistenciaController();
    Logica_Persona logicaPersona = new Logica_Persona();

    public List<TbReportes> ObtenerReportes(int idUsuario) {
        List<TbReportes> lista = null;
        try {
            Persona persona = logicaPersona.buscarpersonaPorId(idUsuario);
            if (persona == null) {
                return null;  // Si la persona no existe, devolver null
            }

            int rol = persona.getRol().getId();

            if (rol == 1) { // Gestor
                lista = controladora.ObtenerReportesGestor(persona.getId());  // Búsqueda por ID del gestor
            } else if (rol == 2) { // Aprendiz
                lista = controladora.ObtenerReportesAprendiz(persona.getId());  // Búsqueda por ID del aprendiz
            } else if (rol == 3) { // Administrador u otro rol
                lista = controladora.ObtenerReportes();  // Obtener todos los reportes
            }

        } catch (Exception e) {
            System.err.println("Hubo un error al obtener la lista de reportes correspondiente a este usuario: " + e.getMessage());
            e.getStackTrace();
            return null;
        }

        return lista;
    }


    public void CrearReporte(TbReportes nuevoReporte) {
    try{
        System.out.println("Intento de crear Reporte");
        controladora.CrearReporte(nuevoReporte);
    }catch (Exception e){
        System.err.println("Hubo un error al crear el nuevo reporte: " +e.getMessage());
        e.getStackTrace();
    }

    }
}
