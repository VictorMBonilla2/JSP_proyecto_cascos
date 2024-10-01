package Modelo;

import Modelo.enums.ColorVehiculo;
import jakarta.persistence.*;

@Entity
@Table(name = "tb_vehiculo")
public class TbVehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo", nullable = false)
    private int id_vehiculo;

    @Column(name = "placa_vehiculo", nullable = false)
    private String placaVehiculo;

    @Column(name = "cant_casco")
    private Integer cantCasco;

    @Enumerated(EnumType.STRING)
    @Column(name = "color_vehiculo")
    private ColorVehiculo colorVehiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "persona_documento")
    private Persona persona;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_ciudad", nullable = false)  // Relación con la tabla de ciudades
    private Tb_CiudadVehiculo ciudadVehiculo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipovehiculo", nullable = false)  // Clave foránea que conecta con TbTipovehiculo
    private TbTipovehiculo tipovehiculo;

    // Nueva relación con Tb_MarcaVehiculo
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_marcavehiculo", nullable = false)  // Clave foránea que conecta con Tb_MarcaVehiculo
    private Tb_MarcaVehiculo marcaVehiculo;

    // Nueva relación con Tb_ModeloVehiculo
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_modelovehiculo", nullable = false)  // Clave foránea que conecta con Tb_ModeloVehiculo
    private Tb_ModeloVehiculo modeloVehiculo;


    public TbVehiculo() {
    }

    public TbVehiculo(int id_vehiculo, String placaVehiculo, Integer cantCasco, ColorVehiculo colorVehiculo, Persona persona, Tb_CiudadVehiculo ciudadVehiculo, TbTipovehiculo tipovehiculo, Tb_MarcaVehiculo marcaVehiculo, Tb_ModeloVehiculo modeloVehiculo) {
        this.id_vehiculo = id_vehiculo;
        this.placaVehiculo = placaVehiculo;
        this.cantCasco = cantCasco;
        this.colorVehiculo = colorVehiculo;
        this.persona = persona;
        this.ciudadVehiculo = ciudadVehiculo;
        this.tipovehiculo = tipovehiculo;
        this.marcaVehiculo = marcaVehiculo;
        this.modeloVehiculo = modeloVehiculo;
    }

    public int getId_vehiculo() {
        return id_vehiculo;
    }

    public void setId_vehiculo(int id_vehiculo) {
        this.id_vehiculo = id_vehiculo;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }

    public Integer getCantCasco() {
        return cantCasco;
    }

    public void setCantCasco(Integer cantCasco) {
        this.cantCasco = cantCasco;
    }

    public ColorVehiculo getColorVehiculo() {
        return colorVehiculo;
    }

    public void setColorVehiculo(ColorVehiculo colorVehiculo) {
        this.colorVehiculo = colorVehiculo;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Tb_CiudadVehiculo getCiudadVehiculo() {
        return ciudadVehiculo;
    }

    public void setCiudadVehiculo(Tb_CiudadVehiculo ciudadVehiculo) {
        this.ciudadVehiculo = ciudadVehiculo;
    }

    public TbTipovehiculo getTipovehiculo() {
        return tipovehiculo;
    }

    public void setTipovehiculo(TbTipovehiculo tipovehiculo) {
        this.tipovehiculo = tipovehiculo;
    }

    public Tb_MarcaVehiculo getMarcaVehiculo() {
        return marcaVehiculo;
    }

    public void setMarcaVehiculo(Tb_MarcaVehiculo marcaVehiculo) {
        this.marcaVehiculo = marcaVehiculo;
    }

    public Tb_ModeloVehiculo getModeloVehiculo() {
        return modeloVehiculo;
    }

    public void setModeloVehiculo(Tb_ModeloVehiculo modeloVehiculo) {
        this.modeloVehiculo = modeloVehiculo;
    }
}