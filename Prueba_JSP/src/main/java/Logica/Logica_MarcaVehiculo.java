package Logica;

import Controlador.PersistenciaController;
import Modelo.Tb_MarcaVehiculo;

import java.util.List;

public class Logica_MarcaVehiculo {
    PersistenciaController controladora = new PersistenciaController();
    public List<Tb_MarcaVehiculo> ObtenerMarcasPorTipo(int idTipoVehiculo) {
        try {
            return controladora.buscarMarcasPorTipo(idTipoVehiculo);
        }catch (Exception e){
            System.err.println("El documento proporcionado no es un número válido: " + e.getMessage());
            // Manejo adicional si es necesario, como lanzar una excepción personalizada o devolver una lista vacía.
        }

        return null;
    }
}
