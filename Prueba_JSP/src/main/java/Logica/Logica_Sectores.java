package Logica;

import Controlador.PersistenciaController;
import Modelo.TbSectores;
import Modelo.CasilleroServices;

import java.util.List;

public class Logica_Sectores {
    private PersistenciaController controladora;
    private CasilleroServices casilleroServices;

    public Logica_Sectores(PersistenciaController controladora, CasilleroServices casilleroServices) {
        this.controladora = controladora;
        this.casilleroServices = casilleroServices;
    }

    public List<TbSectores> ObtenerSectores() {
        return controladora.ObtenerSectores();
    }

    public boolean crearSector(TbSectores sector) {
        try {
            boolean result = controladora.CrearSector(sector);
            if (result) {
                TbSectores sectors = ConseguirSector(sector.getId());
                casilleroServices.crearEspaciosParaSector(sectors);
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error al crear el sector: " + e);
            return false;
        }
    }

    public TbSectores ConseguirSector(int sectorId) {
        return controladora.TraerCasillero(sectorId);
    }

    public boolean actualizarSector(TbSectores sector) {
        try {
            boolean result = controladora.ActualizarSector(sector);
            if (result) {
                TbSectores sectors = ConseguirSector(sector.getId());
                casilleroServices.crearEspaciosParaSector(sectors);
            }
            return true;
        } catch (Exception e) {
            System.out.println("Error al actualizar el sector: " + e);
            return false;
        }
    }
}
