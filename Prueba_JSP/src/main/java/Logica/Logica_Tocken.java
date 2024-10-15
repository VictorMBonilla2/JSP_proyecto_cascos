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

public class Logica_Tocken {
    PersistenciaController controladora = new PersistenciaController();
    Logica_Persona logicaPersona = new Logica_Persona();
    PasswordService passwordService = new PasswordService();

    public String generarTokenRecuperacion(String email) {
        System.out.println("Intentando obtener usuario");
        Persona usuario = logicaPersona.buscarPersonaPorCorreo(email);
        if (usuario == null || usuario.getEstadoUsuario().equals(EstadoUsuario.INACTIVO)) {
            System.out.println("No se encontro el usuario con ese correo o esta inactivo");
            return null;
        }
        System.out.println("Usuario obtenido");
        try{
            TbRecuperacion recuperacion = new TbRecuperacion();
            String token = UUID.randomUUID().toString();
            recuperacion.setPersona(usuario);
            recuperacion.setTokenRecuperacion(passwordService.encriptarContrasena(token));
            recuperacion.setFechaExpiracionToken(new Date(System.currentTimeMillis() + 3600000));
            recuperacion.setTokenUsado(false);
            controladora.crearToken(recuperacion);
            return token;
        }catch (Exception e){
            System.err.println("Hubo un error al crear el token de recuperacíon" + e.getMessage());
            return null;
        }

    }


    public void enviarCorreoRecuperacion(String email, String token) {
        System.out.println("Se inicia la creacion del correo");
        try {
            EmailService emailService = new EmailService();
            emailService.enviarCorreoRecuperacion(email, token);
        }catch (Exception e){
            System.err.println("Hubo un error al crear el correo: "+ e.getMessage());
            e.getStackTrace();
        }

    }

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

    public List<TbRecuperacion> buscarTokenActivos(){
        return controladora.buscarRecuperacionesActivas();
    }

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

    public void invalidarToken(TbRecuperacion tokenObj) {

        tokenObj.setTokenUsado(true);
        try{
            controladora.ActualizarToken(tokenObj);
        }catch (Exception e){
            System.err.println("Hubo un error al actualizar el token: " + e.getMessage());
        }

    }
}
