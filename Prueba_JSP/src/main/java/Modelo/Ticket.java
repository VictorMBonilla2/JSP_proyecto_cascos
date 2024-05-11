package Modelo;

import java.sql.Time;
import java.util.Date;

public class Ticket {

    private int cod_ticket;
    private Date fecha_entrada;
    private Date fecha_salida;
    private Time hora_entrada;
    private Time hora_salida;
    private int cod_espacio_Fk;
    private int cod_cascos_Fk;

    public Ticket() {
    }

    public int getCod_ticket() {
        return cod_ticket;
    }

    public void setCod_ticket(int cod_ticket) {
        this.cod_ticket = cod_ticket;
    }

    public Date getFecha_entrada() {
        return fecha_entrada;
    }

    public void setFecha_entrada(Date fecha_entrada) {
        this.fecha_entrada = fecha_entrada;
    }

    public Date getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(Date fecha_salida) {
        this.fecha_salida = fecha_salida;
    }

    public Time getHora_entrada() {
        return hora_entrada;
    }

    public void setHora_entrada(Time hora_entrada) {
        this.hora_entrada = hora_entrada;
    }

    public Time getHora_salida() {
        return hora_salida;
    }

    public void setHora_salida(Time hora_salida) {
        this.hora_salida = hora_salida;
    }

    public int getCod_espacio_Fk() {
        return cod_espacio_Fk;
    }

    public void setCod_espacio_Fk(int cod_espacio_Fk) {
        this.cod_espacio_Fk = cod_espacio_Fk;
    }

    public int getCod_cascos_Fk() {
        return cod_cascos_Fk;
    }

    public void setCod_cascos_Fk(int cod_cascos_Fk) {
        this.cod_cascos_Fk = cod_cascos_Fk;
    }
}
