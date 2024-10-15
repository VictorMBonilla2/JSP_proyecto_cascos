package Modelo;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "tb_registros")
public class TbRegistro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRegistro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_espacio", nullable = false)
    private TbEspacio espacio;

    @Column(name = "hora_entrada")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEntrada;

    @Column(name = "fecha_registro", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaRegistro;

    // Cambiar la relación a TbVehiculo en lugar de Persona
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_vehiculo", nullable = false)
    private TbVehiculo vehiculo;

    // Relación con la entidad Gestor (si es una entidad separada)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_gestor", nullable = false)
    private Persona gestor;

    public TbRegistro() {
    }


    public TbRegistro(int idRegistro, TbEspacio espacio, Date fechaEntrada, Date fechaRegistro, TbVehiculo vehiculo, Persona gestor) {
        this.idRegistro = idRegistro;
        this.espacio = espacio;
        this.fechaEntrada = fechaEntrada;
        this.fechaRegistro = fechaRegistro;
        this.vehiculo = vehiculo;
        this.gestor = gestor;
    }

    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public TbEspacio getEspacio() {
        return espacio;
    }

    public void setEspacio(TbEspacio espacio) {
        this.espacio = espacio;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
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

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }
}

