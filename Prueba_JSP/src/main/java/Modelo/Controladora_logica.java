package Modelo;

import Controlador.PersistenciaController;

import java.util.List;


public class Controladora_logica {


    PersistenciaController controladora = new PersistenciaController();
     public void crearPersona(Persona perso){
         controladora.CrearPersona(perso);
     }

     public List<Persona> TraerPersonas(){
         return controladora.TraerPersonas();
     }
}
