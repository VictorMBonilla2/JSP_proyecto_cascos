package Logica;

import Controlador.PersistenciaController;
import DTO.VehiculoDTO;
import Modelo.Persona;
import Modelo.TbVehiculo;
import Modelo.Tb_MarcaVehiculo;
import Modelo.Tb_ModeloVehiculo;
import Modelo.enums.EstadoUsuario;
import Modelo.enums.EstadoVehiculo;
import Utilidades.ResultadoOperacion;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que contiene la lógica relacionada con la gestión de vehículos.
 * Proporciona métodos para crear, actualizar, buscar, eliminar y cambiar el estado de los vehículos.
 */
public class Logica_Vehiculo {
    PersistenciaController controladora = new PersistenciaController();
    Logica_Persona logicaPersona = new Logica_Persona();

    /**
     * Crea un nuevo vehículo en la base de datos.
     *
     * @param vehiculo El vehículo a crear.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion crearVehiculo(TbVehiculo vehiculo) {

        try{
            controladora.CrearVehiculo(vehiculo);
            return new ResultadoOperacion(true,"Vehiculo creado correctamente.");
        }catch (Exception e) {
            System.err.println("Error al crear al vehiculo " + e.getMessage());
            return new ResultadoOperacion(false,"Error al crear el vehiculo.");
        }

    }


    /**
     * Actualiza los datos de un vehículo existente en la base de datos.
     *
     * @param vehiculo El vehículo con los datos actualizados.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion actualizarVehiculo(TbVehiculo vehiculo) {

        try{
            controladora.ActualizarVehiculo(vehiculo);
            return  new ResultadoOperacion(true, "Vehiculo actualizado correctamente");
        }catch ( Exception e){
            System.err.println("Error al actualizar al vehiculo " + e.getMessage());;
            return new ResultadoOperacion(false, "Error al actualizar el vehiculo");
        }
    }

    /**
     * Obtiene la lista de vehículos de una persona en base a su documento de identidad.
     *
     * @param documento El documento de identidad de la persona.
     * @return Una lista de objetos {@link VehiculoDTO} con los vehículos de la persona.
     */

    public List<VehiculoDTO> obtenerVehiculosDePersona(String documento) {
        List<VehiculoDTO> ListaNueva = new ArrayList<>();

        try {
            int documentoInt = Integer.parseInt(documento);
            List<TbVehiculo> Lista = controladora.obtenerVehiculos(documentoInt);

            if (Lista == null || Lista.isEmpty()) {
                throw new Exception("No hay vehículos registrados para el documento: " + documento);
            }

            ListaNueva = Lista.stream()
                    .map(vehiculo -> new VehiculoDTO(vehiculo.getId_vehiculo(), vehiculo.getPlacaVehiculo(), vehiculo.getModeloVehiculo().getNombreModelo(), vehiculo.getCantCasco()))
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


    /**
     * Busca los vehículos registrados de una persona en base a su documento.
     *
     * @param documento El documento de identidad de la persona.
     * @return Una lista de objetos {@link TbVehiculo} con los vehículos de la persona.
     */
    public List<TbVehiculo> buscarVehiculoDePersona(String documento) {
        List<TbVehiculo>  ListaVehiculos = new ArrayList<>();
        try {

            int documentoInt = Integer.parseInt(documento);
            Persona aprendiz=  logicaPersona.buscarPersonaConDocumento(documentoInt);
            if (aprendiz == null || aprendiz.getEstadoUsuario().equals(EstadoUsuario.INACTIVO)){
                throw new Exception("El aprendiz no existe o esta inactivo " + documento);
            }
            List<TbVehiculo> Lista = controladora.obtenerVehiculos(aprendiz.getId());

            if (Lista == null || Lista.isEmpty()) {
                throw new Exception("No hay vehículos registrados para el documento: " + documento);
            }
            ListaVehiculos = Lista;

        } catch (NumberFormatException e) {
            System.err.println("El documento proporcionado no es un número válido: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error al obtener vehículos: " + e.getMessage());
        }

        return ListaVehiculos;
    }

    /**
     * Busca un vehículo en base a su placa.
     *
     * @param placa La placa del vehículo.
     * @return El vehículo encontrado o {@code null} si no existe.
     */
    public TbVehiculo buscarVehiculoPorPlaca(String placa) {
        TbVehiculo vehiculo = new TbVehiculo();
        try{
            vehiculo= controladora.buscarVehiculoPorPlaca(placa);
        } catch (Exception e){
            System.err.println("Error al obtener vehículos: " + e.getMessage());
        }
        return vehiculo;
    }

    /**
     * Busca la marca de un vehículo según su tipo.
     *
     * @param marcaVehiculo El ID de la marca del vehículo.
     * @param tipoVehiculo  El ID del tipo de vehículo.
     * @return La marca del vehículo o {@code null} si no se encuentra.
     */

    public Tb_MarcaVehiculo buscarMarcaPorTipo(int marcaVehiculo, int tipoVehiculo) {
        try{
            return controladora.obtenerMarcaPorTipo(marcaVehiculo, tipoVehiculo);
        }catch (Exception e){
            System.err.println("Error al obtener vehículos: " + e.getMessage());
            // Puedes manejar esto devolviendo una lista vacía, lanzando una excepción, o lo que consideres adecuado.
            return null;
        }

    }
    /**
     * Busca el modelo de un vehículo según su marca y tipo.
     *
     * @param modeloVehiculo El ID del modelo del vehículo.
     * @param marcaVehiculo  El ID de la marca del vehículo.
     * @param tipoVehiculo   El ID del tipo de vehículo.
     * @return El modelo del vehículo o {@code null} si no se encuentra.
     */
    public Tb_ModeloVehiculo buscarModeloPorMarcaYTipo(int modeloVehiculo, int marcaVehiculo, int tipoVehiculo) {
        try{
            return controladora.obtenerModeloPorMarcaYTipo(modeloVehiculo, marcaVehiculo, tipoVehiculo);
        }catch (Exception e){
            System.err.println("Error al obtener vehículos: " + e.getMessage());
            // Puedes manejar esto devolviendo una lista vacía, lanzando una excepción, o lo que consideres adecuado.
            return null;
        }

    }
    /**
     * Elimina un vehículo en base a su ID.
     *
     * @param idVehiculo El ID del vehículo a eliminar.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion borrarVehiculo(int idVehiculo) {
        try{
            controladora.eliminarVehiculo(idVehiculo);
            return new ResultadoOperacion(true, "El vehiculo ha sido eliminado exitosamente");
        } catch (Exception e){
            System.err.println("Hubo un error al eliminar el vehiculo: " + e.getMessage());
            return new ResultadoOperacion(false,"Hubo un error al eliminar el vehiculo");
        }
    }

    /**
     * Cambia el estado de un vehículo (ACTIVO/INACTIVO).
     *
     * @param idVehiculo El ID del vehículo cuyo estado se desea cambiar.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion cambiarEstadoVehiculo(int idVehiculo) {
        TbVehiculo vehiculo = buscarVehiculoPorId(idVehiculo);
        if (vehiculo != null) {
            EstadoVehiculo nuevoEstado = vehiculo.getEstadoVehiculo() == EstadoVehiculo.ACTIVO ? EstadoVehiculo.INACTIVO : EstadoVehiculo.ACTIVO;
            String accion = nuevoEstado == EstadoVehiculo.ACTIVO ? "activado" : "deshabilitado";
            try {
                if(VehiculoEnEstacionamiento(idVehiculo)){
                    return new ResultadoOperacion(false, "El usuario esta ocupando un espacio actualmente");
                }else{
                    controladora.actualizarestadoVehiculo(vehiculo, nuevoEstado);
                    return new ResultadoOperacion(true, "El usuario ha sido " + accion + " exitosamente.");
                }
            } catch (Exception e) {
                System.err.println("Hubo un error al " + accion + " el usuario: " + e.getMessage());
                return new ResultadoOperacion(false, "Hubo un error al " + accion + " el usuario.");
            }
        } else {
            return new ResultadoOperacion(false, "Usuario no encontrado.");
        }
    }

    /**
     * Busca un vehículo por su ID.
     *
     * @param idVehiculo El ID del vehículo.
     * @return El vehículo encontrado o {@code null} si no existe.
     */
    private TbVehiculo buscarVehiculoPorId(int idVehiculo) {
        try{
            return controladora.buscarvehiculo(idVehiculo);
        }catch (Exception e){
            System.err.println("Error al buscar el vehiculo: " + e.getMessage());
        }
        return  null;
    }
    /**
     * Verifica si un vehículo está actualmente en un estacionamiento.
     *
     * @param idVehiculo El ID del vehículo.
     * @return {@code true} si el vehículo está en un estacionamiento, {@code false} en caso contrario.
     */
    private boolean VehiculoEnEstacionamiento(int idVehiculo){
        TbVehiculo vehiculoEnEspacios = controladora.buscarVehiculoEnEspacios(idVehiculo);

        return vehiculoEnEspacios != null;
    }


}
