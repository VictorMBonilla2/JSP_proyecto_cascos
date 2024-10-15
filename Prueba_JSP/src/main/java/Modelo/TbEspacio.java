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

    @Column( nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vehiculo_fk")
    private TbVehiculo vehiculo;

    //Pasar a hora-fecha
    private Date hora_entrada;

    private Integer cantidad_cascos;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_espacio", nullable = false)
    private EstadoEspacio estado_espacio;

    // Foránea de la clase Sector
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_sector", nullable = false)
    private TbSectores sector;



    public TbEspacio() {
    }

    public TbEspacio(Integer id_espacio, String nombre, TbVehiculo vehiculo, Date hora_entrada, Integer cantidad_cascos, EstadoEspacio estado_espacio, TbSectores sector) {
        this.id_espacio = id_espacio;
        this.nombre = nombre;
        this.vehiculo = vehiculo;
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



