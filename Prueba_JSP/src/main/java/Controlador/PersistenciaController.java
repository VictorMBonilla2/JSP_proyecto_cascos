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
    TipoDocumentoJPAController TpDocumento = new TipoDocumentoJPAController();
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
    public TbSectores TraerSector(int casilleroId) {
        return sectoresJPA.findTbCasillero(casilleroId);
    }

    public boolean CrearSector(TbSectores sector)  throws  Exception{
        try {
            sectoresJPA.create(sector);
            return true;
        } catch (Exception e) {
            System.err.println("Error al Crear el Sector: " + e.getMessage());
            throw e; // Propagar la excepción
        }

    }
    public boolean ActualizarSector(TbSectores sector) throws Exception {
        try {
            sectoresJPA.edit(sector);
            return true;
        } catch (Exception e) {
            System.err.println("Error al Actualizar el Sector: " + e.getMessage());
            throw e; // Propagar la excepción
        }

    }

    public boolean eliminarSector(int idSector) throws Exception {
        try{
            sectoresJPA.destroy(idSector);
            return true;
        }catch (Exception e) {
            System.err.println("Error al Actualizar el Sector: " + e.getMessage());
            throw e; // Propagar la excepción
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

    public List<TbEspacio> ObtenerEspaciosPorSector(int idSector){
        return espacioJPA.obtenerEspaciosPorSectorNativo(idSector);
    }
    public void eliminarEspacio(int espacio) throws Exception {
        espacioJPA.destroy(espacio);
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


    //JPA TIPO DOCUMENTO
    public List <TbTipoDocumento> BuscarTiposDocumento(){
        return TpDocumento.findTbTipoDocumentoEntities();
    };
    public TbTipoDocumento BuscarTipoDocumentoPorId(int idDocumento) {
        return TpDocumento.findTbTipoDocumento(idDocumento);
    }

    public boolean crearTipoDocumento(TbTipoDocumento documento) throws Exception {
        try{
            TpDocumento.create(documento);
            return true;
        }catch (Exception e) {
            System.err.println("Error al Crear el Sector: " + e.getMessage());
            throw e; // Propagar la excepción
        }
    }
    public boolean ActualizarTipoDocumento(TbTipoDocumento documento) throws Exception {
        try{
            TpDocumento.edit(documento);
            return true;
        }catch (Exception e) {
            System.err.println("Error al actualizar el tipo de documento: " + e.getMessage());
            throw e; // Propagar la excepción
        }
    }
    public boolean eliminarTipoDocumento(int idDocumento) throws Exception {
        try {
            TpDocumento.destroy(idDocumento);
            return true;
        }catch (Exception e){
            System.err.println("Error al eliminar el tipo de documento: " + e.getMessage());
            throw e;
        }
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

    public List<Roles> obtenerRoles() {
        return rolesJPA.findRolesEntities();
    }

    public boolean crearRol(Roles roles) {
        try {
            rolesJPA.create(roles);
            return  true;
        } catch (Exception e){
            System.err.println("Error al crear el rol: " + e.getMessage());
            throw e;
        }
    }

    public boolean ActualizarRol(Roles roles) throws Exception {
        try{
            rolesJPA.edit(roles);
            return true;
        }catch (Exception e){
            System.err.println("Error al actualizar el rol: " + e.getMessage());
            throw e;
        }
    }

    public boolean eliminarRol(int idRol) throws Exception {
        try {
            rolesJPA.destroy(idRol);
            return true;
        }catch (Exception e){
            System.err.println("Error al eliminar el rol: " + e.getMessage());
            throw e;
        }
    }
}
