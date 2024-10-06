package Modelo;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_registro")
public class TbRegistro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_registro", nullable = false)

    private Integer id_registro;

    private LocalDateTime fecha_registro;
    private int id_espacio;

    private String placaVehiculo;
    private int documentoAprendiz;
    private int documentoGestor;

    public TbRegistro() {
    }

    public TbRegistro(Integer id_registro, LocalDateTime fecha_registro, int id_espacio, String placaVehiculo, int documentoAprendiz, int documentoGestor) {
        this.id_registro = id_registro;
        this.fecha_registro = fecha_registro;
        this.id_espacio = id_espacio;
        this.placaVehiculo = placaVehiculo;
        this.documentoAprendiz = documentoAprendiz;
        this.documentoGestor = documentoGestor;
    }

    public Integer getId_registro() {
        return id_registro;
    }

    public void setId_registro(Integer id_registro) {
        this.id_registro = id_registro;
    }

    public LocalDateTime getFecha_registro() {
        return fecha_registro;
    }

    public void setFecha_reporte(LocalDateTime fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public int getId_espacio() {
        return id_espacio;
    }

    public void setId_espacio(int id_espacio) {
        this.id_espacio = id_espacio;
    }

    public void setFecha_registro(LocalDateTime fecha_registro) {
        this.fecha_registro = fecha_registro;
    }

    public String getPlacaVehiculo() {
        return placaVehiculo;
    }

    public void setPlacaVehiculo(String placaVehiculo) {
        this.placaVehiculo = placaVehiculo;
    }

    public int getDocumentoAprendiz() {
        return documentoAprendiz;
    }

    public void setDocumentoAprendiz(int documentoAprendiz) {
        this.documentoAprendiz = documentoAprendiz;
    }

    public int getDocumentoGestor() {
        return documentoGestor;
    }

    public void setDocumentoGestor(int documentoGestor) {
        this.documentoGestor = documentoGestor;
    }
}

