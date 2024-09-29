package Utilidades;

public class ResultadoOperacion {
    private boolean exito;
    private String mensaje;

    // Constructor
    public ResultadoOperacion(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    // Getter para 'exito'
    public boolean isExito() {
        return exito;
    }

    // Getter para 'mensaje'
    public String getMensaje() {
        return mensaje;
    }

    // Setter para 'exito'
    public void setExito(boolean exito) {
        this.exito = exito;
    }

    // Setter para 'mensaje'
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
