package Modelo;

import Servlets.Anotaciones;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Date;
@Entity
@Table(name = "tb_registro")
public class TbRegistro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro", nullable = false)

    @Anotaciones.PropertyName("ID del Registro")
    private Integer id_registro;

    @Anotaciones.PropertyName("Fecha del Registro")
    private LocalDateTime fecha_registro;

    @Anotaciones.PropertyName("Espacio")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_espacio_fk", nullable = false) // Clave foránea a TbEspacio
    private TbEspacio espacio;

    @Anotaciones.PropertyName("Placa del Vehiculo")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vehiculo_fk", nullable = false) // Clave foránea a TbVehiculo
    private TbVehiculo vehiculo;

    @Anotaciones.PropertyName("Documento del Aprendiz")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aprendiz_fk", nullable = false) // Clave foránea a Persona (aprendiz)
    private Persona aprendiz;

    @Anotaciones.PropertyName("Documento del Gestor")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_colaborador_fk", nullable = false) // Clave foránea a Persona (colaborador)
    private Persona colaborador;


    public TbRegistro() {
    }

    public TbRegistro(Integer id_registro, LocalDateTime fecha_registro, TbEspacio espacio, TbVehiculo vehiculo, Persona aprendiz, Persona colaborador) {
        this.id_registro = id_registro;
        this.fecha_registro = fecha_registro;
        this.espacio = espacio;
        this.vehiculo = vehiculo;
        this.aprendiz = aprendiz;
        this.colaborador = colaborador;
    }

    public Integer getId_registro() {
        return id_registro;
    }

    public void setId_registro(Integer id_registro) {
        this.id_registro = id_registro;
    }

    public LocalDateTime getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_reporte(LocalDateTime fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public TbEspacio getEspacio() {
        return espacio;
    }

    public void setEspacio(TbEspacio espacio) {
        this.espacio = espacio;
    }

    public TbVehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(TbVehiculo vehiculo) {
        this.vehiculo = vehiculo;
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

