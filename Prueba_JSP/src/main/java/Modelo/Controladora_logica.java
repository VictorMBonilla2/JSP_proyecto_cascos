package Modelo;

import Controlador.PersistenciaController;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Controladora_logica {
    private static final Logger logger = Logger.getLogger(Controladora_logica.class.getName());
//Aca se reciben los retornos del Controlador de la persistencia y se hace todo el trabajo logico.
    static PersistenciaController controladora = new PersistenciaController();

    //Proceso Logeo
    public static boolean validarIngreso(int documento, String tipoDocumento, String clave) {

        List<LoginDTO> lista = controladora.login(documento);

        for (LoginDTO login : lista) {
            System.out.println(login.getTipoDocumento().equals(tipoDocumento));
            System.out.println(login.getClave().equals(clave));
            if (login.getTipoDocumento().equals(tipoDocumento) && login.getClave().equals(clave)) {
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

        List<Persona> lista = controladora.TraerPersonas();

        for (Persona persona : lista) {
            if (persona.getDocumento() == documento) {
                return persona;
            }
        }
        return null;
    }

    public TbVehiculo buscarVehiculoPorDocumento(Integer documento) {
        // Buscar la persona asociada al documento
        Persona persona = controladora.buscarpersona(documento);

        if (persona != null) {
            // Obtener el ID del vehículo desde la persona
            Integer idVehiculoString = persona.getVehiculo().getId_vehiculo();

            if (idVehiculoString != null) {
                try {
                    // Convertir el ID del vehículo a Integer
                    Integer vehiculoID = Integer.valueOf(idVehiculoString);
                    // Buscar el vehículo asociado al ID
                    return controladora.buscarvehiculo(vehiculoID);
                } catch (NumberFormatException e) {
                    // Manejar el caso en el que el ID del vehículo no se puede convertir a Integer
                    System.err.println("Error al convertir id_vehiculo_FK a Integer: " + e.getMessage());
                    return null;
                }
            } else {
                // id_vehiculo_FK es null o vacío
                System.err.println("id_vehiculo_FK es null o vacío para el documento: " + documento);
                return null;
            }
        } else {
            // Persona no encontrada
            System.err.println("Persona no encontrada para el documento: " + documento);
            return null;
        }
    }

    public Persona obtenerColaborador(int documento) {

       Persona Colaborador = buscarusuario(documento);

       if (Colaborador != null && Colaborador.getRol().equals("Gestor")) {

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
}
