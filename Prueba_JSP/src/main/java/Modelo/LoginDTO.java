package Modelo;

public class LoginDTO {
    private int documento;
    private String TipoDocumento;
    private String clave;

    // Constructor, getters y setters
    // Constructor

    public LoginDTO(int documento, String tipoDocumento, String clave) {
        this.documento = documento;
        TipoDocumento = tipoDocumento;
        this.clave = clave;
    }

    public int getDocumento() {
        return documento;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public String getTipoDocumento() {
        return TipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        TipoDocumento = tipoDocumento;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
