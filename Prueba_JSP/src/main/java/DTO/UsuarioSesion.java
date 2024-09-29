package DTO;

public class UsuarioSesion {
    private Integer documento;
    private String rol;

    public UsuarioSesion(Integer documento, String rol) {
        this.documento = documento;
        this.rol = rol;
    }

    public Integer getDocumento() {
        return documento;
    }

    public String getRol() {
        return rol;
    }
}
