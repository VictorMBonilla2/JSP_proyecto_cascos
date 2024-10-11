package Controlador;

import Modelo.*;
import Modelo.enums.EstadoUsuario;
import Modelo.enums.EstadoVehiculo;
import jakarta.persistence.PersistenceException;

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
    InformeJPAController informaJPA =  new InformeJPAController();




    //JPA PERSONA

    public void CrearPersona(Persona persona) throws Exception {
        persoJpa.create(persona);
    }
    public List<Persona> TraerPersonas() {
        return persoJpa.findPersonaEntities();
    }

    public long contarPersonas() {
        return persoJpa.contarUsuarios();
    }

    public int ContarUsuariosActivos(){
        return persoJpa.contarUsuariosActivos();
    }

    public int ContarUsuariosInacvitos(){
        return persoJpa.contarUsuariosInactivos();
    }

    public Persona buscarpersona(int documento) {
        return persoJpa.findPersona(documento);
    }

    public void EditarPersona(Persona user) throws Exception {
        persoJpa.edit(user);
    }
    public Persona buscarPersonaPorToken(String token) {
        return persoJpa.buscarPersonaToken(token);
    }
    public Persona buscarPersonaPorCorreo(String email) {
        return persoJpa.buscarPersonaEmail(email);
    }

    public List<Persona> TraerPersonasPorPagina(int data_inicio, int data_fin) {

        return  persoJpa.findPersonaEntities(data_fin,data_inicio);
    }
    public List<Persona> TraerPersonasActivas(int data_inicio, int data_fin) {

        return  persoJpa.findUsuariosActivos(data_fin,data_inicio);
    }
    public List<Persona> TraerPersonasInactivas(int data_inicio, int data_fin) {

        return  persoJpa.findUsuariosInactivos(data_fin,data_inicio);
    }

    public List<Persona> ObtenerPersonas() {
        return persoJpa.findPersonaEntities();
    }

    public Persona buscarPersonaDocumento(int documento) {
        return persoJpa.buscarPersonaDocumento(documento);
    }

    public void eliminarUsuario(int id) throws Exception {

        persoJpa.destroy(id);
    }
    public void actualizarestadoUsuario(Persona usuario, EstadoUsuario estadoUsuario) throws Exception {
        persoJpa.actualizarEstado(usuario, estadoUsuario);
    }
    public Persona buscarUsuarioEnEspacios(int iduser) {
        return persoJpa.buscarPersonaEnEspacio(iduser);
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
    public TbVehiculo buscarVehiculoPorPlaca(String placa) {
        return vehiculoJPA.findVehiculoByPlaca(placa);
    }
    public void CrearVehiculo(TbVehiculo vehiculo)  throws Exception{
        vehiculoJPA.create(vehiculo);
    }

    public void ActualizarVehiculo(TbVehiculo vehiculo) throws Exception {
        vehiculoJPA.edit(vehiculo);
    }

    public void actualizarestadoVehiculo(TbVehiculo vehiculo, EstadoVehiculo nuevoEstado) {
        vehiculoJPA.actualizarEstado(vehiculo, nuevoEstado);
    }

    public void eliminarVehiculo(int idVehiculo) throws Exception {
        vehiculoJPA.destroy(idVehiculo);
    }

    public TbVehiculo buscarVehiculoEnEspacios(int idVehiculo) {
        return vehiculoJPA.buscarVehiculoEnEspacio(idVehiculo);
    }


    //JPA REGISTRO


    public void CrearRegistro(TbRegistro nuevoRegistro) {
        registroJPA.create(nuevoRegistro);
    }

    public List<TbRegistro> ObtenerRegistros() {

        return registroJPA.findTbRegistroEntities();
    }
    public List<TbRegistro> ObtenerRegistrosGestor(int id) {
        return registroJPA.findRegistrosGestor(id);
    }
    public List<TbRegistro> ObtenerRegistrosAprendiz(int id) {
        return registroJPA.findRegistrosAprendiz(id);
    }


    //JPA REPORTE

    public List<TbReportes> ObtenerReportes() {
        return reportesJPA.findTbReportesEntities();
    }

    public void CrearReporte(TbReportes nuevoReporte) {
        reportesJPA.create(nuevoReporte);
    }

    public List<TbReportes> ObtenerReportesGestor(int idGestor) {
        return reportesJPA.findReportesGestor(idGestor);
    }
    public List<TbReportes> ObtenerReportesAprendiz(int idAprendiz) {
        return reportesJPA.findReportesAprendiz(idAprendiz);

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
    public boolean eliminarTipoDocumento(int idDocumento) throws PersistenceException, Exception {
        try {
            TpDocumento.destroy(idDocumento);
            return true;
        } catch (PersistenceException e) {
            // Registrar y propagar las excepciones de persistencia, como ConstraintViolationException
            System.err.println("Error de persistencia al eliminar el tipo de documento: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Capturar cualquier otra excepción no relacionada con la persistencia
            System.err.println("Error inesperado al eliminar el tipo de documento: " + e.getMessage());
            throw e; // Propagar la excepción hacia la capa lógica
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

    public boolean eliminarRol(int idRol) throws PersistenceException, Exception {
        try {
            rolesJPA.destroy(idRol); // Llamada a la capa de persistencia
            return true;
        } catch (PersistenceException e) {
            // Registrar y propagar las excepciones de persistencia, como ConstraintViolationException
            System.err.println("Error de persistencia al eliminar el rol: " + e.getMessage());
            throw e; // Propagar la excepción hacia la capa lógica
        } catch (Exception e) {
            // Capturar cualquier otra excepción no relacionada con la persistencia
            System.err.println("Error inesperado al eliminar el rol: " + e.getMessage());
            throw e; // Propagar la excepción hacia la capa lógica
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

    // JPA INFORMES
    public void subirInforme(TbInformesUsuarios informe) {
        informaJPA.create(informe);
    }

    public TbInformesUsuarios buscarInformePorId(Long informeId) {
        return informaJPA.findInforme(informeId);
    }
    public TbInformesUsuarios buscarInformePorCodigo(String informeCode) {
        return informaJPA.findInformeByCodigo(informeCode);
    }


}

