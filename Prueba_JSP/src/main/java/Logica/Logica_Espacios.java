package Logica;

import Controlador.PersistenciaController;
import Modelo.TbEspacio;
import Modelo.TbSectores;

import java.util.List;

public class Logica_Espacios {
    PersistenciaController controladora = new PersistenciaController();
    Logica_Sectores logica = new Logica_Sectores();

    public List<TbEspacio> DatosEspacio() {
        return controladora.DatosEspacios();
    }
    public Integer EspaciosPorSector(int id) {
        List<TbSectores> casilleros= logica.ObtenerSectores();
        int espacios = 0;

        for (TbSectores c : casilleros) {
            if (c.getId().equals(id)) { // Verificar si el ID del casillero coincide con el ID específico
                espacios = c.getCant_espacio(); // Obtener la cantidad de espacios del casillero
                break; // Salir del bucle una vez que se encuentre el casillero con el ID específico
            }
        }
        return espacios;
    }

    public void crearEspacio(TbEspacio espacio) {
        controladora.CrearEspacio(espacio);
    }

    public void eliminarEspacio(TbEspacio espacio) throws Exception {

        controladora.eliminarEspacio(espacio.getId_espacio());
    }

    public TbEspacio buscarEspacio(Integer idEspacio) {

        return controladora.traerEspacio(idEspacio);
    }
    public boolean actualizarEspacio(TbEspacio espacio) {

        try {
            controladora.ActualizarEspacio(espacio);
            return true;
        } catch (Exception e) {
            /*log(Level.SEVERE, "Error al actualizar el espacio con ID " + espacio.getId_espacio(), e);*/
            return false;
        }
    }
}
