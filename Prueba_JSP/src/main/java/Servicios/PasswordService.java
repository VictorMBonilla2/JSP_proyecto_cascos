package Servicios;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordService {

    private BCryptPasswordEncoder passwordEncoder;

    // Constructor para inicializar BCryptPasswordEncoder
    public PasswordService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // Método para encriptar la contraseña
    public String encriptarContrasena(String password) {
        return passwordEncoder.encode(password);
    }

    // Método para verificar si la contraseña ingresada coincide con la encriptada
    public boolean verificarContrasena(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
