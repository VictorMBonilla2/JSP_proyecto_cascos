package Logica;

import Controlador.PersistenciaController;
import Modelo.Tb_CiudadVehiculo;

import java.util.List;

public class Logica_Ciudad {
    PersistenciaController controladora = new PersistenciaController();
    public Tb_CiudadVehiculo buscarCiudadPorId(int ciudadId) {
        try{
            return controladora.BuscarCiudad(ciudadId);
        }catch (Exception e){
            System.err.println("El documento proporcionado no es un número válido: " + e.getMessage());
            return null;
            // Manejo adicional si es necesario, como lanzar una excepción personalizada o devolver una lista vacía.
        }
    }

    public List<Tb_CiudadVehiculo> obtenerCiudades(){
        try{
            return controladora.ObtenerCiudades();
        } catch (Exception e){
            System.err.println("El documento proporcionado no es un número válido: " + e.getMessage());
            return null;
            // Manejo adicional si es necesario, como lanzar una excepción personalizada o devolver una lista vacía.
        }
    }
}
