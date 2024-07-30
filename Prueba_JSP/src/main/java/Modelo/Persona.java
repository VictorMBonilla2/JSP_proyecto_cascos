package Modelo;

import jakarta.persistence.*;

import java.util.Date;
@Entity(name = "tb_persona")
public class Persona {
    @Id
    private int documento;

    private String nombre;
    private String apellido;
    private String tipoDocumento;
    private String correo;

    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    private String clave;
    private String rol;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehiculo_FK")
    private TbVehiculo vehiculo;
//  un salt para cifrado
    // Getters y setters

    public Persona() {

    }

    public Persona(int documento, String nombre, String apellido, String tipoDocumento, String correo, Date fechaNacimiento, String clave, String rol, TbVehiculo vehiculo) {
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDocumento = tipoDocumento;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.clave = clave;
        this.rol = rol;
        this.vehiculo = vehiculo;
    }

    public int getDocumento() {
        return documento;
    }

    public void setDocumento(int documento) {
        this.documento = documento;
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
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
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

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public TbVehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(TbVehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }
}
