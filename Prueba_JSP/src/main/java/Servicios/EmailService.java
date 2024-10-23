package Servicios;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * Servicio para enviar correos electrónicos relacionados con la recuperación de contraseñas.
 * Utiliza un servidor SMTP (como Gmail) para enviar correos de recuperación de contraseñas.
 */
public class EmailService {

    private final String username = "";  // Dirección de correo electrónico del remitente
    private final String password = "";  // Contraseña del correo electrónico del remitente

    /**
     * Envía un correo electrónico de recuperación de contraseña al destinatario proporcionado con un enlace de token.
     *
     * @param destinatario Dirección de correo electrónico del destinatario.
     * @param token        El token de recuperación de contraseña que debe incluirse en el cuerpo del correo.
     */
    public void enviarCorreoRecuperacion(String destinatario, String token) {
        String host = "smtp.gmail.com";  // Configuración del servidor SMTP para Gmail
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");  // Habilitar autenticación SMTP
        properties.put("mail.smtp.starttls.enable", "true");  // Habilitar STARTTLS
        properties.put("mail.smtp.host", host);  // Servidor SMTP de Gmail
        properties.put("mail.smtp.port", "587");  // Puerto SMTP seguro para STARTTLS

        // Crear una sesión SMTP autenticada
        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Crear el mensaje de correo
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(username));  // Dirección de correo del remitente
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));  // Destinatarios del correo
            mensaje.setSubject("Recuperación de Contraseña");  // Asunto del correo

            // Cuerpo del correo
            String cuerpo = "Hola,\n\nPara recuperar tu contraseña, por favor usa el siguiente enlace:\n" + token + "\n\nSaludos, Equipo de soporte.";
            mensaje.setText(cuerpo);

            // Enviar el correo
            Transport.send(mensaje);
            System.out.println("Correo enviado con éxito!");

        } catch (MessagingException e) {
            // Manejar cualquier error durante el envío del correo
            throw new RuntimeException(e);
        }
    }
}

