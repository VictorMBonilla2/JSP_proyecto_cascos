package DTO;

public class LoginDTO {
    private int documento;
    private String tipoDocumento;
    private String clave;
    private int rolId; // Cambiado a int para almacenar el ID del rol

    // Constructor
    public LoginDTO(int documento, String tipoDocumento, String clave, int rolId) {
        this.documento = documento;
        this.tipoDocumento = tipoDocumento;
        this.clave = clave;
        this.rolId = rolId;
    }

    public int getDocumento() {
        return documento;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public int getRolId() {
        return rolId;
    }

    public void setRolId(int rolId) {
        this.rolId = rolId;
    }
}
