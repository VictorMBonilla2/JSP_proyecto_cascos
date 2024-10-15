package Modelo;

import Modelo.enums.TipoReporte;
import jakarta.persistence.*;

import java.util.Date;

@Entity (name = "tb_reportes")
public class TbReportes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte", nullable = false)
    private Integer id_reporte;
    private Date fecha_reporte;

    @Column(nullable = false)
    private String nombre_reporte;

    @Column(nullable = false)
    private String descripcion_reporte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_espacio", nullable = false)
    private TbEspacio espacio;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_reporte")
    private TipoReporte tipoReporte;

    // Cambiar la relación a TbVehiculo en lugar de Persona
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private TbVehiculo vehiculo;

    // Relación ManyToOne con el Gestor (Colaborador)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_gestor")
    private Persona gestor;

    public TbReportes() {
    }

    public TbReportes(Integer id_reporte, Date fecha_reporte, String nombre_reporte, String descripcion_reporte, TbEspacio espacio, TipoReporte tipoReporte, TbVehiculo vehiculo, Persona gestor) {
        this.id_reporte = id_reporte;
        this.fecha_reporte = fecha_reporte;
        this.nombre_reporte = nombre_reporte;
        this.descripcion_reporte = descripcion_reporte;
        this.espacio = espacio;
        this.tipoReporte = tipoReporte;
        this.vehiculo = vehiculo;
        this.gestor = gestor;
    }

    public Integer getId_reporte() {
        return id_reporte;
    }

    public void setId_reporte(Integer id_reporte) {
        this.id_reporte = id_reporte;
    }

    public Date getFecha_reporte() {
        return fecha_reporte;
    }

    public void setFecha_reporte(Date fecha_reporte) {
        this.fecha_reporte = fecha_reporte;
    }

    public String getNombre_reporte() {
        return nombre_reporte;
    }

    public void setNombre_reporte(String nombre_reporte) {
        this.nombre_reporte = nombre_reporte;
    }

    public String getDescripcion_reporte() {
        return descripcion_reporte;
    }

    public void setDescripcion_reporte(String descripcion_reporte) {
        this.descripcion_reporte = descripcion_reporte;
    }

    public TipoReporte getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(TipoReporte tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public TbVehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(TbVehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public Persona getGestor() {
        return gestor;
    }

    public void setGestor(Persona gestor) {
        this.gestor = gestor;
    }

    public TbEspacio getEspacio() {
        return espacio;
    }

    public void setEspacio(TbEspacio espacio) {
        this.espacio = espacio;
    }
}


