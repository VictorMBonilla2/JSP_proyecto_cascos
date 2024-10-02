package Logica;

import Controlador.PersistenciaController;
import Modelo.TbTipovehiculo;
import Utilidades.ResultadoOperacion;

import java.util.List;

public class Logica_TipoVehiculo {
    PersistenciaController controladora = new PersistenciaController();

    public TbTipovehiculo buscarTipoVehiculo(int tipoVehiculo) {
        return controladora.obtenerTipoVehiculoPorId(tipoVehiculo);
    }

    public List<TbTipovehiculo> ObtenerTiposVehiculo() {
        return controladora.BuscarTiposVehiculo();
    }

    // Método para crear un nuevo tipo de vehículo
    public ResultadoOperacion crearTipoVehiculo(TbTipovehiculo tipoVehiculo) {
        try {
            // Lógica para crear el tipo de vehículo en la base de datos
            controladora.crearTipoVehiculo(tipoVehiculo);
            return new ResultadoOperacion(true, "El tipo de vehículo ha sido creado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al crear el tipo de vehículo: " + e.getMessage());
            return new ResultadoOperacion(false, "Hubo un error al crear el tipo de vehículo en la base de datos.");
        }
    }

    // Método para actualizar un tipo de vehículo existente
    public ResultadoOperacion actualizarTipoVehiculo(TbTipovehiculo tipoVehiculo) {
        try {
            // Lógica para actualizar el tipo de vehículo en la base de datos
            controladora.actualizarTipoVehiculo(tipoVehiculo);
            return new ResultadoOperacion(true, "El tipo de vehículo ha sido actualizado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al actualizar el tipo de vehículo: " + e.getMessage());
            return new ResultadoOperacion(false, "Hubo un error al actualizar el tipo de vehículo en la base de datos.");
        }
    }

    // Método para eliminar un tipo de vehículo basado en su ID
    public ResultadoOperacion eliminarTipoVehiculo(int idTipo) {
        try {
            // Lógica para eliminar el tipo de vehículo en la base de datos
            controladora.eliminarTipoVehiculo(idTipo);
            return new ResultadoOperacion(true, "El tipo de vehículo ha sido eliminado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al eliminar el tipo de vehículo: " + e.getMessage());
            return new ResultadoOperacion(false, "Hubo un error al eliminar el tipo de vehículo en la base de datos.");
        }
    }


}
