package Modelo;

import Controlador.PersistenciaController;

import java.util.List;


public class Controladora_logica {

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



}