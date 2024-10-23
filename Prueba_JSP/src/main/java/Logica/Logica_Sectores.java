package Logica;

import Controlador.PersistenciaController;
import Servicios.CasilleroServices;
import Modelo.TbSectores;
import Modelo.enums.EstadoEspacio;
import Utilidades.ResultadoOperacion;

import java.util.List;


/**
 * Clase que contiene la lógica relacionada con la gestión de sectores.
 * Proporciona métodos para crear, actualizar, eliminar y obtener sectores, así como gestionar sus espacios.
 */
public class Logica_Sectores {

    private final PersistenciaController controladora;
    private final CasilleroServices casilleroServices;

    /**
     * Constructor que inicializa los servicios necesarios para la lógica de sectores.
     *
     * @param controladora      Controlador de persistencia para interactuar con la base de datos.
     * @param casilleroServices Servicio de casilleros para gestionar los espacios de los sectores.
     */
    public Logica_Sectores(PersistenciaController controladora, CasilleroServices casilleroServices) {
        this.controladora = controladora;
        this.casilleroServices = casilleroServices;
    }

    /**
     * Obtiene todos los sectores registrados en la base de datos.
     *
     * @return Una lista de objetos {@link TbSectores} que representan los sectores.
     */
    public List<TbSectores> ObtenerSectores() {
        return controladora.ObtenerSectores();
    }

    /**
     * Crea un nuevo sector en la base de datos, y genera los espacios asociados a dicho sector.
     *
     * @param sector El sector que se desea crear.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion crearSector(TbSectores sector) {
        try {
            boolean result = controladora.CrearSector(sector);

            if (result) {
                TbSectores sectorCreado = ConseguirSector(sector.getId());

                // Crear los espacios asociados al nuevo sector
                ResultadoOperacion resultadoEspacios = casilleroServices.crearEspaciosParaSector(sectorCreado, sectorCreado);

                if (!resultadoEspacios.isExito()) {
                    return new ResultadoOperacion(false, "Sector creado, pero error al crear espacios: " + resultadoEspacios.getMensaje());
                }

                return new ResultadoOperacion(true, "Sector y espacios creados correctamente.");
            } else {
                return new ResultadoOperacion(false, "Error al crear el sector en la base de datos.");
            }

        } catch (Exception e) {
            System.out.println("Error al crear el sector: " + e);
            return new ResultadoOperacion(false, "Error al crear el sector: " + e.getMessage());
        }
    }

    /**
     * Obtiene un sector específico basado en su ID.
     *
     * @param sectorId El ID del sector a obtener.
     * @return El objeto {@link TbSectores} que representa el sector encontrado.
     */
    public TbSectores ConseguirSector(int sectorId) {
        return controladora.TraerSector(sectorId);
    }

    /**
     * Actualiza los datos de un sector, ajustando también los espacios según sea necesario.
     *
     * @param sector El sector con los datos actualizados.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion actualizarSector(TbSectores sector) {
        try {
            TbSectores sectorActual = ConseguirSector(sector.getId());
            ResultadoOperacion resultadoEspacios = casilleroServices.crearEspaciosParaSector(sectorActual, sector);

            if (!resultadoEspacios.isExito()) {
                return new ResultadoOperacion(false, "Error al ajustar los espacios: " + resultadoEspacios.getMensaje());
            }

            boolean result = controladora.ActualizarSector(sector);
            if (result) {
                return new ResultadoOperacion(true, "Sector actualizado correctamente.");
            } else {
                return new ResultadoOperacion(false, "Error al actualizar el sector.");
            }

        } catch (Exception e) {
            System.out.println("Error al actualizar el sector: " + e);
            return new ResultadoOperacion(false, "Error inesperado al actualizar el sector: " + e.getMessage());
        }
    }

    /**
     * Elimina un sector si no tiene espacios ocupados.
     *
     * @param idSector El ID del sector que se desea eliminar.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion eliminarSector(int idSector) {
        try {
            TbSectores sector = ConseguirSector(idSector);

            if (sector == null) {
                String mensaje = "El sector con ID " + idSector + " no existe.";
                return new ResultadoOperacion(false, mensaje);
            }

            boolean tieneEspaciosOcupados = sector.getEspacios().stream()
                    .anyMatch(espacio -> espacio.getEstado_espacio() != EstadoEspacio.Libre);

            if (tieneEspaciosOcupados) {
                String mensaje = "No se puede eliminar el sector. Tiene espacios ocupados.";
                return new ResultadoOperacion(false, mensaje);
            }

            boolean result = controladora.eliminarSector(idSector);
            if (result) {
                return new ResultadoOperacion(true, "Sector eliminado exitosamente.");
            } else {
                return new ResultadoOperacion(false, "No se pudo eliminar el sector.");
            }
        } catch (Exception e) {
            return new ResultadoOperacion(false, "Error al eliminar el sector: " + e.getMessage());
        }
    }
}
