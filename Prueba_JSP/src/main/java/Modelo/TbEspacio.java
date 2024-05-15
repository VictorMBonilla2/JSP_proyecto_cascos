package Modelo;

import jakarta.persistence.*;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_casillero_fk") // Nombre de la columna de la clave foránea en esta entidad
    private TbCasillero casillero;


}



