package Logica;

import Controlador.PersistenciaController;
import DTO.VehiculoDTO;
import Modelo.TbTipovehiculo;
import Modelo.TbVehiculo;
import Modelo.Tb_MarcaVehiculo;
import Modelo.Tb_ModeloVehiculo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Logica_Vehiculo {
    PersistenciaController controladora = new PersistenciaController();
    public boolean crearVehiculo(TbVehiculo vehiculo) {

        try{
            controladora.CrearVehiculo(vehiculo);
            return true;
        }catch (Exception e) {
            System.err.println("Error al crear al vehiculo " + e.getMessage());
            e.printStackTrace();
            return false;
        }

    }

    public List<TbTipovehiculo> ObtenerTiposVehiculo() {
        return controladora.BuscarTiposVehiculo();
    }
    public TbTipovehiculo buscarTipoVehiculo(int tipoVehiculo) {
        return controladora.obtenerTipoVehiculoPorId(tipoVehiculo);
    }

    public boolean actualizarVehiculo(TbVehiculo vehiculo) {

        try{
            controladora.ActualizarVehiculo(vehiculo);
            return  true;
        }catch ( Exception e){
            System.err.println("Error al actualizar al vehiculo " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<VehiculoDTO> obtenerVehiculosDePersona(String documento) {
        List<VehiculoDTO> ListaNueva = new ArrayList<>();

        try {
            int documentoInt = Integer.parseInt(documento);
            List<TbVehiculo> Lista = controladora.obtenerVehiculos(documentoInt);

            if (Lista == null || Lista.isEmpty()) {
                throw new Exception("No hay vehículos registrados para el documento: " + documento);
            }

            ListaNueva = Lista.stream()
                    .map(vehiculo -> new VehiculoDTO(vehiculo.getId_vehiculo(), vehiculo.getPlacaVehiculo(), vehiculo.getMarcaVehiculo().getNombreMarca(), vehiculo.getCantCasco()))
                    .collect(Collectors.toList());

        } catch (NumberFormatException e) {
            System.err.println("El documento proporcionado no es un número válido: " + e.getMessage());
            // Manejo adicional si es necesario, como lanzar una excepción personalizada o devolver una lista vacía.
        } catch (Exception e) {
            System.err.println("Error al obtener vehículos: " + e.getMessage());
            // Puedes manejar esto devolviendo una lista vacía, lanzando una excepción, o lo que consideres adecuado.
        }

        return ListaNueva;
    }

    public List<TbVehiculo> buscarVehiculoDePersona(String id) {
        List<TbVehiculo>  ListaVehiculos = new ArrayList<>();
        try {
            int documentoInt = Integer.parseInt(id);
            List<TbVehiculo> Lista = controladora.obtenerVehiculos(documentoInt);

            if (Lista == null || Lista.isEmpty()) {
                throw new Exception("No hay vehículos registrados para el documento: " + id);
            }
            ListaVehiculos = Lista;

        } catch (NumberFormatException e) {
            System.err.println("El documento proporcionado no es un número válido: " + e.getMessage());
            // Manejo adicional si es necesario, como lanzar una excepción personalizada o devolver una lista vacía.
        } catch (Exception e) {
            System.err.println("Error al obtener vehículos: " + e.getMessage());
            // Puedes manejar esto devolviendo una lista vacía, lanzando una excepción, o lo que consideres adecuado.
        }

        return ListaVehiculos;
    }

    public Tb_MarcaVehiculo buscarMarcaPorTipo(int marcaVehiculo, int tipoVehiculo) {
        try{
            return controladora.obtenerMarcaPorTipo(marcaVehiculo, tipoVehiculo);
        }catch (Exception e){
            System.err.println("Error al obtener vehículos: " + e.getMessage());
            // Puedes manejar esto devolviendo una lista vacía, lanzando una excepción, o lo que consideres adecuado.
            return null;
        }

    }

    public Tb_ModeloVehiculo buscarModeloPorMarcaYTipo(int modeloVehiculo, int marcaVehiculo, int tipoVehiculo) {
        try{
            return controladora.obtenerModeloPorMarcaYTipo(modeloVehiculo, marcaVehiculo, tipoVehiculo);
        }catch (Exception e){
            System.err.println("Error al obtener vehículos: " + e.getMessage());
            // Puedes manejar esto devolviendo una lista vacía, lanzando una excepción, o lo que consideres adecuado.
            return null;
        }

    }
}
