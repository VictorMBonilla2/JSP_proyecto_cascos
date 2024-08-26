package Modelo;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tb_vehiculo")
public class TbVehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo", nullable = false)
    private int id_vehiculo;
    private String placa_vehiculo;
    private String marca_vehiculo;
    private String modelo_vehiculo;
    private Integer cant_casco;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_documento")
    private Persona persona;
    private String ciudad_vehiculo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipovehiculo")  // Clave foránea que conecta con TbTipovehiculo
    private TbTipovehiculo tipovehiculo;


    public TbVehiculo() {
    }

    public TbVehiculo(int id_vehiculo, String placa_vehiculo, String marca_vehiculo, String modelo_vehiculo, Integer cant_casco, Persona persona, String ciudad_vehiculo, TbTipovehiculo tipovehiculo) {
        this.id_vehiculo = id_vehiculo;
        this.placa_vehiculo = placa_vehiculo;
        this.marca_vehiculo = marca_vehiculo;
        this.modelo_vehiculo = modelo_vehiculo;
        this.cant_casco = cant_casco;
        this.persona = persona;
        this.ciudad_vehiculo = ciudad_vehiculo;
        this.tipovehiculo = tipovehiculo;
    }

    public int getId_vehiculo() {
        return id_vehiculo;
    }

    public void setId_vehiculo(int id_vehiculo) {
        this.id_vehiculo = id_vehiculo;
    }

    public String getPlaca_vehiculo() {
        return placa_vehiculo;
    }

    public void setPlaca_vehiculo(String placa_vehiculo) {
        this.placa_vehiculo = placa_vehiculo;
    }

    public String getMarca_vehiculo() {
        return marca_vehiculo;
    }

    public void setMarca_vehiculo(String marca_vehiculo) {
        this.marca_vehiculo = marca_vehiculo;
    }

    public String getModelo_vehiculo() {
        return modelo_vehiculo;
    }

    public void setModelo_vehiculo(String modelo_vehiculo) {
        this.modelo_vehiculo = modelo_vehiculo;
    }

    public Integer getCant_casco() {
        return cant_casco;
    }

    public void setCant_casco(Integer cant_casco) {
        this.cant_casco = cant_casco;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getCiudad_vehiculo() {
        return ciudad_vehiculo;
    }

    public void setCiudad_vehiculo(String ciudad_vehiculo) {
        this.ciudad_vehiculo = ciudad_vehiculo;
    }

    public TbTipovehiculo getTipovehiculo() {
        return tipovehiculo;
    }

    public void setTipovehiculo(TbTipovehiculo tipovehiculo) {
        this.tipovehiculo = tipovehiculo;
    }
}