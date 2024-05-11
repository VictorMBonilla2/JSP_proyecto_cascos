package Modelo;

public class Cascos {
    private int cod_cascos;
    private String placa_cascos;
    private int cant_cascos;
    private int cod_ticket_Fk;

    public Cascos(int cod_cascos) {
        this.cod_cascos = cod_cascos;
    }

    public int getCod_cascos() {
        return cod_cascos;
    }

    public void setCod_cascos(int cod_cascos) {
        this.cod_cascos = cod_cascos;
    }

    public String getPlaca_cascos() {
        return placa_cascos;
    }

    public void setPlaca_cascos(String placa_cascos) {
        this.placa_cascos = placa_cascos;
    }

    public int getCant_cascos() {
        return cant_cascos;
    }

    public void setCant_cascos(int cant_cascos) {
        this.cant_cascos = cant_cascos;
    }

    public int getCod_ticket_Fk() {
        return cod_ticket_Fk;
    }

    public void setCod_ticket_Fk(int cod_ticket_Fk) {
        this.cod_ticket_Fk = cod_ticket_Fk;
    }
}
