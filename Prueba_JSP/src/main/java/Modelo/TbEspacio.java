package Modelo;

import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(name = "tb_espacios")
public class TbEspacio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_espacio", nullable = false)
    private Integer id_espacio;
    private String nombre;
    private String placa_vehiculo;
    private Integer documento_aprendiz;
    private Date hora_entrada;
    private Date hora_salida;
    private Integer cantidad_cascos;
    private String estado_espacio;
    //Foranea Mucho a uno de la clase Casillero
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_casillero_fk", nullable = false) // Nombre de la columna de la clave foránea en esta entidad
    private TbCasillero casillero;


    public TbEspacio() {
    }

    public TbEspacio(Integer id_espacio, String nombre, String placa_vehiculo, Integer documento_aprendiz, Date hora_entrada, Date hora_salida, Integer cantidad_cascos, String estado_espacio, TbCasillero casillero) {
        this.id_espacio = id_espacio;
        this.nombre = nombre;
        this.placa_vehiculo = placa_vehiculo;
        this.documento_aprendiz = documento_aprendiz;
        this.hora_entrada = hora_entrada;
        this.hora_salida = hora_salida;
        this.cantidad_cascos = cantidad_cascos;
        this.estado_espacio = estado_espacio;
        this.casillero = casillero;
    }

    public TbCasillero getCasillero() {
        return casillero;
    }

    public void setCasillero(TbCasillero casillero) {
        this.casillero = casillero;
    }

    public Integer getCantidad_cascos() {
        return cantidad_cascos;
    }

    public void setCantidad_cascos(Integer cantidad_cascos) {
        this.cantidad_cascos = cantidad_cascos;
    }

    public Date getHora_salida() {
        return hora_salida;
    }

    public void setHora_salida(Date hora_salida) {
        this.hora_salida = hora_salida;
    }

    public Date getHora_entrada() {
        return hora_entrada;
    }

    public void setHora_entrada(Date hora_entrada) {
        this.hora_entrada = hora_entrada;
    }

    public Integer getDocumento_aprendiz() {
        return documento_aprendiz;
    }

    public void setDocumento_aprendiz(Integer documento_aprendiz) {
        this.documento_aprendiz = documento_aprendiz;
    }

    public String getPlaca_vehiculo() {
        return placa_vehiculo;
    }

    public void setPlaca_vehiculo(String placa_vehiculo) {
        this.placa_vehiculo = placa_vehiculo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getId_espacio() {
        return id_espacio;
    }

    public void setId_espacio(Integer id_espacio) {
        this.id_espacio = id_espacio;
    }

    public String getEstado_espacio() {
        return estado_espacio;
    }

    public void setEstado_espacio(String estado_espacio) {
        this.estado_espacio = estado_espacio;
    }
}



