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
            if (login.getTipoDocumento().equals(tipoDocumento) && login.getClave().equals(clave)) {
                return true;
            }
        }
        return false;
    }
    //Proceso Registro.
    public void crearPersona(Persona perso){
         controladora.CrearPersona(perso);
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

    public List<TbEspacio> DatosEspacio(){
        return controladora.DatosEspacios();
    }


    public TbEspacio buscarEspacio(Integer idEspacio) {

        return controladora.traerEspacio(idEspacio);
    }

    public TbCasco buscarCascoPorPlaca(String placa) {
        return controladora.obtenerCasco(placa);
    }

    public void Crearcasco(TbCasco casco) {
        controladora.CrearCasco(casco);
    }




    public boolean actualizarEspacio(TbEspacio espacio) {

        try {
            controladora.ActualizarEspacio(espacio);
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al actualizar el espacio con ID " + espacio.getId(), e);
            return false;
            }
        }

    public void actualizarCasco(TbCasco casco) {

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
}
