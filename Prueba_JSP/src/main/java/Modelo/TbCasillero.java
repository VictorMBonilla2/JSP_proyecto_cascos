package Modelo;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tb_casillero")
public class TbCasillero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_casillero", nullable = false)
    private Integer id;
    @Column
    private Integer cant_espacio;

    @OneToMany(mappedBy = "casillero", cascade = CascadeType.ALL)
    private Set<TbEspacio> espacios = new LinkedHashSet<>();

    public TbCasillero() {
    }

    public TbCasillero(Integer id, Integer cant_espacio, Set<TbEspacio> espacios) {
        this.id = id;
        this.cant_espacio = cant_espacio;
        this.espacios = espacios;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCant_espacio() {
        return cant_espacio;
    }

    public void setCant_espacio(Integer cant_espacio) {
        this.cant_espacio = cant_espacio;
    }

    public Set<TbEspacio> getEspacios() {
        return espacios;
    }

    public void setEspacios(Set<TbEspacio> espacios) {
        this.espacios = espacios;
    }
}