package cl.duoc.mspartidas.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ErrorResponseDTO {

    private String mensaje;
    private int codigoHttp;
    private List<String> detalles;
    private LocalDateTime timestamp;
}