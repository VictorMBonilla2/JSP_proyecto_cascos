package Modelo;

import Controlador.PersistenciaController;
import DTO.LoginDTO;
import DTO.VehiculoDTO;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class Controladora_logica {
    private static final Logger logger = Logger.getLogger(Controladora_logica.class.getName());
//Aca se reciben los retornos del Controlador de la persistencia y se hace todo el trabajo logico.
    static PersistenciaController controladora = new PersistenciaController();

    //Proceso Logeo
    public static boolean validarIngreso(int documento, String tipoDocumento, String clave, String rol) {

        List<LoginDTO> lista = controladora.login(documento);
        int rolInt = Integer.parseInt(rol);

        for (LoginDTO login : lista) {
            System.out.println("La coincidencia con el Tipo de documento es: "+ login.getTipoDocumento().equals(tipoDocumento));
            System.out.println("La coincidencia con la clave es: "+ login.getClave().equals(clave));
            boolean resultado = login.getRolId() == rolInt;
            System.out.println("La coincidencia con el rol es : " + resultado);
            if (login.getTipoDocumento().equals(tipoDocumento) && login.getClave().equals(clave) && login.getRolId() == rolInt) {
                return true;
            }
        }
        return false;
    }
    //Proceso Registro.
    public boolean crearPersona(Persona perso){
        try {
            controladora.CrearPersona(perso);
            return true; // La creación fue exitosa
        } catch (Exception e) {
            // Maneja la excepción, por ejemplo, registrando el error
            System.err.println("Error al crear la persona: " + e.getMessage());
            e.printStackTrace();
            return false; // Se produjo un error
        }
     }

    //Solicitud de Personas registradas.
    public List<Persona> TraerPersonas(){
         return controladora.TraerPersonas();
     }

    //Solicitud de espacios de un Casillero especifico.
    public Integer ObtenerEspacios(int id) {
        List<TbCasillero> casilleros= controladora.ObtEspacios();
        int espacios = 0;

        for (TbCasillero c : casilleros) {
            if (c.getId().equals(id)) { // Verificar si el ID del casillero coincide con el ID específico
                espacios = c.getCant_espacio(); // Obtener la cantidad de espacios del casillero
                break; // Salir del bucle una vez que se encuentre el casillero con el ID específico
            }
        }
        return espacios;
    }

    public List<TbEspacio> DatosEspacio() {
        return controladora.DatosEspacios();
    }


    public TbEspacio buscarEspacio(Integer idEspacio) {

        return controladora.traerEspacio(idEspacio);
    }

    public TbVehiculo buscarCascoPorPlaca(String placa) {
        return controladora.obtenerCasco(placa);
    }

    public void Crearcasco(TbVehiculo casco) {
        controladora.CrearCasco(casco);
    }




    public boolean actualizarEspacio(TbEspacio espacio) {

        try {
            controladora.ActualizarEspacio(espacio);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al actualizar el espacio con ID " + espacio.getId_espacio(), e);
            return false;
            }
        }

    public void actualizarCasco(TbVehiculo casco) {

        try{
            controladora.updateCasco(casco);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean borrarCasco(int idCasco) {
        try {
            controladora.deleteCasco(idCasco);
            return true; // Devuelve true si la eliminación es exitosa
        } catch (Exception e) {
            // Registra el error para el diagnóstico
            System.err.println("Error al eliminar el casco con ID " + idCasco + ": " + e.getMessage());
            return false; // Devuelve false si se produce una excepción
        }
    }

    public Persona buscarusuario(int documento) {

        Persona lista = controladora.buscarpersona(documento);

        return lista;

    }

//    public TbVehiculo buscarVehiculoPorDocumento(Integer documento) {
//        // Buscar la persona asociada al documento
//        Persona persona = controladora.buscarpersona(documento);
//
//        if (persona != null) {
//            // Obtener el ID del vehículo desde la persona
//            Integer idVehiculoString = persona.getVehiculo().getId_vehiculo();
//
//            if (idVehiculoString != null) {
//                try {
//                    // Convertir el ID del vehículo a Integer
//                    Integer vehiculoID = Integer.valueOf(idVehiculoString);
//                    // Buscar el vehículo asociado al ID
//                    return controladora.buscarvehiculo(vehiculoID);
//                } catch (NumberFormatException e) {
//                    // Manejar el caso en el que el ID del vehículo no se puede convertir a Integer
//                    System.err.println("Error al convertir id_vehiculo_FK a Integer: " + e.getMessage());
//                    return null;
//                }
//            } else {
//                // id_vehiculo_FK es null o vacío
//                System.err.println("id_vehiculo_FK es null o vacío para el documento: " + documento);
//                return null;
//            }
//        } else {
//            // Persona no encontrada
//            System.err.println("Persona no encontrada para el documento: " + documento);
//            return null;
//        }
//    }

    public Persona obtenerColaborador(int documento) {

       Persona Colaborador = buscarusuario(documento);

       if (Colaborador != null && Colaborador.getRol().getId() == 1) {

           return Colaborador;
       }

        return null;
    }

    public void CrearRegistro(TbRegistro nuevoRegistro) {
        controladora.CrearRegistro(nuevoRegistro);
    }

    public TbCasillero ConseguirCasillero(int casilleroId) {

        return controladora.TraerCasillero(casilleroId);

    }

    public void crearEspacio(TbEspacio espacio) {
        controladora.CrearEspacio(espacio);
    }

    public List<TbRegistro> ObtenerRegistros() {

        return controladora.ObtenerRegistros();
    }

    public List<TbReportes> ObtenerReportes() {
        List<TbReportes> reportes = controladora.ObtenerReportes();

        for (TbReportes reporte : reportes) {
            int numeroAPrendiz= reporte.getAprendiz().getDocumento();
            int numeroGestor=reporte.getColaborador().getDocumento();
           reporte.setAprendiz(buscarusuario(numeroAPrendiz)); ;
           reporte.setColaborador(buscarusuario(numeroGestor));
        }

        return reportes;
    }

    public Map<String, Integer> obtenerRegistrosPorSemana() {
        List<TbRegistro> registros = ObtenerRegistros();
        System.out.println("Total registros: " + registros.size()); // Verifica la cantidad de registros obtenidos

        // Obtener la fecha de hoy
        LocalDate today = LocalDate.now();

        // Calcular el inicio y fin de la semana actual (de lunes a domingo)
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = startOfWeek.plusDays(6); // Domingo de la misma semana

        System.out.println("Semana actual: " + startOfWeek + " - " + endOfWeek);

        // Agrupar registros por fecha dentro de la semana actual
        Map<LocalDate, Long> registrosPorFecha = registros.stream()
                .map(registro -> {
                    LocalDate fecha = registro.getFecha_registro().toLocalDate();
                    System.out.println("Fecha del registro: " + fecha); // Verifica cada fecha de registro
                    return fecha;
                })
                .filter(fecha -> !fecha.isBefore(startOfWeek) && !fecha.isAfter(endOfWeek))
                .collect(Collectors.groupingBy(
                        fecha -> fecha,
                        Collectors.counting()
                ));

        System.out.println("Registros por fecha: " + registrosPorFecha); // Verifica los registros agrupados por fecha

        // Convertir el resultado en un Map con String y Integer usando LinkedHashMap para mantener el orden
        Map<String, Integer> registrosPorSemana = new LinkedHashMap<>();
        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)) {
            registrosPorSemana.put(date.toString(), registrosPorFecha.getOrDefault(date, 0L).intValue());
        }

        System.out.println("Registros por semana: " + registrosPorSemana); // Verifica los registros por semana

        return registrosPorSemana;
    }

    public void CrearReporte(TbReportes nuevoReporte) {

        controladora.CrearReporte(nuevoReporte);
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
                    .map(vehiculo -> new VehiculoDTO(vehiculo.getId_vehiculo(), vehiculo.getPlaca_vehiculo(), vehiculo.getMarca_vehiculo(), vehiculo.getCant_casco()))
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

    public boolean actualizarPersona(Persona user)  {
        try {
            controladora.EditarPersona(user);
            return true; // La actualización fue exitosa
        } catch (Exception e) {
            // Manejo del error
            System.err.println("Error al actualizar la persona: " + e.getMessage());
            e.printStackTrace(); // O puedes optar por loggear la excepción en lugar de imprimirla
            return false; // La actualización falló
        }
    }

    public List<TbTipovehiculo> ObtenerTiposVehiculo() {

        return controladora.BuscarTiposVehiculo();
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

    public TbTipovehiculo buscarTipoVehiculo(int tipoVehiculo) {
        return controladora.obtenerTipoVehiculoPorId(tipoVehiculo);
    }

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

    public Roles ObtenerRol(int rol) {
        Roles roles = null;
        try{
            roles = controladora.ObtenerLogin(rol);
        } catch (Exception e){
            System.err.println("Error al obtener rol " + e.getMessage());
            e.printStackTrace();
            return roles;
        }
        return roles;
    }
}
