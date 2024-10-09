package Modelo;

import Modelo.enums.EstadoUsuario;
import jakarta.persistence.*;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity(name = "tb_persona")
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true, nullable = false)
    private int documento;

    private String nombre;
    private String apellido;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipodocumento")
    private TbTipoDocumento tipoDocumento;
    private String correo;

    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    private String clave;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", nullable = false)
    private Roles rol;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoUsuario estadoUsuario;

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TbVehiculo> vehiculos = new LinkedHashSet<>();

    public Persona() {
    }

    public Persona(int id, int documento, String nombre, String apellido, TbTipoDocumento tipoDocumento, String correo, Date fechaNacimiento, String clave, Roles rol, EstadoUsuario estadoUsuario, Set<TbVehiculo> vehiculos) {
        this.id = id;
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDocumento = tipoDocumento;
        this.correo = correo;
        this.fechaNacimiento = fechaNacimiento;
        this.clave = clave;
        this.rol = rol;
        this.estadoUsuario = estadoUsuario;
        this.vehiculos = vehiculos;
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

    public TbTipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TbTipoDocumento tipoDocumento) {
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

    public Roles getRol() {
        return rol;
    }

    public void setRol(Roles rol) {
        this.rol = rol;
    }

    public EstadoUsuario getEstadoUsuario() {
        return estadoUsuario;
    }

    public void setEstadoUsuario(EstadoUsuario estadoUsuario) {
        this.estadoUsuario = estadoUsuario;
    }

    public Set<TbVehiculo> getVehiculos() {
        return vehiculos;
    }

    public void setVehiculos(Set<TbVehiculo> vehiculos) {
        this.vehiculos = vehiculos;
    }
}
