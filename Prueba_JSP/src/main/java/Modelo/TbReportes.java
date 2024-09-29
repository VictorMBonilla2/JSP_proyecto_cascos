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

    private int documentoAprendiz;
    private int documentoColaborador;

    private String placaVehiculo; // Relación con el vehículo específico

    public TbReportes() {
    }

    public TbReportes(Integer id_reporte, Date fecha_reporte, String nombre_reporte, String descripcion_reporte, TipoReporte tipoReporte, int documentoAprendiz, int documentoColaborador, String placaVehiculo) {
        this.id_reporte = id_reporte;
        this.fecha_reporte = fecha_reporte;
        this.nombre_reporte = nombre_reporte;
        this.descripcion_reporte = descripcion_reporte;
        this.tipoReporte = tipoReporte;
        this.documentoAprendiz = documentoAprendiz;
        this.documentoColaborador = documentoColaborador;
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

    public int getDocumentoAprendiz() {
        return documentoAprendiz;
    }

    public void setDocumentoAprendiz(int documentoAprendiz) {
        this.documentoAprendiz = documentoAprendiz;
    }

    public int getDocumentoColaborador() {
        return documentoColaborador;
    }

    public void setDocumentoColaborador(int documentoColaborador) {
        this.documentoColaborador = documentoColaborador;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }
}


