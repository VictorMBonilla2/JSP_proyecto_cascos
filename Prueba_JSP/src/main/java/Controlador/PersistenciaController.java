package Controlador;

import Modelo.*;

import java.util.List;

public class PersistenciaController {
//Aca se Instancian de todos los controladores Jpa, y se retorna las consultas deseadas
    PersonaJpaController persoJpa = new PersonaJpaController();
    CasillerosJPAController casillerosJPA = new CasillerosJPAController();
    EspacioJPAController espacioJPA = new EspacioJPAController();
    CascosJPAController cascosJPA = new CascosJPAController();
    VehiculoJPAController vehiculoJPA = new VehiculoJPAController();

    Persona persona = new Persona();

    public void CrearPersona(Persona persona) throws Exception {
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

    public TbVehiculo obtenerCasco(String placa) {
    return cascosJPA.buscarCascoPorPlaca(placa);
    }
    public void CrearCasco(TbVehiculo casco) {

        cascosJPA.create(casco);
    }
    public void updateCasco(TbVehiculo casco) throws Exception {
        cascosJPA.edit(casco);
    }

    public void ActualizarEspacio(TbEspacio espacio) throws Exception {
        espacioJPA.edit(espacio);
    }


    public void deleteCasco(int idCasco) throws Exception {
        cascosJPA.destroy(idCasco);
    }

    public Persona buscarpersona(int documento) {
        return persoJpa.findPersona(documento);
    }

    public TbVehiculo buscarvehiculo(int id_vehiculo) {
        return vehiculoJPA.findTbVehiculo(id_vehiculo);
    }


}
