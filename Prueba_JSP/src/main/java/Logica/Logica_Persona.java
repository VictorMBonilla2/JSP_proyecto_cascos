package Logica;

import Controlador.PersistenciaController;
import DTO.LoginDTO;
import Modelo.Persona;
import Modelo.TbVehiculo;
import Modelo.enums.EstadoUsuario;
import Utilidades.ResultadoOperacion;

import java.util.List;


public class Logica_Persona {
    PersistenciaController controladora = new PersistenciaController();
    public ResultadoOperacion validarIngreso(int documento, int tipoDocumento, String clave, String rol) {
        System.out.println("Iniciando validación de ingreso para documento: " + documento);

        // Obtener el LoginDTO correspondiente al documento
        Persona persona = controladora.buscarPersonaDocumento(documento);

        // Verificar si se encontró el documento
        if (persona == null) {
            System.err.println("Error: No se encontraron registros para el documento: " + documento);
            return new ResultadoOperacion(false, "No se encontraron registros para el documento.");
        }

        System.out.println("Documento encontrado, continuando validación...");

        int rolInt = Integer.parseInt(rol);

        // Validar las credenciales: tipo de documento, clave y rol
        if (persona.getClave() != null) {
            boolean tipoDocCoincide = persona.getTipoDocumento().getId() == tipoDocumento;
            boolean claveCoincide = persona.getClave().equals(clave);
            boolean rolCoincide = persona.getRol().getId() == rolInt;

            System.out.println("Resultado de validaciones:");
            System.out.println("Tipo de documento coincide: " + tipoDocCoincide);
            System.out.println("Clave coincide: " + claveCoincide);
            System.out.println("Rol coincide: " + rolCoincide);

            if (tipoDocCoincide && claveCoincide && rolCoincide) {
                // Verificar si el usuario está activo
                if (persona.getEstadoUsuario() == EstadoUsuario.ACTIVO) {
                    System.out.println("Usuario activo. Acceso permitido.");
                    return new ResultadoOperacion(true, "Acceso permitido.");
                } else {
                    System.err.println("Error: El usuario está deshabilitado.");
                    return new ResultadoOperacion(false, "El usuario está deshabilitado.");
                }
            } else {
                System.err.println("Error: Las credenciales proporcionadas no coinciden.");
                return new ResultadoOperacion(false, "Las credenciales proporcionadas no coinciden.");
            }
        }

        // Retornar error si la clave no existe
        System.err.println("Error: Clave no válida.");
        return new ResultadoOperacion(false, "Clave no válida.");
    }


    //Proceso Registro.
    public ResultadoOperacion crearPersona(Persona perso){
        try {
            controladora.CrearPersona(perso);
            return new ResultadoOperacion(true, "El Aprendiz se ha registrado");
        } catch (Exception e) {
            // Maneja la excepción, por ejemplo, registrando el error
            System.err.println("Error al crear la persona: " + e.getMessage());
            e.printStackTrace();
            return new ResultadoOperacion(false,"Hubo un error al crear al aprendiz");
        }
    }

    public ResultadoOperacion actualizarPersona(Persona user)  {
        try {

            String password = buscarpersonaPorId(user.getId()).getClave();
            user.setClave(password);
            controladora.EditarPersona(user);
            return new ResultadoOperacion(true,"Usuario actualizado correctamente"); // La actualización fue exitosa
        } catch (Exception e) {
            // Manejo del error
            System.err.println("Error al actualizar la persona: " + e.getMessage());
            e.printStackTrace(); // O puedes optar por loggear la excepción en lugar de imprimirla
            return new ResultadoOperacion(false,"Error al actualizar el usuario"); // La actualización falló
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

    public ResultadoOperacion borrarUsuario(int id) throws Exception {
        try{
            controladora.eliminarUsuario(id);
            return new ResultadoOperacion(true, "Usuario eliminado correctamente");
        }catch (Exception e){
            System.err.println("Error al eliminar la persona: " + e.getMessage());
            return new ResultadoOperacion(false, "Error al eliminar al usuario");
        }
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

    public ResultadoOperacion cambiarEstadoUsuario(int id) {
        Persona usuario = buscarpersonaPorId(id);
        if (usuario != null) {
            EstadoUsuario nuevoEstado = usuario.getEstadoUsuario() == EstadoUsuario.ACTIVO ? EstadoUsuario.INACTIVO : EstadoUsuario.ACTIVO;
            String accion = nuevoEstado == EstadoUsuario.ACTIVO ? "activado" : "deshabilitado";
            try {
                if(UsuarioEnEstacionamiento(id)){
                    return new ResultadoOperacion(false, "El usuario esta ocupando un espacio actualmente");
                }else{
                    controladora.actualizarestadoUsuario(usuario, nuevoEstado);
                    return new ResultadoOperacion(true, "El usuario ha sido " + accion + " exitosamente.");
                }
            } catch (Exception e) {
                System.err.println("Hubo un error al " + accion + " el usuario: " + e.getMessage());
                return new ResultadoOperacion(false, "Hubo un error al " + accion + " el usuario.");
            }
        } else {
            return new ResultadoOperacion(false, "Usuario no encontrado.");
        }
    }
    public boolean UsuarioEnEstacionamiento(int iduser) {
        Persona usuarioEnEspacios = controladora.buscarUsuarioEnEspacios(iduser);

        return usuarioEnEspacios != null;
    }


}
