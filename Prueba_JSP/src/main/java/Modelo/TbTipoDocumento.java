package Modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name= "tb_tipodocumento")
public class TbTipoDocumento {
    @Id
    private int id;

    @Column(name="nombre_documento")
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
