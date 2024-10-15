package Utilidades;

import Controlador.PersistenciaController;
import Logica.Logica_Espacios;
import Logica.Logica_Sectores;
import Servicios.CasilleroServices;

public class EspacioServiceManager {
    private static EspacioServiceManager instance;

    private PersistenciaController persistenciaController;
    private Logica_Sectores logicaSectores;
    private Logica_Espacios logicaEspacios;
    private CasilleroServices casilleroServices;

    // Constructor privado para evitar inicialización directa
    private EspacioServiceManager() {
        this.persistenciaController = new PersistenciaController();
    }

    // Método para obtener la instancia única de EspacioServiceManager
    public static EspacioServiceManager getInstance() {
        if (instance == null) {
            instance = new EspacioServiceManager();
        }
        return instance;
    }

    public PersistenciaController getPersistenciaController() {
        return persistenciaController;
    }

    // Inicialización perezosa para Logica_Sectores
    public Logica_Sectores getLogicaSectores() {
        if (logicaSectores == null) {
            logicaSectores = new Logica_Sectores(persistenciaController, getCasilleroServices());
        }
        return logicaSectores;
    }

    // Inicialización perezosa para Logica_Espacios
    public Logica_Espacios getLogicaEspacios() {
        if (logicaEspacios == null) {
            logicaEspacios = new Logica_Espacios(persistenciaController);
        }
        return logicaEspacios;
    }

    // Inicialización perezosa para CasilleroServices
    public CasilleroServices getCasilleroServices() {
        if (casilleroServices == null) {
            casilleroServices = new CasilleroServices(getLogicaEspacios());
        }
        return casilleroServices;
    }
}

