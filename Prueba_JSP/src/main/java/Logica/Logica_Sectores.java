package Logica;

import Controlador.PersistenciaController;
import Modelo.TbSectores;
import Utilidades.CasilleroServices;

import java.util.List;

public class Logica_Sectores {
    PersistenciaController controladora = new PersistenciaController();
    CasilleroServices casilleroServices = new CasilleroServices();


    public List<TbSectores> ObtenerSectores() {
        return controladora.ObtenerSectores();
    }
    public boolean crearSector(TbSectores sector) {
        try{
            boolean result =  controladora.CrearSector(sector);
            if (result){
                int idSector= sector.getId();
                TbSectores sectors = ConseguirSector(idSector);
                casilleroServices.crearEspaciosParaSector(sectors);
            }
            return true;
        } catch (Exception e){
            System.out.println("Error al Crear el secotr= "+ e);
            return false;
        }
    }


    public TbSectores ConseguirSector(int sectorId) {

        return controladora.TraerCasillero(sectorId);

    }

    public boolean actualizarSector(TbSectores sector) {
        try{
            boolean result= controladora.ActualizarSector(sector);
            if (result){
                int idSector= sector.getId();
                TbSectores sectors = ConseguirSector(idSector);
                casilleroServices.crearEspaciosParaSector(sectors);
            }
            return true;

        } catch (Exception e){
            System.out.println("Error al Crear el secotr= "+ e);
            return false;
        }
    }
}
