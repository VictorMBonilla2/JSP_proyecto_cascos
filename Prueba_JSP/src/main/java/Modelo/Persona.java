package Modelo;

import jakarta.persistence.*;

import java.util.Date;
@Entity
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nombre;
    private String apellido;
    private String TipoDocumento;
    private int documento;
    private String correo;
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    private String clave;
    private String rol;


    public Persona() {
    }


    public Persona(int id, String nombre, String apellido, String tipoDocumento, int documento, String correo, Date fechaNacimiento, String clave, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        TipoDocumento = tipoDocumento;
        this.documento = documento;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.clave = clave;
        this.rol = rol;
    }


    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
