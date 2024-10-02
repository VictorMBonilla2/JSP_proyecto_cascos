package DTO;

public class LoginDTO {
    private int id;
    private int documento;
    private int tipoDocumento;
    private String clave;
    private int rolId; // Cambiado a int para almacenar el ID del rol

    public LoginDTO() {
    }

    public LoginDTO(int id, int documento, int tipoDocumento, String clave, int rolId) {
        this.id = id;
        this.documento = documento;
        this.tipoDocumento = tipoDocumento;
        this.clave = clave;
        this.rolId = rolId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDocumento() {
        return documento;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public int getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(int tipoDocumento) {
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