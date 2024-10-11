package Modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_registro")
public class TbRegistro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro", nullable = false)
    private Integer id_registro;

    private LocalDateTime fecha_registro;

    @ManyToOne(fetch = FetchType.EAGER) // Relación con Persona
    @JoinColumn(name = "id_aprendiz", referencedColumnName = "id", nullable = false)
    private Persona aprendiz;

    @ManyToOne(fetch = FetchType.EAGER) // Relación con Gestor, si aplica
    @JoinColumn(name = "id_gestor", referencedColumnName = "id", nullable = true)
    private Persona gestor;

    private int id_espacio;
    @Column ( nullable = false)
    private String placaVehiculo;

    public TbRegistro() {
    }

    public TbRegistro(Integer id_registro, LocalDateTime fecha_registro, Persona aprendiz, Persona gestor, int id_espacio, String placaVehiculo) {
        this.id_registro = id_registro;
        this.fecha_registro = fecha_registro;
        this.aprendiz = aprendiz;
        this.gestor = gestor;
        this.id_espacio = id_espacio;
        this.placaVehiculo = placaVehiculo;
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

    public void setFecha_registro(LocalDateTime fecha_registro) {
        this.fecha_registro = fecha_registro;
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

    public int getId_espacio() {
        return id_espacio;
    }

    public void setId_espacio(int id_espacio) {
        this.id_espacio = id_espacio;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }
}

