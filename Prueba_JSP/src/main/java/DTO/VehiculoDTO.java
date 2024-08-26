package DTO;

public class VehiculoDTO {
    private int id;
    private String placa_vehiculo;
    private String marca_vehiculo;
    private Integer cant_cascos;

    public VehiculoDTO(int id, String placa_vehiculo, String marca_vehiculo, Integer cant_cascos) {
        this.id = id;
        this.placa_vehiculo = placa_vehiculo;
        this.marca_vehiculo = marca_vehiculo;
        this.cant_cascos = cant_cascos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca_vehiculo() {
        return placa_vehiculo;
    }

    public void setPlaca_vehiculo(String placa_vehiculo) {
        this.placa_vehiculo = placa_vehiculo;
    }

    public String getMarca_vehiculo() {
        return marca_vehiculo;
    }

    public void setMarca_vehiculo(String marca_vehiculo) {
        this.marca_vehiculo = marca_vehiculo;
    }

    public Integer getCant_cascos() {
        return cant_cascos;
    }

    public void setCant_cascos(Integer cant_cascos) {
        this.cant_cascos = cant_cascos;
    }
}
