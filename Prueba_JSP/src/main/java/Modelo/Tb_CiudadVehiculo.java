package Modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_ciudad_vehiculo")
public class Tb_CiudadVehiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nombre_ciudad", nullable = false, unique = true)
    private String nombreCiudad;

    // Constructor vacío
    public Tb_CiudadVehiculo() {
    }

    // Constructor completo
    public Tb_CiudadVehiculo(int id, String nombreCiudad) {
        this.id = id;
        this.nombreCiudad = nombreCiudad;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }
}
