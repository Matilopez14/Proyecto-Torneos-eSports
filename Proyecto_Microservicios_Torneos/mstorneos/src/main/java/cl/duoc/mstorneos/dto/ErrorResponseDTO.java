package cl.duoc.mstorneos.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ErrorResponseDTO {

    private String mensaje;
    private int codigoHttp;
    private List<String> detalles;
    private LocalDateTime timestamp;

    public ErrorResponseDTO() {
    }

    public ErrorResponseDTO(String mensaje, int codigoHttp, List<String> detalles, LocalDateTime timestamp) {
        this.mensaje = mensaje;
        this.codigoHttp = codigoHttp;
        this.detalles = detalles;
        this.timestamp = timestamp;
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getCodigoHttp() {
        return codigoHttp;
    }

    public List<String> getDetalles() {
        return detalles;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setCodigoHttp(int codigoHttp) {
        this.codigoHttp = codigoHttp;
    }

    public void setDetalles(List<String> detalles) {
        this.detalles = detalles;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}