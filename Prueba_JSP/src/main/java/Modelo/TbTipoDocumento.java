package Modelo;

import jakarta.persistence.*;

@Entity
@Table (name= "tb_tipodocumento")
public class TbTipoDocumento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipodocumento", nullable = false)
    private int id;

    @Column(name="nombre_documento", nullable = false)
    private String nombreDocumento;

    public TbTipoDocumento() {
    }

    public TbTipoDocumento(int id, String nombreDocumento) {
        this.id = id;
        this.nombreDocumento = nombreDocumento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento(String nombreDocumento) {
        this.nombreDocumento = nombreDocumento;
    }
}
