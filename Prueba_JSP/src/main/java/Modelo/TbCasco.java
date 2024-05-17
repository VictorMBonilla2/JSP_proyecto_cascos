package Modelo;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tb_cascos")
public class TbCasco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_casco", nullable = false)
    private int id;
    private String placa_casco;
    private Integer cant_casco;
    @OneToMany(mappedBy = "casco", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<TbEspacio> espacios = new LinkedHashSet<>();
    private String ciudad;

    public TbCasco() {
    }

    public TbCasco(int id, String placa_casco, Integer cant_casco, Set<TbEspacio> espacios, String ciudad) {
        this.id = id;
        this.placa_casco = placa_casco;
        this.cant_casco = cant_casco;
        this.espacios = espacios;
        this.ciudad = ciudad;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca_casco() {
        return placa_casco;
    }

    public void setPlaca_casco(String placa_casco) {
        this.placa_casco = placa_casco;
    }

    public Integer getCant_casco() {
        return cant_casco;
    }

    public void setCant_casco(Integer cant_casco) {
        this.cant_casco = cant_casco;
    }

    public Set<TbEspacio> getEspacios() {
        return espacios;
    }

    public void setEspacios(Set<TbEspacio> espacios) {
        this.espacios = espacios;
    }
}