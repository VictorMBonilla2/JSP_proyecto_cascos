package DTO;

public class LoginDTO {
    private int documento;
    private String TipoDocumento;
    private String clave;
    private String rol;

    // Constructor, getters y setters
    // Constructor


    public LoginDTO(int documento, String tipoDocumento, String clave, String rol) {
        this.documento = documento;
        TipoDocumento = tipoDocumento;
        this.clave = clave;
        this.rol = rol;
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

    public String getRol() {
        return rol;

    }
    public void setRol(String rol) {
        this.rol = rol;
    }

}
