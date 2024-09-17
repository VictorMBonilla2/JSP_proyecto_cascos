package Controlador;

import DTO.LoginDTO;
import Modelo.*;

import java.util.List;

public class PersistenciaController {
//Aca se Instancian de todos los controladores Jpa, y se retorna las consultas deseadas
    PersonaJpaController persoJpa = new PersonaJpaController();
    CasillerosJPAController sectoresJPA = new CasillerosJPAController();
    EspacioJPAController espacioJPA = new EspacioJPAController();
    CascosJPAController cascosJPA = new CascosJPAController();
    VehiculoJPAController vehiculoJPA = new VehiculoJPAController();
    RegistroJPAController registroJPA = new RegistroJPAController();
    ReportesJPAController reportesJPA = new ReportesJPAController();
    TiposVehiculosJPAController TpVehiculo = new TiposVehiculosJPAController();
    RolesJPAController rolesJPA = new RolesJPAController();


    //JPA PERSONA

    public void CrearPersona(Persona persona) throws Exception {
        persoJpa.create(persona);
    }
    public List<Persona> TraerPersonas() {
        return persoJpa.findPersonaEntities();
    }

    public List<LoginDTO> login(int documento){
        return  persoJpa.login(documento);
    }

    public Persona buscarpersona(int documento) {
        return persoJpa.findPersona(documento);
    }

    public void EditarPersona(Persona user) throws Exception {
        persoJpa.edit(user);
    }
    public List<Persona> TraerPersonasPorPagina(int data_inicio, int data_fin) {

        return  persoJpa.findPersonaEntities(data_fin,data_inicio);
    }

    public List<Persona> ObtenerPersonas() {
        return persoJpa.findPersonaEntities();
    }

    public void eliminarUsuario(int documneto) throws Exception {

        persoJpa.destroy(documneto);
    }

    //JPA SECTORES

    public List<TbSectores> ObtenerSectores() {
        return sectoresJPA.findTbCasilleroEntities();
    }
    public TbSectores TraerCasillero(int casilleroId) {
        return sectoresJPA.findTbCasillero(casilleroId);
    }

    public boolean CrearSector(TbSectores sector)  throws  Exception{
        try {
            sectoresJPA.create(sector);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
    public boolean ActualizarSector(TbSectores sector) throws Exception {
        try {
            sectoresJPA.edit(sector);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    //JPA ESPACIOS
    public List<TbEspacio> DatosEspacios(){
        return espacioJPA.findTbEspacioEntities();
    }

    public void ActualizarEspacio(TbEspacio espacio) throws Exception {
        espacioJPA.edit(espacio);
    }
    public TbEspacio traerEspacio(Integer idEspacio) {

        return espacioJPA.findTbEspacio(idEspacio);
    }
    public void CrearEspacio(TbEspacio espacio) {
        espacioJPA.create(espacio);
    }


    //JPA CASCOS

    public TbVehiculo obtenerCasco(String placa) {
    return cascosJPA.buscarCascoPorPlaca(placa);
    }
    public void CrearCasco(TbVehiculo casco) {

        cascosJPA.create(casco);
    }
    public void updateCasco(TbVehiculo casco) throws Exception {
        cascosJPA.edit(casco);
    }
    public void deleteCasco(int idCasco) throws Exception {
        cascosJPA.destroy(idCasco);
    }

    //JPA VEHICULO

    public TbVehiculo buscarvehiculo(int id_vehiculo) {
        return vehiculoJPA.findTbVehiculo(id_vehiculo);
    }
    public List<TbVehiculo> obtenerVehiculos(int documentoInt) {
        return vehiculoJPA.findVehiculosByPersona(documentoInt);
    }
    public void CrearVehiculo(TbVehiculo vehiculo)  throws Exception{
        vehiculoJPA.create(vehiculo);
    }

    public void ActualizarVehiculo(TbVehiculo vehiculo) throws Exception {
        vehiculoJPA.edit(vehiculo);
    }


    //JPA REGISTRO


    public void CrearRegistro(TbRegistro nuevoRegistro) {
        registroJPA.create(nuevoRegistro);
    }

    public List<TbRegistro> ObtenerRegistros() {

        return registroJPA.findTbRegistroEntities();
    }

    //JPA REPORTE

    public List<TbReportes> ObtenerReportes() {
        return reportesJPA.findTbReportesEntities();
    }

    public void CrearReporte(TbReportes nuevoReporte) {
        reportesJPA.create(nuevoReporte);
    }


    //JPA TIPO VEHICULO
    public List<TbTipovehiculo> BuscarTiposVehiculo() {
        return TpVehiculo.findtipovehiculoEntities();
    }

    public TbTipovehiculo obtenerTipoVehiculoPorId(int tipoVehiculo) {

       return TpVehiculo.findtipovehiculo(tipoVehiculo);
    }


    //JPA ROLES

    public Roles ObtenerRol(int rol) {
        return rolesJPA.findRol(rol);
    }

    public void eliminarEspacio(Integer espacio) throws Exception {
        espacioJPA.destroy(espacio);
    }


}
