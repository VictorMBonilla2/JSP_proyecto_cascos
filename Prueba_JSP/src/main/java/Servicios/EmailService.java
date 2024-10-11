package Servicios;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {

    private final String username = "";
    private final String password = "";

    public void enviarCorreoRecuperacion(String destinatario, String token) {
        System.out.println("Se inicia la creacion del correo 1");
        String host = "smtp.gmail.com";  // Si utilizas Gmail como servidor SMTP
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(username));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject("Recuperación de Contraseña");

            String cuerpo = "Hola,\n\nPara recuperar tu contraseña, por favor usa el siguiente enlace:\n" + token + "\n\nSaludos, Equipo de soporte.";
            mensaje.setText(cuerpo);

            Transport.send(mensaje);
            System.out.println("Correo enviado con éxito!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
