package Controlador;

import Modelo.Persona;

import javax.swing.*;
import java.util.List;

public class PersistenciaController {

    PersonaJpaController persoJpa = new PersonaJpaController();

    Persona persona = new Persona();

    public void CrearPersona(Persona persona) {
        persoJpa.create(persona);
    }
    public List<Persona> TraerPersonas() {
        return persoJpa.findPersonaEntities();
    }
}
