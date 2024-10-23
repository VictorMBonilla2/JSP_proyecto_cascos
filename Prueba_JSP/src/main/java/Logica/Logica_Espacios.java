package Logica;

import Controlador.PersistenciaController;
import Modelo.TbEspacio;
import Modelo.TbSectores;
import Modelo.enums.EstadoEspacio;
import Utilidades.EspacioServiceManager;

import java.util.List;


/**
 * Clase que contiene la lógica relacionada con la gestión de espacios en los sectores.
 * Proporciona métodos para crear, actualizar, eliminar, desactivar y reactivar espacios.
 */
public class Logica_Espacios {

    private final PersistenciaController controladora;

    /**
     * Constructor que inicializa la lógica con un controlador de persistencia.
     *
     * @param controladora El controlador de persistencia que interactúa con la base de datos.
     */
    public Logica_Espacios(PersistenciaController controladora) {
        this.controladora = controladora;
    }

    /**
     * Obtiene los datos de los espacios disponibles.
     *
     * @return Una lista de objetos {@link TbEspacio} que representan los espacios disponibles.
     */
    public List<TbEspacio> DatosEspacio() {
        return controladora.DatosEspaciosDisponibles();
    }

    /**
     * Obtiene una lista de espacios pertenecientes a un sector específico.
     *
     * @param idsector El ID del sector para el cual se desean obtener los espacios.
     * @return Una lista de objetos {@link TbEspacio} que representan los espacios del sector.
     */
    public List<TbEspacio> obtnerEspaciosPorSector(int idsector) {
        return controladora.ObtenerEspaciosPorSector(idsector);
    }

    /**
     * Busca un espacio específico por su ID.
     *
     * @param idEspacio El ID del espacio que se desea buscar.
     * @return El objeto {@link TbEspacio} correspondiente al ID proporcionado, o {@code null} si no se encuentra.
     */
    public TbEspacio buscarEspacio(Integer idEspacio) {
        return controladora.traerEspacio(idEspacio);
    }

    /**
     * Obtiene la cantidad de espacios para un sector específico.
     *
     * @param id El ID del sector.
     * @return El número total de espacios en el sector.
     */
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

    /**
     * Crea un nuevo espacio en la base de datos.
     *
     * @param espacio El objeto {@link TbEspacio} que contiene los datos del nuevo espacio.
     */
    public void crearEspacio(TbEspacio espacio) {
        controladora.CrearEspacio(espacio);
    }

    /**
     * Elimina un espacio de la base de datos.
     *
     * @param espacio El objeto {@link TbEspacio} que se desea eliminar.
     * @throws Exception Si ocurre un error durante la eliminación.
     */
    public void eliminarEspacio(TbEspacio espacio) throws Exception {
        controladora.eliminarEspacio(espacio.getId_espacio());
    }

    /**
     * Desactiva un espacio cambiando su estado a "Inactivo".
     *
     * @param espacio El espacio que se desea desactivar.
     */
    public void desactivarEspacio(TbEspacio espacio) {
        espacio.setEstado_espacio(EstadoEspacio.Inactivo);
        System.out.println("Se inserta el estado de inactivo.");
        actualizarEspacio(espacio);
    }

    /**
     * Reactiva un espacio cambiando su estado a "Libre".
     *
     * @param espacio El espacio que se desea reactivar.
     */
    public void reactivarEspacio(TbEspacio espacio) {
        espacio.setEstado_espacio(EstadoEspacio.Libre);
        System.out.println("Se inserta el estado de libre.");
        actualizarEspacio(espacio);
    }

    /**
     * Actualiza los datos de un espacio en la base de datos.
     *
     * @param espacio El espacio con los datos actualizados.
     * @return {@code true} si la actualización fue exitosa, {@code false} si ocurrió un error.
     */
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

