package Logica;

import Controlador.PersistenciaController;
import Modelo.Persona;
import Modelo.TbInformesUsuarios;
import Modelo.TbVehiculo;
import Modelo.enums.EstadoUsuario;
import Servicios.EmailService;
import Servicios.PDFService;
import Servicios.PasswordService;
import Utilidades.ResultadoOperacion;

import java.util.*;
import java.util.stream.Collectors;


public class Logica_Persona {
    PersistenciaController controladora = new PersistenciaController();
    PDFService pdfService= new PDFService() ;
    public ResultadoOperacion validarIngreso(int documento, int tipoDocumento, String clave, String rol) {
        PasswordService passwordService = new PasswordService();
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
            boolean claveCoincide = passwordService.verificarContrasena(clave,persona.getClave());
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
    public ResultadoOperacion crearPersona(Persona perso , String password){
        try {
            PasswordService passwordService = new PasswordService();
            perso.setClave(passwordService.encriptarContrasena(password) );
            controladora.CrearPersona(perso);
            return new ResultadoOperacion(true, "El Usuario se ha registrado");
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

    public Map<String, Object> ObtenerUsuariosPorPagina(int numeroPagina) {
        int tamanioPagina = 10; // Tamaño de página fijo, ajusta según tus necesidades
        int data_inicio = (numeroPagina - 1) * tamanioPagina; // Índice inicial basado en la página solicitada
        int data_fin = tamanioPagina; // Número de resultados por página

        // Obtener el total de registros de personas
        long totalRegistros = controladora.contarPersonas(); // Método que cuenta el total de personas
        int totalPaginas = (int) Math.ceil((double) totalRegistros / tamanioPagina); // Calcular total de páginas

        // Obtener la lista paginada de personas
        List<Persona> usuarios = controladora.TraerPersonasPorPagina(data_inicio, data_fin);

        // Crear el mapa de resultados
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("usuarios", usuarios);
        resultado.put("totalRegistros", totalRegistros);
        resultado.put("totalPaginas", totalPaginas);
        resultado.put("paginaActual", numeroPagina);

        return resultado;
    }

    public Map<String, Object> findUsuariosActivos(int numeroPagina) {
        int tamanioPagina = 10; // Tamaño de la página
        int data_inicio = (numeroPagina - 1) * tamanioPagina;

        // Obtener el total de registros de usuarios activos
        long totalActivos = controladora.ContarUsuariosActivos();
        int totalPaginas = (int) Math.ceil((double) totalActivos / tamanioPagina);

        // Obtener la lista de usuarios activos paginada
        List<Persona> usuariosActivos = controladora.TraerPersonasActivas(data_inicio, tamanioPagina);

        // Crear un mapa para retornar los datos
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("usuarios", usuariosActivos);
        resultado.put("totalRegistros", totalActivos);
        resultado.put("totalPaginas", totalPaginas);
        resultado.put("paginaActual", numeroPagina);

        return resultado;
    }

    public Map<String, Object> findUsuariosInactivos(int numeroPagina) {
        int tamanioPagina = 10; // Tamaño de la página
        int data_inicio = (numeroPagina - 1) * tamanioPagina;

        // Obtener el total de registros de usuarios inactivos
        long totalInactivos = controladora.ContarUsuariosInacvitos();
        int totalPaginas = (int) Math.ceil((double) totalInactivos / tamanioPagina);

        // Obtener la lista de usuarios inactivos paginada
        List<Persona> usuariosInactivos = controladora.TraerPersonasInactivas(data_inicio, tamanioPagina);

        // Crear un mapa para retornar los datos
        Map<String, Object> resultado = new HashMap<>();
        resultado.put("usuarios", usuariosInactivos);
        resultado.put("totalRegistros", totalInactivos);
        resultado.put("totalPaginas", totalPaginas);
        resultado.put("paginaActual", numeroPagina);

        return resultado;
    }


    public String generarInforme(int idUsuario) {
        Persona usuario = buscarpersonaPorId(idUsuario);
        Set<TbVehiculo> vehiculos = usuario.getVehiculos();
        List<TbVehiculo> listaVehiculos = new ArrayList<>(vehiculos);
        try{
            TbInformesUsuarios informe = pdfService.generarPDF(usuario, listaVehiculos);

            controladora.subirInforme(informe);

            String codigo = informe.getCodigoInforme();
            System.out.println(codigo);
            return codigo;
        }catch (Exception e){
            System.err.println("Hubo un error al crear el pdf "+ e.getMessage());
            return null;
        }

    }

    public TbInformesUsuarios findInformeById(Long informeId) {

        return controladora.buscarInformePorId(informeId);

    }
    public TbInformesUsuarios findInformeByCode(String informeCode) {

        return controladora.buscarInformePorCodigo(informeCode);

    }

    public String generarTokenRecuperacion(String email) {
        System.out.println("Intentando obtener usuario");
        Persona usuario = buscarPersonaPorCorreo(email);
        if (usuario != null) {
            System.out.println("Usuario obtenido");
            String token = UUID.randomUUID().toString();
            // Guardar el token en la base de datos con un tiempo de expiración
            usuario.setTokenRecuperacion(token);
            usuario.setFechaExpiracionToken(new Date(System.currentTimeMillis() + 3600000)); // 1 hora
            actualizarPersona(usuario);
            return token;
        }
        return null;
    }
    public void enviarCorreoRecuperacion(String email, String token) {
        System.out.println("Se inicia la creacion del correo");
        EmailService emailService = new EmailService();
        emailService.enviarCorreoRecuperacion(email, token);
    }
    public Persona buscarPorToken(String token) {
        return controladora.buscarPersonaPorToken(token);
    }

    public boolean isTokenValid(Persona usuario) {
        System.out.println("Se intenta validar el tocken.");
        Date now = new Date();
        return usuario.getFechaExpiracionToken().after(now); // Verifica si el token aún no ha expirado
    }

    public void actualizarPassword(Persona usuario, String nuevaPassword) {
        System.out.println("Se inicia el cambio de contraseña");
        PasswordService passwordService = new PasswordService();
        String clave_encriptada= passwordService.encriptarContrasena(nuevaPassword);
        System.out.println("Clave encriptada: "+clave_encriptada );
        usuario.setClave(clave_encriptada); // Aplica una función hash a la contraseña antes de guardarla
        usuario.setTokenRecuperacion(null); // Limpia el token después de usarlo
        usuario.setFechaExpiracionToken(null);
        try{
            controladora.EditarPersona(usuario); // Limpia la fecha de expiración
            System.out.println("Contraseña cambiada");
        } catch (Exception e){
            System.out.println("Error al cambiar contraseña");
        }

    }

    private Persona buscarPersonaPorCorreo(String email) {

        return controladora.buscarPersonaPorCorreo(email);
    }
}
