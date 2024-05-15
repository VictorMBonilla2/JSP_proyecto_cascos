package Modelo;

import Controlador.PersistenciaController;

import java.util.List;


public class Controladora_logica {


    static PersistenciaController controladora = new PersistenciaController();

    public static boolean validarIngreso(int documento, String tipoDocumento, String clave) {

        List<LoginDTO> lista = controladora.login(documento);

        for (LoginDTO login : lista) {
            if (login.getTipoDocumento().equals(tipoDocumento) && login.getClave().equals(clave)) {
                return true;
            }
        }
        return false;
    }

    public void crearPersona(Persona perso){
         controladora.CrearPersona(perso);
     }

     public List<Persona> TraerPersonas(){
         return controladora.TraerPersonas();
     }

    public List<Integer> ObtenerEspacios() {
        return controladora.ObtEspacios();
    }
}