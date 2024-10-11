package Modelo;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tb_tipovehiculo")
public class TbTipovehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipovehiculo", nullable = false)
    private int id;
    @Column(name = "nombre_tipovehiculo", nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "tipoVehiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Tb_MarcaVehiculo> marcas = new LinkedHashSet<>();

    public TbTipovehiculo() {
    }

    public TbTipovehiculo(int id, String nombre, Set<Tb_MarcaVehiculo> marcas) {
        this.id = id;
        this.nombre = nombre;
        this.marcas = marcas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<Tb_MarcaVehiculo> getMarcas() {
        return marcas;
    }

    public void setMarcas(Set<Tb_MarcaVehiculo> marcas) {
        this.marcas = marcas;
    }
}
