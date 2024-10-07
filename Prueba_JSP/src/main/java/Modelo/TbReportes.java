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
    private String nombre_reporte;
    private String descripcion_reporte;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_reporte")
    private TipoReporte tipoReporte;

    // Relación ManyToOne con el Aprendiz
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_aprendiz", referencedColumnName = "id")
    private Persona aprendiz;

    // Relación ManyToOne con el Gestor (Colaborador)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_gestor", referencedColumnName = "id")
    private Persona gestor;
    // Campo relacionado con la placa del vehículo (puede ser string si no hay relación directa)
    private String placaVehiculo; // Relación con el vehículo específico

    public TbReportes() {
    }

    public TbReportes(Integer id_reporte, Date fecha_reporte, String nombre_reporte, String descripcion_reporte, TipoReporte tipoReporte, Persona aprendiz, Persona gestor, String placaVehiculo) {
        this.id_reporte = id_reporte;
        this.fecha_reporte = fecha_reporte;
        this.nombre_reporte = nombre_reporte;
        this.descripcion_reporte = descripcion_reporte;
        this.tipoReporte = tipoReporte;
        this.aprendiz = aprendiz;
        this.gestor = gestor;
        this.placaVehiculo = placaVehiculo;
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

    public Persona getAprendiz() {
        return aprendiz;
    }

    public void setAprendiz(Persona aprendiz) {
        this.aprendiz = aprendiz;
    }

    public Persona getGestor() {
        return gestor;
    }

    public void setGestor(Persona gestor) {
        this.gestor = gestor;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }
}


