package Modelo;

import jakarta.persistence.*;

import java.util.Date;
@Entity
@Table(name = "tb_registro")
public class TbRegistro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_regsitro", nullable = false)
    private Integer id_registro;
    private Date fecha_registro;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_espacio_fk", nullable = false) // Nombre de la columna de la clave foránea en esta entidad
    private TbEspacio espacio;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehiculo_fk", nullable = false) // Nombre de la columna de la clave foránea en esta entidad
    private TbEspacio vehiculo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_persona_fk", nullable = false) // Nombre de la columna de la clave foránea en esta entidad
    private TbEspacio persona;

    public TbRegistro() {
    }

    public TbRegistro(Integer id_registro, Date fecha_registro, TbEspacio espacio, TbEspacio vehiculo, TbEspacio persona) {
        this.id_registro = id_registro;
        this.fecha_registro = fecha_registro;
        this.espacio = espacio;
        this.vehiculo = vehiculo;
        this.persona = persona;
    }

    public Integer getId_registro() {
        return id_registro;
    }

    public void setId_registro(Integer id_registro) {
        this.id_registro = id_registro;
    }

    public Date getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_registro(Date fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public TbEspacio getEspacio() {
        return espacio;
    }

    public void setEspacio(TbEspacio espacio) {
        this.espacio = espacio;
    }

    public TbEspacio getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(TbEspacio vehiculo) {
        this.vehiculo = vehiculo;
    }

    public TbEspacio getPersona() {
        return persona;
    }

    public void setPersona(TbEspacio persona) {
        this.persona = persona;
    }
}
