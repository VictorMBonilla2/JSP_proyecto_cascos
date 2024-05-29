package Modelo;

import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(name = "tb_espacios")
public class TbEspacio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_espacio", nullable = false)
    private Integer id;
    private String estado_espacio;
    private Date hora_entrada;
    private Date hora_salida;
    //Foranea Mucho a uno de la clase Casillero
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_casillero_fk", nullable = false) // Nombre de la columna de la clave foránea en esta entidad
    private TbCasillero casillero;
    //Foranea muchos a uno de la clase Cascos
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cod_casco_fk")
    private TbCasco casco;

    public TbCasco getCasco() {
        return casco;
    }

    public void setCasco(TbCasco casco) {
        this.casco = casco;
    }

    public TbEspacio() {
    }

    public TbEspacio(Integer id, String estado_espacio, Date hora_entrada, Date hora_salida, TbCasillero casillero, TbCasco casco) {
        this.id = id;
        this.estado_espacio = estado_espacio;
        this.hora_entrada = hora_entrada;
        this.hora_salida = hora_salida;
        this.casillero = casillero;
        this.casco = casco;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEstado_espacio() {
        return estado_espacio;
    }

    public void setEstado_espacio(String estado_espacio) {
        this.estado_espacio = estado_espacio;
    }

    public Date getHora_entrada() {
        return hora_entrada;
    }

    public void setHora_entrada(Date hora_entrada) {
        this.hora_entrada = hora_entrada;
    }

    public Date getHora_salida() {
        return hora_salida;
    }

    public void setHora_salida(Date hora_salida) {
        this.hora_salida = hora_salida;
    }

    public TbCasillero getCasillero() {
        return casillero;
    }

    public void setCasillero(TbCasillero casillero) {
        this.casillero = casillero;
    }
}



