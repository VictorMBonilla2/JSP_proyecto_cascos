package Modelo;

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
    private String tipo_reporte;
    private String estado_reporte;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aprendiz_fk", nullable = false) // Clave foránea a Persona (aprendiz)
    private Persona aprendiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_gestor_fk", nullable = false) // Clave foránea a Persona (gestor)
    private Persona colaborador;


    public TbReportes() {
    }

    public TbReportes(Integer id_reporte, Date fecha_reporte, String nombre_reporte, String descripcion_reporte, String tipo_reporte, String estado_reporte, Persona aprendiz, Persona colaborador) {
        this.id_reporte = id_reporte;
        this.fecha_reporte = fecha_reporte;
        this.nombre_reporte = nombre_reporte;
        this.descripcion_reporte = descripcion_reporte;
        this.tipo_reporte = tipo_reporte;
        this.estado_reporte = estado_reporte;
        this.aprendiz = aprendiz;
        this.colaborador = colaborador;
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

    public String getTipo_reporte() {
        return tipo_reporte;
    }

    public void setTipo_reporte(String tipo_reporte) {
        this.tipo_reporte = tipo_reporte;
    }

    public String getEstado_reporte() {
        return estado_reporte;
    }

    public void setEstado_reporte(String estado_reporte) {
        this.estado_reporte = estado_reporte;
    }

    public Persona getAprendiz() {
        return aprendiz;
    }

    public void setAprendiz(Persona aprendiz) {
        this.aprendiz = aprendiz;
    }

    public Persona getColaborador() {
        return colaborador;
    }

    public void setColaborador(Persona colaborador) {
        this.colaborador = colaborador;
    }
}


