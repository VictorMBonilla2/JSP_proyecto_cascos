package Clases;

import java.util.Date;

public class Persona {
    private String nombre;
    private String apellido;
    private String TipoDocumento;
    private int documento;
    private String correo;
    private Date fechaNacimiento;

    public Persona() {}

    public Persona(String nombre, String apellido, String tipoDocumento, int documento, String correo, Date fechaNacimiento) {
        this.nombre = nombre;
        this.apellido = apellido;
        TipoDocumento = tipoDocumento;
        this.documento = documento;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTipoDocumento() {
        return TipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        TipoDocumento = tipoDocumento;
    }

    public int getDocumento() {
        return documento;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}
