package Logica;

import Controlador.PersistenciaController;
import Modelo.Roles;

public class Logica_Rol {
    PersistenciaController controladora = new PersistenciaController();

    public Roles ObtenerRol(int rol) {
        Roles roles = null;
        try{
            roles = controladora.ObtenerRol(rol);
        } catch (Exception e){
            System.err.println("Error al obtener rol " + e.getMessage());
            e.printStackTrace();
            return roles;
        }
        return roles;
    }
}
