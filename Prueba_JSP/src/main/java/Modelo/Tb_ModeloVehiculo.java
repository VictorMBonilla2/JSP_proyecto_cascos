package Modelo;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_modelo_vehiculo")
public class Tb_ModeloVehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nombre_modelo", nullable = false, unique = true)
    private String nombreModelo;

    @ManyToOne
    @JoinColumn(name = "marca_vehiculo_id", nullable = false)
    private Tb_MarcaVehiculo marcaVehiculo;

    // Constructor vacío
    public Tb_ModeloVehiculo() {
    }

    // Constructor completo
    public Tb_ModeloVehiculo(int id, String nombreModelo) {
        this.id = id;
        this.nombreModelo = nombreModelo;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreModelo() {
        return nombreModelo;
    }

    public void setNombreModelo(String nombreModelo) {
        this.nombreModelo = nombreModelo;
    }
}
