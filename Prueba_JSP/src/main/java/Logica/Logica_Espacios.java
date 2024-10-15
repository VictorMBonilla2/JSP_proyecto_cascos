package Logica;

import Controlador.PersistenciaController;
import Modelo.TbEspacio;
import Modelo.TbSectores;
import Modelo.enums.EstadoEspacio;
import Utilidades.EspacioServiceManager;

import java.util.List;

public class Logica_Espacios {
    private PersistenciaController controladora;

    public Logica_Espacios(PersistenciaController controladora) {
        this.controladora = controladora;
    }

    public List<TbEspacio> DatosEspacio() {
        return controladora.DatosEspaciosDisponibles();
    }
    public List<TbEspacio> obtnerEspaciosPorSector (int idsector){
        return controladora.ObtenerEspaciosPorSector(idsector);
    }


    public TbEspacio buscarEspacio(Integer idEspacio) {
        return controladora.traerEspacio(idEspacio);
    }
    public Integer EspaciosPorSector(int id) {
        Logica_Sectores logicaSectores = EspacioServiceManager.getInstance().getLogicaSectores();
        List<TbSectores> casilleros = logicaSectores.ObtenerSectores();
        int espacios = 0;

        for (TbSectores c : casilleros) {
            if (c.getId().equals(id)) {
                espacios = c.getCant_espacio();
                break;
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
    public void desactivarEspacio(TbEspacio espacio){
        espacio.setEstado_espacio(EstadoEspacio.Inactivo);
        System.out.println("Se inserta el estado de inactivo");
        actualizarEspacio(espacio);
    }

    public void reactivarEspacio(TbEspacio espacio){
        espacio.setEstado_espacio(EstadoEspacio.Libre);
        System.out.println("Se inserta el estado de inactivo");
        actualizarEspacio(espacio);
    }

    public boolean actualizarEspacio(TbEspacio espacio) {
        try {
            controladora.ActualizarEspacio(espacio);
            return true;
        } catch (Exception e) {
            System.out.println("Error al actualizar el espacio con ID " + espacio.getId_espacio() + ": " + e);
            return false;
        }
    }


}
