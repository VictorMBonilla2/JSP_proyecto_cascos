package Modelo;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tb_sectores")
public class TbSectores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cod_sector", nullable = false)
    private Integer id;
    @Column ( nullable = false)
    private Integer cant_espacio;
    @Column ( nullable = false)
    private String nombreSector;

    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<TbEspacio> espacios;


    public TbSectores() {
    }

    public TbSectores(Integer id, Integer cant_espacio, String nombreSector, Set<TbEspacio> espacios) {
        this.id = id;
        this.cant_espacio = cant_espacio;
        this.nombreSector = nombreSector;
        this.espacios = espacios;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCant_espacio() {
        return cant_espacio;
    }

    public void setCant_espacio(Integer cant_espacio) {
        this.cant_espacio = cant_espacio;
    }

    public String getNombreSector() {
        return nombreSector;
    }

    public void setNombreSector(String nombreSector) {
        this.nombreSector = nombreSector;
    }

    public Set<TbEspacio> getEspacios() {
        return espacios;
    }

    public void setEspacios(Set<TbEspacio> espacios) {
        this.espacios = espacios;
    }
}