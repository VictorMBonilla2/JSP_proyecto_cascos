package Logica;

import Controlador.PersistenciaController;
import Modelo.Persona;
import Modelo.TbInformesUsuarios;
import Modelo.TbRecuperacion;
import Modelo.TbVehiculo;
import Modelo.enums.EstadoUsuario;
import Servicios.PDFService;
import Servicios.PasswordService;
import Utilidades.ResultadoOperacion;

import java.util.*;

/**
 * Clase que contiene la lógica relacionada con la gestión de personas (usuarios).
 * Proporciona métodos para validar el ingreso, crear, actualizar, buscar y eliminar personas en el sistema.
 */
public class Logica_Persona {
    PersistenciaController controladora = new PersistenciaController();
    PDFService pdfService= new PDFService() ;

    /**
     * Valida las credenciales de un usuario para permitir el ingreso.
     * Verifica el documento, tipo de documento, clave y estado del usuario.
     *
     * @param documento     El número de documento del usuario.
     * @param tipoDocumento El tipo de documento asociado al usuario.
     * @param clave         La clave ingresada por el usuario.
     * @return Un objeto {@link ResultadoOperacion} que indica si la validación fue exitosa o no.
     */
    public ResultadoOperacion validarIngreso(int documento, int tipoDocumento, String clave) {
        PasswordService passwordService = new PasswordService();
        System.out.println("Iniciando validación de ingreso para documento: " + documento);

        // Obtener el LoginDTO correspondiente al documento
        Persona persona = controladora.buscarPersonaDocumento(documento);
        // Verificar si se encontró el documento
        if (persona == null) {
            System.err.println("Error: No se encontraron usuarios para el documento: " + documento);
            return new ResultadoOperacion(false, "No se encontraron usuarios para el documento.");
        }
        System.out.println("Documento encontrado, continuando validación...");

        // Validar las credenciales: tipo de documento, clave y rol
        if (persona.getClave() != null) {
            boolean tipoDocCoincide = persona.getTipoDocumento().getId() == tipoDocumento;
            boolean claveCoincide = passwordService.verificarContrasena(clave,persona.getClave());


            System.out.println("Resultado de validaciones:");
            System.out.println("Tipo de documento coincide: " + tipoDocCoincide);
            System.out.println("Clave coincide: " + claveCoincide);


            if (tipoDocCoincide && claveCoincide) {
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

    /**
     * Crea un nuevo usuario (persona) en la base de datos con una clave encriptada.
     *
     * @param perso    El objeto {@link Persona} que contiene los datos del nuevo usuario.
     * @param password La clave en texto plano que se debe encriptar y almacenar.
     * @return Un objeto {@link ResultadoOperacion} que indica si la creación fue exitosa o no.
     */
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

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param user El objeto {@link Persona} que contiene los datos actualizados del usuario.
     * @return Un objeto {@link ResultadoOperacion} que indica si la actualización fue exitosa o no.
     */
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
    /**
     * Busca una persona por su ID.
     *
     * @param id El ID de la persona que se desea buscar.
     * @return El objeto {@link Persona} correspondiente al ID proporcionado, o {@code null} si no se encuentra.
     */

    public Persona buscarpersonaPorId(int id) {

        Persona persona = controladora.buscarpersona(id);

        return persona;
    }
    /**
     * Busca una persona por su correo electrónico.
     *
     * @param email El correo electrónico de la persona que se desea buscar.
     * @return El objeto {@link Persona} correspondiente al correo proporcionado, o {@code null} si no se encuentra.
     */
    public Persona buscarPersonaPorCorreo(String email) {
        return controladora.buscarPersonaPorCorreo(email);
    }

    /**
     * Busca una persona por su número de documento.
     *
     * @param documento El número de documento de la persona.
     * @return El objeto {@link Persona} correspondiente al documento proporcionado, o {@code null} si no se encuentra.
     */
    public Persona buscarPersonaConDocumento(int documento) {
        try{
            return controladora.buscarPersonaDocumento(documento);
        }catch (Exception e){
            System.err.println("Error al obtener la persona: " + e.getMessage());
            return null;
        }
    }
    /**
     * Busca un vehículo en la lista de vehículos de una persona por su ID.
     *
     * @param persona    La persona propietaria de los vehículos.
     * @param idVehiculo El ID del vehículo a buscar.
     * @return El objeto {@link TbVehiculo} correspondiente al ID del vehículo, o {@code null} si no se encuentra.
     */
    public TbVehiculo buscarVehiculoPorId(Persona persona, int idVehiculo) {
        if (persona == null || persona.getVehiculos() == null) {
            return null;
        }
        return persona.getVehiculos().stream()
                .filter(vehiculo -> vehiculo.getId_vehiculo() == idVehiculo)
                .findFirst()
                .orElse(null);
    }

    /**
     * Elimina un usuario (persona) en la base de datos por su ID.
     *
     * @param id El ID del usuario que se desea eliminar.
     * @return Un objeto {@link ResultadoOperacion} que indica si la eliminación fue exitosa o no.
     * @throws Exception Si ocurre un error durante la eliminación.
     */
    public ResultadoOperacion borrarUsuario(int id) throws Exception {
        try{
            controladora.eliminarUsuario(id);
            return new ResultadoOperacion(true, "Usuario eliminado correctamente");
        }catch (Exception e){
            System.err.println("Error al eliminar la persona: " + e.getMessage());
            return new ResultadoOperacion(false, "Error al eliminar al usuario");
        }
    }

    /**
     * Busca un colaborador por su documento y verifica que su rol sea "colaborador" (rol con ID 1).
     *
     * @param documento El número de documento del colaborador.
     * @return Un objeto {@link Persona} si el colaborador es encontrado y su rol coincide, de lo contrario {@code null}.
     */
    public Persona obtenerColaborador(int documento) {

        Persona Colaborador = buscarPersonaConDocumento(documento);

        if (Colaborador != null && Colaborador.getRol().getId() == 1) {

            return Colaborador;
        }

        return null;
    }


    /**
     * Cambia el estado de un usuario (ACTIVO o INACTIVO). Si el usuario está en un estacionamiento, no se puede desactivar.
     *
     * @param id El ID del usuario cuyo estado se va a cambiar.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
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

    /**
     * Verifica si un usuario está ocupando un espacio de estacionamiento.
     *
     * @param iduser El ID del usuario.
     * @return {@code true} si el usuario está ocupando un espacio, {@code false} en caso contrario.
     */
    public boolean UsuarioEnEstacionamiento(int iduser) {
        Persona usuarioEnEspacios = controladora.buscarUsuarioEnEspacios(iduser);

        return usuarioEnEspacios != null;
    }

    /**
     * Obtiene una lista de usuarios paginada.
     *
     * @param numeroPagina El número de la página que se desea obtener.
     * @return Un mapa con los usuarios paginados y la información de la paginación.
     */
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
    /**
     * Obtiene una lista paginada de usuarios activos.
     *
     * @param numeroPagina El número de la página que se desea obtener.
     * @return Un mapa con los usuarios activos paginados y la información de la paginación.
     */

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
    /**
     * Obtiene una lista paginada de usuarios inactivos.
     *
     * @param numeroPagina El número de la página que se desea obtener.
     * @return Un mapa con los usuarios inactivos paginados y la información de la paginación.
     */
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

    /**
     * Genera un informe en PDF de un usuario específico.
     *
     * @param idUsuario El ID del usuario para el cual se desea generar el informe.
     * @return El código del informe generado.
     */
    public String generarInforme(int idUsuario) {
        // Buscar el usuario por su ID
        Persona usuario = buscarpersonaPorId(idUsuario);
        // Obtener los vehículos del usuario
        Set<TbVehiculo> vehiculos = usuario.getVehiculos();
        // Convertir a lista para facilitar el manejo
        List<TbVehiculo> listaVehiculos = new ArrayList<>(vehiculos);
        try{
       TbInformesUsuarios informe = pdfService.generarPDF(usuario, listaVehiculos);
            controladora.subirInforme(informe);
            String codigo = informe.getCodigoInforme();
            System.out.println(codigo);
            return codigo;
        }catch (Exception e){
            System.err.println("Hubo un error al crear el pdf "+ e.getMessage());
            e.printStackTrace();
            return null;
        }

    }
    /**
     * Busca un informe de usuario por su código.
     *
     * @param informeCode El código del informe a buscar.
     * @return El objeto {@link TbInformesUsuarios} correspondiente al código, o {@code null} si no se encuentra.
     */
    public TbInformesUsuarios findInformeByCode(String informeCode) {

        return controladora.buscarInformePorCodigo(informeCode);

    }

    /**
     * Actualiza la contraseña de un usuario.
     *
     * @param usuario        El objeto {@link Persona} cuyo password se va a actualizar.
     * @param nuevaPassword  La nueva contraseña en texto plano.
     * @return Un objeto {@link ResultadoOperacion} que indica si la operación fue exitosa o no.
     */
    public ResultadoOperacion actualizarPassword(Persona usuario, String nuevaPassword) {
        try{
            PasswordService passwordService = new PasswordService();
            String clave_encriptada= passwordService.encriptarContrasena(nuevaPassword);
            System.out.println("Se inicia el cambio de contraseña");
            System.out.println("Clave encriptada: "+ clave_encriptada );
            usuario.setClave(clave_encriptada);
            controladora.EditarPersona(usuario);
            return  new ResultadoOperacion(true, "La contraseña ha sido cambiada");
        } catch (Exception e){
            System.out.println("Error al cambiar contraseña: " + e.getMessage());
            return  new ResultadoOperacion(false, "Error al cambiar la contraseña");
        }
    }


}
