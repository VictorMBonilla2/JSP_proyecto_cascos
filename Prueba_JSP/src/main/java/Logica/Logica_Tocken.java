package Logica;

import Controlador.PersistenciaController;
import Modelo.Persona;
import Modelo.TbRecuperacion;
import Modelo.enums.EstadoUsuario;
import Servicios.EmailService;
import Servicios.PasswordService;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Esta clase gestiona la lógica relacionada con los tokens de recuperación de contraseña.
 * Proporciona métodos para generar, enviar, validar y desactivar tokens.
 */
public class Logica_Tocken {

    private final PersistenciaController controladora = new PersistenciaController();
    private final Logica_Persona logicaPersona = new Logica_Persona();
    private final PasswordService passwordService = new PasswordService();

    /**
     * Genera un token de recuperación de contraseña para el usuario con el correo electrónico proporcionado.
     *
     * @param email El correo electrónico del usuario que solicita la recuperación de contraseña.
     * @return El token de recuperación generado, o {@code null} si no se encuentra el usuario o si está inactivo.
     */
    public String generarTokenRecuperacion(String email) {
        System.out.println("Intentando obtener usuario");
        Persona usuario = logicaPersona.buscarPersonaPorCorreo(email);
        if (usuario == null || usuario.getEstadoUsuario().equals(EstadoUsuario.INACTIVO)) {
            System.out.println("No se encontró el usuario con ese correo o está inactivo");
            return null;
        }
        System.out.println("Usuario obtenido");
        try {
            TbRecuperacion recuperacion = new TbRecuperacion();
            String token = UUID.randomUUID().toString();
            recuperacion.setPersona(usuario);
            recuperacion.setTokenRecuperacion(passwordService.encriptarContrasena(token));
            recuperacion.setFechaExpiracionToken(new Date(System.currentTimeMillis() + 3600000));  // Expira en 1 hora
            recuperacion.setTokenUsado(false);
            controladora.crearToken(recuperacion);
            return token;
        } catch (Exception e) {
            System.err.println("Hubo un error al crear el token de recuperación: " + e.getMessage());
            return null;
        }
    }

    /**
     * Envía un correo de recuperación de contraseña con el token generado al usuario.
     *
     * @param email El correo electrónico del destinatario.
     * @param token El token de recuperación que debe enviarse.
     */
    public void enviarCorreoRecuperacion(String email, String token) {
        System.out.println("Se inicia la creación del correo");
        try {
            EmailService emailService = new EmailService();
            emailService.enviarCorreoRecuperacion(email, token);
        } catch (Exception e) {
            System.err.println("Hubo un error al crear el correo: " + e.getMessage());
        }
    }

    /**
     * Verifica si un token de recuperación es válido (no ha expirado y no ha sido usado).
     *
     * @param recuperacion El objeto {@link TbRecuperacion} que contiene el token a validar.
     * @return {@code true} si el token es válido, {@code false} si ha expirado o ya fue usado.
     */
    public boolean isTokenValid(TbRecuperacion recuperacion) {
        System.out.println("Se intenta validar el token.");
        Date now = new Date();

        // Verifica si el token ha expirado y si no ha sido usado
        boolean noHaExpirado = recuperacion.getFechaExpiracionToken().after(now);
        boolean noHaSidoUsado = !recuperacion.isTokenUsado();
        System.out.println("No Expirado? " + noHaExpirado);
        System.out.println("No Usado? " + noHaSidoUsado);
        return noHaExpirado && noHaSidoUsado;
    }

    /**
     * Busca todos los tokens de recuperación que están activos (no han expirado ni sido usados).
     *
     * @return Una lista de objetos {@link TbRecuperacion} con los tokens activos.
     */
    public List<TbRecuperacion> buscarTokenActivos() {
        return controladora.buscarRecuperacionesActivas();
    }

    /**
     * Busca un token de recuperación específico encriptado.
     *
     * @param token El token encriptado que se desea buscar.
     * @return El objeto {@link TbRecuperacion} correspondiente al token, o {@code null} si no se encuentra.
     */
    public TbRecuperacion buscarPorToken(String token) {
        PasswordService passwordService = new PasswordService();
        List<TbRecuperacion> listaActivos = buscarTokenActivos();

        for (TbRecuperacion recuperacion : listaActivos) {
            if (passwordService.verificarContrasena(token, recuperacion.getTokenRecuperacion())) {
                return recuperacion;  // Si el token coincide, retorna la recuperación
            }
        }
        return null;
    }

    /**
     * Invalida un token de recuperación marcándolo como usado.
     *
     * @param tokenObj El objeto {@link TbRecuperacion} que contiene el token a invalidar.
     */
    public void invalidarToken(TbRecuperacion tokenObj) {
        tokenObj.setTokenUsado(true);
        try {
            controladora.ActualizarToken(tokenObj);
        } catch (Exception e) {
            System.err.println("Hubo un error al actualizar el token: " + e.getMessage());
        }
    }
}

