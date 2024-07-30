package Modelo;

import jakarta.persistence.*;

import java.util.Date;
@Entity
@Table(name = "tb_registro")
public class TbRegistro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro", nullable = false)
    private Integer id_registro;

    private Date fecha_registro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_espacio_fk", nullable = false) // Clave foránea a TbEspacio
    private TbEspacio espacio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vehiculo_fk", nullable = false) // Clave foránea a TbVehiculo
    private TbVehiculo vehiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aprendiz_fk", nullable = false) // Clave foránea a Persona (aprendiz)
    private Persona aprendiz;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_colaborador_fk", nullable = false) // Clave foránea a Persona (colaborador)
    private Persona colaborador;


    public TbRegistro() {
    }

    public TbRegistro(Integer id_registro, Date fecha_registro, TbEspacio espacio, TbVehiculo vehiculo, Persona aprendiz, Persona colaborador) {
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

