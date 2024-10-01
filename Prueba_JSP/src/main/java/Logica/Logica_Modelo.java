package Logica;

import Controlador.PersistenciaController;
import Modelo.Tb_ModeloVehiculo;

import java.util.List;

public class Logica_Modelo {
    PersistenciaController controladora = new PersistenciaController();
    public List<Tb_ModeloVehiculo> ObtenerModelosPorMarcaYTipo(int idMarcaVehiculo, int idTipoVehiculo) {
        try{
           return   controladora.ObtenerModelosPorMarcaYTipo(idMarcaVehiculo,idTipoVehiculo);
        }catch (Exception e){
            System.err.println("El documento proporcionado no es un número válido: " + e.getMessage());
            // Manejo adicional si es necesario, como lanzar una excepción personalizada o devolver una lista vacía.
        }

        return null;
    }
}
