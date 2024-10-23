package Utilidades;

/**
 * Clase que representa el resultado de una operación, indicando si fue exitosa y proporcionando un mensaje asociado.
 */
public class ResultadoOperacion {
    // Indica si la operación fue exitosa
    private boolean exito;

    // Mensaje relacionado con el resultado de la operación
    private String mensaje;

    /**
     * Constructor que inicializa el resultado de la operación con el estado de éxito y un mensaje.
     *
     * @param exito   Indica si la operación fue exitosa.
     * @param mensaje Mensaje relacionado con el resultado de la operación.
     */
    public ResultadoOperacion(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    /**
     * Verifica si la operación fue exitosa.
     *
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    public boolean isExito() {
        return exito;
    }

    /**
     * Obtiene el mensaje relacionado con el resultado de la operación.
     *
     * @return El mensaje de la operación.
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Establece el estado de éxito de la operación.
     *
     * @param exito true si la operación fue exitosa, false en caso contrario.
     */
    public void setExito(boolean exito) {
        this.exito = exito;
    }

    /**
     * Establece el mensaje relacionado con el resultado de la operación.
     *
     * @param mensaje El mensaje de la operación.
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}

