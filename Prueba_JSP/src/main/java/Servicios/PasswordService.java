package Servicios;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Servicio para la gestión de contraseñas, proporcionando métodos para encriptarlas y verificarlas.
 * Utiliza BCrypt para la encriptación de contraseñas.
 */
public class PasswordService {

    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructor que inicializa el codificador de contraseñas BCrypt.
     */
    public PasswordService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Encripta una contraseña proporcionada utilizando BCrypt.
     *
     * @param password La contraseña en texto plano que se desea encriptar.
     * @return La contraseña encriptada.
     */
    public String encriptarContrasena(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Verifica si una contraseña en texto plano coincide con una contraseña encriptada.
     *
     * @param rawPassword    La contraseña en texto plano que se desea verificar.
     * @param hashedPassword La contraseña previamente encriptada con la que se desea comparar.
     * @return true si la contraseña coincide, false en caso contrario.
     */
    public boolean verificarContrasena(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}
