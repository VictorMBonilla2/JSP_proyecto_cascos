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
    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Persona> documento = new LinkedHashSet<>();
    private String ciudad_vehiculo;

    public TbVehiculo() {
    }

    public TbVehiculo(int id_vehiculo, String placa_vehiculo, String marca_vehiculo, String modelo_vehiculo, Integer cant_casco, Set<Persona> documento, String ciudad_vehiculo) {
        this.id_vehiculo = id_vehiculo;
        this.placa_vehiculo = placa_vehiculo;
        this.marca_vehiculo = marca_vehiculo;
        this.modelo_vehiculo = modelo_vehiculo;
        this.cant_casco = cant_casco;
        this.documento = documento;
        this.ciudad_vehiculo = ciudad_vehiculo;
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

    public Set<Persona> getDocumento() {
        return documento;
    }

    public void setDocumento(Set<Persona> documento) {
        this.documento = documento;
    }

    public String getCiudad_vehiculo() {
        return ciudad_vehiculo;
    }

    public void setCiudad_vehiculo(String ciudad_vehiculo) {
        this.ciudad_vehiculo = ciudad_vehiculo;
    }
}