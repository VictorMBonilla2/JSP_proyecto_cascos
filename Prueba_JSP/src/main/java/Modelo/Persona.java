package Modelo;

import jakarta.persistence.*;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<TbVehiculo> vehiculos = new LinkedHashSet<>();
//  un salt para cifrado
    // Getters y setters

    public Persona() {

    }

    public Persona(int documento, String nombre, String apellido, String tipoDocumento, String correo, Date fechaNacimiento, String clave, String rol, Set<TbVehiculo> vehiculos) {
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDocumento = tipoDocumento;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.clave = clave;
        this.rol = rol;
        this.vehiculos = vehiculos;
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

    public Set<TbVehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(Set<TbVehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }
}
