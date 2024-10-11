package Modelo;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "tb_marca_vehiculo")

public class Tb_MarcaVehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nombre_marca", nullable = false)
    private String nombreMarca;

    @ManyToOne
    @JoinColumn(name = "tipo_vehiculo_id", nullable = false)
    private TbTipovehiculo tipoVehiculo;

    @OneToMany(mappedBy = "marcaVehiculo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Tb_ModeloVehiculo> modelos = new LinkedHashSet<>();

    // Constructor vacío
    public Tb_MarcaVehiculo() {
    }

    public Tb_MarcaVehiculo(int id, String nombreMarca, TbTipovehiculo tipoVehiculo, Set<Tb_ModeloVehiculo> modelos) {
        this.id = id;
        this.nombreMarca = nombreMarca;
        this.tipoVehiculo = tipoVehiculo;
        this.modelos = modelos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        this.nombreMarca = nombreMarca;
    }

    public TbTipovehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TbTipovehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public Set<Tb_ModeloVehiculo> getModelos() {
        return modelos;
    }

    public void setModelos(Set<Tb_ModeloVehiculo> modelos) {
        this.modelos = modelos;
    }
}
