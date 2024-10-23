package Utilidades;

import Controlador.PersistenciaController;
import Logica.Logica_Espacios;
import Logica.Logica_Sectores;
import Servicios.CasilleroServices;

/**
 * Singleton que gestiona los servicios de persistencia y lógica relacionados
 * con espacios y sectores.
 * Proporciona acceso a controladores de lógica y servicios específicos.
 */
public class EspacioServiceManager {
    // Instancia única del Singleton
    private static EspacioServiceManager instance;

    // Controlador de persistencia
    private PersistenciaController persistenciaController;

    // Controladores de lógica y servicios
    private Logica_Sectores logicaSectores;
    private Logica_Espacios logicaEspacios;
    private CasilleroServices casilleroServices;

    /**
     * Constructor privado para evitar la inicialización directa de la clase.
     * Inicializa el controlador de persistencia.
     */
    private EspacioServiceManager() {
        this.persistenciaController = new PersistenciaController();
    }

    /**
     * Obtiene la instancia única de EspacioServiceManager.
     * Si la instancia no existe, la crea.
     *
     * @return Instancia única de EspacioServiceManager.
     */
    public static EspacioServiceManager getInstance() {
        if (instance == null) {
            instance = new EspacioServiceManager();
        }
        return instance;
    }

    /**
     * Obtiene el controlador de persistencia utilizado por el gestor de servicios.
     *
     * @return Instancia de PersistenciaController.
     */
    public PersistenciaController getPersistenciaController() {
        return persistenciaController;
    }

    /**
     * Obtiene la lógica relacionada con los sectores.
     * Se inicializa de forma perezosa si no ha sido instanciada previamente.
     *
     * @return Instancia de Logica_Sectores.
     */
    public Logica_Sectores getLogicaSectores() {
        if (logicaSectores == null) {
            logicaSectores = new Logica_Sectores(persistenciaController, getCasilleroServices());
        }
        return logicaSectores;
    }

    /**
     * Obtiene la lógica relacionada con los espacios.
     * Se inicializa de forma perezosa si no ha sido instanciada previamente.
     *
     * @return Instancia de Logica_Espacios.
     */
    public Logica_Espacios getLogicaEspacios() {
        if (logicaEspacios == null) {
            logicaEspacios = new Logica_Espacios(persistenciaController);
        }
        return logicaEspacios;
    }

    /**
     * Obtiene los servicios de casilleros.
     * Se inicializa de forma perezosa si no ha sido instanciado previamente.
     *
     * @return Instancia de CasilleroServices.
     */
    public CasilleroServices getCasilleroServices() {
        if (casilleroServices == null) {
            casilleroServices = new CasilleroServices(getLogicaEspacios());
        }
        return casilleroServices;
    }
}


