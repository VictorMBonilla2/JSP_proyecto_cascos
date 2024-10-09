package Modelo;

import Modelo.enums.EstadoEspacio;
import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(name = "tb_espacio")
public class TbEspacio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_espacio", nullable = false)
    private Integer id_espacio;
    private String nombre;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vehiculo_fk")
    private TbVehiculo vehiculo;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "documento_aprendiz")
    private Persona persona;
    private Date hora_entrada;
    private Integer cantidad_cascos;
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_espacio")
    private EstadoEspacio estado_espacio;
    // Foránea de la clase Casillero
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_sector", nullable = false)
    private TbSectores sector;



    public TbEspacio() {
    }

    public TbEspacio(Integer id_espacio, String nombre, TbVehiculo vehiculo, Persona persona, Date hora_entrada, Integer cantidad_cascos, EstadoEspacio estado_espacio, TbSectores sector) {
        this.id_espacio = id_espacio;
        this.nombre = nombre;
        this.vehiculo = vehiculo;
        this.persona = persona;
        this.hora_entrada = hora_entrada;
        this.cantidad_cascos = cantidad_cascos;
        this.estado_espacio = estado_espacio;
        this.sector = sector;
    }

    public Integer getId_espacio() {
        return id_espacio;
    }

    public void setId_espacio(Integer id_espacio) {
        this.id_espacio = id_espacio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TbVehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(TbVehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Date getHora_entrada() {
        return hora_entrada;
    }

    public void setHora_entrada(Date hora_entrada) {
        this.hora_entrada = hora_entrada;
    }

    public Integer getCantidad_cascos() {
        return cantidad_cascos;
    }

    public void setCantidad_cascos(Integer cantidad_cascos) {
        this.cantidad_cascos = cantidad_cascos;
    }

    public EstadoEspacio getEstado_espacio() {
        return estado_espacio;
    }

    public void setEstado_espacio(EstadoEspacio estado_espacio) {
        this.estado_espacio = estado_espacio;
    }

    public TbSectores getSector() {
        return sector;
    }

    public void setSector(TbSectores sector) {
        this.sector = sector;
    }
}



