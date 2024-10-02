package Logica;

import Controlador.PersistenciaController;
import DTO.LoginDTO;
import Modelo.Persona;
import Modelo.TbVehiculo;

import java.util.List;


public class Logica_Persona {
    PersistenciaController controladora = new PersistenciaController();
    public List<LoginDTO> validarIngreso(int documento, int tipoDocumento, String clave, String rol) {

        List<LoginDTO> logeo = controladora.login(documento);
        int rolInt = Integer.parseInt(rol);

        // Verifica si la lista está vacía antes de iterar
        if (logeo.isEmpty()) {
            System.out.println("No se encontraron registros para el documento.");
            return null;
        }

        for (LoginDTO login : logeo) {
            // Solo verificar si la clave es nula, el tipoDocumento no lo será.
            if (login.getClave() != null) {
                boolean tipoDocCoincide = login.getTipoDocumento() == tipoDocumento;
                boolean claveCoincide = login.getClave().equals(clave);
                boolean rolCoincide = login.getRolId() == rolInt;

                System.out.println("La coincidencia con el Tipo de documento es: " + tipoDocCoincide);
                System.out.println("La coincidencia con la clave es: " + claveCoincide);
                System.out.println("La coincidencia con el rol es : " + rolCoincide);
                if (tipoDocCoincide && claveCoincide && rolCoincide) {
                    return logeo;
                }
            }
        }

        return null;
    }
    //Proceso Registro.
    public boolean crearPersona(Persona perso){
        try {
            controladora.CrearPersona(perso);
            return true; // La creación fue exitosa
        } catch (Exception e) {
            // Maneja la excepción, por ejemplo, registrando el error
            System.err.println("Error al crear la persona: " + e.getMessage());
            e.printStackTrace();
            return false; // Se produjo un error
        }
    }

    public boolean actualizarPersona(Persona user)  {
        try {
            controladora.EditarPersona(user);
            return true; // La actualización fue exitosa
        } catch (Exception e) {
            // Manejo del error
            System.err.println("Error al actualizar la persona: " + e.getMessage());
            e.printStackTrace(); // O puedes optar por loggear la excepción en lugar de imprimirla
            return false; // La actualización falló
        }
    }

    public Persona buscarpersonaPorId(int id) {

        Persona persona = controladora.buscarpersona(id);

        return persona;
    }

    public Persona buscarPersonaConDocumento(int documento) {
        try{
            return controladora.buscarPersonaDocumento(documento);
        }catch (Exception e){
            System.err.println("Error al obtener la persona: " + e.getMessage());
            return null;
        }
    }

    public void borrarUsuario(int documneto) throws Exception {
        controladora.eliminarUsuario(documneto);

    }

    public List<Persona> ObtenerUsuariosPorPagina(int numeroPagina) {
        int tamanioPagina = 10; // Tamaño de página fijo, ajusta según tus necesidades
        int data_inicio = (numeroPagina - 1) * tamanioPagina; // Índice inicial basado en la página solicitada
        int data_fin = tamanioPagina; // Número de resultados por página

        return controladora.TraerPersonasPorPagina(data_inicio, data_fin);
    }
    public Persona obtenerColaborador(int documento) {

        Persona Colaborador = buscarPersonaConDocumento(documento);

        if (Colaborador != null && Colaborador.getRol().getId() == 1) {

            return Colaborador;
        }

        return null;
    }

    // Método que busca un vehículo en la lista de vehículos de una persona por su ID
    public TbVehiculo buscarVehiculoPorId(Persona persona, int idVehiculo) {
        if (persona == null || persona.getVehiculos() == null) {
            return null;
        }
        return persona.getVehiculos().stream()
                .filter(vehiculo -> vehiculo.getId_vehiculo() == idVehiculo)
                .findFirst()
                .orElse(null);
    }

}
