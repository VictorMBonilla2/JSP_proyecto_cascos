package Controlador;

import Modelo.LoginDTO;
import Modelo.Persona;

import java.util.List;

public class PersistenciaController {

    PersonaJpaController persoJpa = new PersonaJpaController();
    CasillerosJPAController casillerosJPA = new CasillerosJPAController();
    Persona persona = new Persona();

    public void CrearPersona(Persona persona) {
        persoJpa.create(persona);
    }

    public List<Persona> TraerPersonas() {
        return persoJpa.findPersonaEntities();
    }

    public List<LoginDTO> login(int documento){
        return  persoJpa.login(documento);
    }

    public List<Integer> ObtEspacios() {
        return casillerosJPA.ObtenerCantidadEspacios();
    }
}
