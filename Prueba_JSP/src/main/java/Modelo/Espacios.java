package Modelo;

public class Espacios {
    private int cod_espacio;
    private String descripcion;
    private int cod_casillero_FK;
    private int num_espacios;

    public Espacios() {
    }

    public int getCod_espacio() {
        return cod_espacio;
    }

    public void setCod_espacio(int cod_espacio) {
        this.cod_espacio = cod_espacio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCod_casillero_FK() {
        return cod_casillero_FK;
    }

    public void setCod_casillero_FK(int cod_casillero_FK) {
        this.cod_casillero_FK = cod_casillero_FK;
    }

    public int getNum_espacios() {
        return num_espacios;
    }

    public void setNum_espacios(int num_espacios) {
        this.num_espacios = num_espacios;
    }
}
