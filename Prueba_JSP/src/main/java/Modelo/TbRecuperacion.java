package Modelo;

import jakarta.persistence.*;

import java.util.Date;


@Entity
@Table(name = "tb_recuperacion")
public class TbRecuperacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_recuperacion", length = 255, nullable = false)
    private String tokenRecuperacion;

    @Column(name = "fecha_expiracion_token", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaExpiracionToken;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    @Column(name = "token_usado", nullable = false)
    private boolean tokenUsado;

    public TbRecuperacion() {
    }

    public TbRecuperacion(Long id, String tokenRecuperacion, Date fechaExpiracionToken, Persona persona, boolean tokenUsado) {
        this.id = id;
        this.tokenRecuperacion = tokenRecuperacion;
        this.fechaExpiracionToken = fechaExpiracionToken;
        this.persona = persona;
        this.tokenUsado = tokenUsado;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTokenRecuperacion() {
        return tokenRecuperacion;
    }

    public void setTokenRecuperacion(String tokenRecuperacion) {
        this.tokenRecuperacion = tokenRecuperacion;
    }

    public Date getFechaExpiracionToken() {
        return fechaExpiracionToken;
    }

    public void setFechaExpiracionToken(Date fechaExpiracionToken) {
        this.fechaExpiracionToken = fechaExpiracionToken;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public boolean isTokenUsado() {
        return tokenUsado;
    }
    public void setTokenUsado(boolean tokenUsado) {
        this.tokenUsado = tokenUsado;
    }
}
