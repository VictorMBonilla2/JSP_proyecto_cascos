package Controlador;

import DTO.LoginDTO;
import Modelo.*;

import java.util.List;

public class FachadaPersistencia {

    private final PersonaJpaController personaJpaController;
    private final VehiculoJPAController vehiculoJpaController;
    private final EspacioJPAController espacioJpaController;
    private final CasillerosJPAController sectoresJpaController;
    private final RegistroJPAController registroJpaController;
    private final ReportesJPAController reportesJpaController;
    private final TiposVehiculosJPAController tiposVehiculosJpaController;
    private final RolesJPAController rolesJpaController;

    public FachadaPersistencia(
            PersonaJpaController personaJpaController,
            VehiculoJPAController vehiculoJpaController,
            EspacioJPAController espacioJpaController,
            CasillerosJPAController sectoresJpaController,
            RegistroJPAController registroJpaController,
            ReportesJPAController reportesJpaController,
            TiposVehiculosJPAController tiposVehiculosJpaController,
            RolesJPAController rolesJpaController) {

        this.personaJpaController = personaJpaController;
        this.vehiculoJpaController = vehiculoJpaController;
        this.espacioJpaController = espacioJpaController;
        this.sectoresJpaController = sectoresJpaController;
        this.registroJpaController = registroJpaController;
        this.reportesJpaController = reportesJpaController;
        this.tiposVehiculosJpaController = tiposVehiculosJpaController;
        this.rolesJpaController = rolesJpaController;
    }

    // Métodos relacionados con Persona
    public void crearPersona(Persona persona) throws Exception {
        personaJpaController.create(persona);
    }

    public Persona buscarPersona(int documento) {
        return personaJpaController.findPersona(documento);
    }

    public void editarPersona(Persona persona) throws Exception {
        personaJpaController.edit(persona);
    }

    public void eliminarPersona(int documento) throws Exception {
        personaJpaController.destroy(documento);
    }

    public List<Persona> traerPersonas() {
        return personaJpaController.findPersonaEntities();
    }

    public List<Persona> traerPersonasPorPagina(int inicio, int fin) {
        return personaJpaController.findPersonaEntities(fin, inicio);
    }

    public List<LoginDTO> login(int documento) {
        return personaJpaController.login(documento);
    }

    // Métodos relacionados con Vehiculo
    public void crearVehiculo(TbVehiculo vehiculo) throws Exception {
        vehiculoJpaController.create(vehiculo);
    }

    public void actualizarVehiculo(TbVehiculo vehiculo) throws Exception {
        vehiculoJpaController.edit(vehiculo);
    }

    public TbVehiculo buscarVehiculo(int id) {
        return vehiculoJpaController.findTbVehiculo(id);
    }

    public List<TbVehiculo> obtenerVehiculos(int documento) {
        return vehiculoJpaController.findVehiculosByPersona(documento);
    }

    // Métodos relacionados con Espacio
    public void crearEspacio(TbEspacio espacio) throws Exception {
        espacioJpaController.create(espacio);
    }

    public void actualizarEspacio(TbEspacio espacio) throws Exception {
        espacioJpaController.edit(espacio);
    }

    public TbEspacio traerEspacio(int id) {
        return espacioJpaController.findTbEspacio(id);
    }

    public void eliminarEspacio(int id) throws Exception {
        espacioJpaController.destroy(id);
    }

    // Métodos relacionados con Casilleros
    public void crearCasillero(TbSectores sector) throws Exception {
        sectoresJpaController.create(sector);
    }

    public void actualizarCasillero(TbSectores sector) throws Exception {
        sectoresJpaController.edit(sector);
    }

    public TbSectores traerCasillero(int id) {
        return sectoresJpaController.findTbCasillero(id);
    }

    public void eliminarCasillero(int id) throws Exception {
        sectoresJpaController.destroy(id);
    }

    // Métodos relacionados con Registro
    public void crearRegistro(TbRegistro registro) {
        registroJpaController.create(registro);
    }

    public List<TbRegistro> obtenerRegistros() {
        return registroJpaController.findTbRegistroEntities();
    }

    // Métodos relacionados con Reportes
    public void crearReporte(TbReportes reporte) {
        reportesJpaController.create(reporte);
    }

    public List<TbReportes> obtenerReportes() {
        return reportesJpaController.findTbReportesEntities();
    }

    // Métodos relacionados con Tipos de Vehiculo
    public List<TbTipovehiculo> buscarTiposVehiculo() {
        return tiposVehiculosJpaController.findtipovehiculoEntities();
    }

    public TbTipovehiculo obtenerTipoVehiculoPorId(int id) {
        return tiposVehiculosJpaController.findtipovehiculo(id);
    }

    // Métodos relacionados con Roles
    public Roles obtenerRol(int id) {
        return rolesJpaController.findRol(id);
    }
}
