package Modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_tipovehiculo")
public class TbTipovehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipovehiculo", nullable = false)
    private int id;

    @Column(name = "nombre_tipovehiculo")
    private String nombre;


    public TbTipovehiculo() {
    }

    public TbTipovehiculo(int id, String nombre, TbVehiculo vehiculo) {
        this.id = id;
        this.nombre = nombre;
    }
    // Getters y Setters

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


}
