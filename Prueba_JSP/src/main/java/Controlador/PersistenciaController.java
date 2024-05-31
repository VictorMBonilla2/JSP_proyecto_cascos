package Controlador;

import Modelo.*;

import java.util.List;

public class PersistenciaController {
//Aca se Instancian de todos los controladores Jpa, y se retorna las consultas deseadas
    PersonaJpaController persoJpa = new PersonaJpaController();
    CasillerosJPAController casillerosJPA = new CasillerosJPAController();
    EspacioJPAController espacioJPA = new EspacioJPAController();
    CascosJPAController cascosJPA = new CascosJPAController();
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




    public List<TbCasillero>  ObtEspacios() {
        return casillerosJPA.findTbCasilleroEntities();
    }
    public List<TbEspacio> DatosEspacios(){
        return espacioJPA.findTbEspacioEntities();
    }


//Espacios
    public TbEspacio traerEspacio(Integer idEspacio) {

        return espacioJPA.findTbEspacio(idEspacio);
    }






    public TbCasco obtenerCasco(String placa) {
    return cascosJPA.buscarCascoPorPlaca(placa);
    }

    public void CrearCasco(TbCasco casco) {

        cascosJPA.create(casco);
    }

    public void ActualizarEspacio(TbEspacio espacio) throws Exception {
        espacioJPA.edit(espacio);
    }
}
