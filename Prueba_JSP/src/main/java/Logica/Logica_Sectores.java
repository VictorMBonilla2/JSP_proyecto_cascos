package Logica;

import Controlador.PersistenciaController;
import Modelo.TbEspacio;
import Modelo.TbSectores;
import Modelo.CasilleroServices;
import Modelo.enums.EstadoEspacio;
import Utilidades.ResultadoOperacion;

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

    public ResultadoOperacion crearSector(TbSectores sector) {
        try {
            // Primero, intentar crear el sector en la base de datos
            boolean result = controladora.CrearSector(sector);

            // Verificar si la creación del sector fue exitosa
            if (result) {
                // Obtener el sector recién creado, incluyendo su ID
                TbSectores sectorCreado = ConseguirSector(sector.getId());

                // Crear los espacios para el sector recién creado
                ResultadoOperacion resultadoEspacios = casilleroServices.crearEspaciosParaSector(sectorCreado, sectorCreado);

                // Verificar si la creación de espacios fue exitosa
                if (!resultadoEspacios.isExito()) {
                    // Si la creación de espacios falló, retornar ese mensaje
                    return new ResultadoOperacion(false, "Sector creado, pero error al crear espacios: " + resultadoEspacios.getMensaje());
                }

                // Si todo salió bien
                return new ResultadoOperacion(true, "Sector y espacios creados correctamente.");
            } else {
                return new ResultadoOperacion(false, "Error al crear el sector en la base de datos.");
            }

        } catch (Exception e) {
            // En caso de excepción, capturamos el error y lo devolvemos en el ResultadoOperacion
            System.out.println("Error al crear el sector: " + e);
            return new ResultadoOperacion(false, "Error al crear el sector: " + e.getMessage());
        }
    }


    public TbSectores ConseguirSector(int sectorId) {
        return controladora.TraerSector(sectorId);
    }

    public ResultadoOperacion actualizarSector(TbSectores sector) {
        try {
            // Primero ajustar los espacios del sector con casilleroServices
            TbSectores sectorActual = ConseguirSector(sector.getId());
            ResultadoOperacion resultadoEspacios = casilleroServices.crearEspaciosParaSector(sectorActual, sector);

            // Verificar si la creación o eliminación de espacios fue exitosa
            if (!resultadoEspacios.isExito()) {
                System.out.println("Error al ajustar los espacios del sector: " + resultadoEspacios.getMensaje());
                return new ResultadoOperacion(false, "Error al ajustar los espacios: " + resultadoEspacios.getMensaje());
            }

            // Si todo salió bien con los espacios, proceder a actualizar el sector en la base de datos
            boolean result = controladora.ActualizarSector(sector);
            System.out.println("El resultado de la actualización del sector: " + result);

            // Confirmar si la actualización del sector fue exitosa
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


    public ResultadoOperacion eliminarSector(int idSector) {
        try {
            // Obtener el sector con sus espacios
            TbSectores sector = ConseguirSector(idSector);

            if (sector == null) {
                String mensaje = "El sector con ID " + idSector + " no existe.";
                System.out.println(mensaje);
                return new ResultadoOperacion(false, mensaje); // Retornar false si el sector no existe
            }

            // Verificar si hay espacios ocupados
            boolean tieneEspaciosOcupados = sector.getEspacios().stream()
                    .anyMatch(espacio -> espacio.getEstado_espacio() != EstadoEspacio.Libre);

            if (tieneEspaciosOcupados) {
                String mensaje = "No se puede eliminar el sector. Tiene espacios ocupados.";
                System.out.println(mensaje);
                return new ResultadoOperacion(false, mensaje); // No eliminar si hay espacios ocupados
            }

            // Intentar eliminar el sector si no tiene espacios ocupados
            boolean result = controladora.eliminarSector(idSector);

            if (result) {
                String mensaje = "Sector eliminado exitosamente.";
                System.out.println(mensaje);
                return new ResultadoOperacion(true, mensaje); // Retornar true si se eliminó con éxito
            } else {
                String mensaje = "No se pudo eliminar el sector.";
                System.out.println(mensaje);
                return new ResultadoOperacion(false, mensaje); // Retornar false si no se pudo eliminar
            }
        } catch (Exception e) {
            // Manejar cualquier excepción y retornar false
            String mensaje = "Error al eliminar el sector: " + e.getMessage();
            System.out.println(mensaje);
            return new ResultadoOperacion(false, mensaje); // En caso de error, retornar false con mensaje
        }
    }





}
