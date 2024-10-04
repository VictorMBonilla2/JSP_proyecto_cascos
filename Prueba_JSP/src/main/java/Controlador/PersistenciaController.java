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
    CiudadVehiculosJPAController ciudadVehiculo= new CiudadVehiculosJPAController();
    MarcaVehiculoJPAController marcaVehiculoJPA = new MarcaVehiculoJPAController();
    ModeloVehiculoJPAController modeloVehiculoJPA = new ModeloVehiculoJPAController();



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

    public Persona buscarPersonaDocumento(int documento) {
        return persoJpa.buscarPersonaDocumento(documento);
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
    public List<TbVehiculo> obtenerVehiculos(int idPersona) {
        return vehiculoJPA.findVehiculosByPersona(idPersona);
    }
    public void CrearVehiculo(TbVehiculo vehiculo)  throws Exception{
        vehiculoJPA.create(vehiculo);
    }

    public void ActualizarVehiculo(TbVehiculo vehiculo) throws Exception {
        vehiculoJPA.edit(vehiculo);
    }

    public void eliminarVehiculo(int idVehiculo) throws Exception {
        vehiculoJPA.destroy(idVehiculo);
    }

    //JPA REGISTRO


    public void CrearRegistro(TbRegistro nuevoRegistro) {
        registroJPA.create(nuevoRegistro);
    }

    public List<TbRegistro> ObtenerRegistros() {

        return registroJPA.findTbRegistroEntities();
    }
    public List<TbRegistro> ObtenerRegistrosGestor(int documento) {
        return registroJPA.findRegistrosGestor(documento);
    }
    public List<TbRegistro> ObtenerRegistrosAprendiz(int documento) {
        return registroJPA.findRegistrosAprendiz(documento);
    }


    //JPA REPORTE

    public List<TbReportes> ObtenerReportes() {
        return reportesJPA.findTbReportesEntities();
    }

    public void CrearReporte(TbReportes nuevoReporte) {
        reportesJPA.create(nuevoReporte);
    }

    public List<TbReportes> ObtenerReportesGestor(int documento) {
        return reportesJPA.findReportesGestor(documento);
    }
    public List<TbReportes> ObtenerReportesAprendiz(int documento) {
        return reportesJPA.findReportesAprendiz(documento);
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
    public void crearTipoVehiculo(TbTipovehiculo tipoVehiculo) throws Exception {
         TpVehiculo.create(tipoVehiculo);
    }

    public void actualizarTipoVehiculo(TbTipovehiculo tipoVehiculo) throws Exception {
        TpVehiculo.edit(tipoVehiculo);
    }

    public void eliminarTipoVehiculo(int idTipo) throws Exception {
        TpVehiculo.destroy(idTipo);
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

    //MARCA VEHICULO.

    public void CrearMarca(Tb_MarcaVehiculo marca)  {
        marcaVehiculoJPA.create(marca);
    }
    public void ActualizarMarca(Tb_MarcaVehiculo marca) throws Exception {
        marcaVehiculoJPA.edit(marca);
    }
    public void eliminarMarca(int idMarca) throws Exception {
        marcaVehiculoJPA.destroy(idMarca);
    }

    public Tb_MarcaVehiculo buscarMarcasPorId(int idMarcaVehiculo) {
        return marcaVehiculoJPA.findMarcaVehiculo(idMarcaVehiculo);
    }

    public Tb_MarcaVehiculo obtenerMarcaPorTipo(int marcaVehiculo, int tipoVehiculo) {
        try{
            return marcaVehiculoJPA.findMarcaEntitiesForType(marcaVehiculo,tipoVehiculo);
        }catch (Exception e){
            System.err.println("Error al eliminar el rol: " + e.getMessage());
            throw e;
        }
    }
    public List<Tb_MarcaVehiculo> buscarMarcasPorTipo(int idTipoVehiculo) {
        try{
            return marcaVehiculoJPA.findMarcasForType(idTipoVehiculo);
        } catch ( Exception e){
            System.err.println("Error al eliminar el rol: " + e.getMessage());
            throw e;
        }
    }



    //MODELO VEHICULO

    public void CrearModelo(Tb_ModeloVehiculo modelo) {
        modeloVehiculoJPA.create(modelo);
    }

    public void ActualizarModelo(Tb_ModeloVehiculo modelo) throws Exception {
        modeloVehiculoJPA.edit(modelo);
    }

    public void eliminarModelo(int idModelo) throws Exception {
        modeloVehiculoJPA.destroy(idModelo);
    }

    public Tb_ModeloVehiculo buscarModeloPorId(int idModelo) {
        return modeloVehiculoJPA.findModeloVehiculo(idModelo);
    }

    public Tb_ModeloVehiculo obtenerModeloPorMarcaYTipo(int modeloVehiculo, int marcaVehiculo, int tipoVehiculo) {
        try{
            return modeloVehiculoJPA.findModeloEntitiesForTypeAndBrand(modeloVehiculo,marcaVehiculo,tipoVehiculo);
        }catch (Exception e){
            System.err.println("Error al eliminar el rol: " + e.getMessage());
            throw e;
        }

    }

    public List<Tb_ModeloVehiculo> ObtenerModelosPorMarcaYTipo(int idMarcaVehiculo, int idTipoVehiculo) {
        try {
            return modeloVehiculoJPA.findModelosPorMarcaYTipo(idMarcaVehiculo, idTipoVehiculo);
        }catch (Exception e){
            System.err.println("Error al eliminar el rol: " + e.getMessage());
            throw e;
        }
    }

    //CIUDAD VEHICULO

    public Tb_CiudadVehiculo BuscarCiudad(int ciudadId) {
        try {
            return ciudadVehiculo.findCiudadVehiculo(ciudadId);
        }catch (Exception e){
            System.err.println("Error al eliminar el rol: " + e.getMessage());
            throw e;
        }
    }
    public List<Tb_CiudadVehiculo> ObtenerCiudades(){
        try {
            return ciudadVehiculo.findCiudadVehiculoEntities();
        }catch (Exception e){
            System.err.println("Error al eliminar el rol: " + e.getMessage());
            throw e;
        }
    }


    public void CrearCiudad(Tb_CiudadVehiculo ciudad) {
        ciudadVehiculo.create(ciudad);
    }

    public void ActualizarCiudad(Tb_CiudadVehiculo ciudad) throws Exception {
        ciudadVehiculo.edit(ciudad);
    }

    public void EliminarCiudad(int idCiudad) throws Exception {
        ciudadVehiculo.destroy(idCiudad);
    }
}

