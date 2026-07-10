package cl.duoc.mstransmisiones.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {
    private String mensaje;
    private int codigoHttp;
    private List<String> detalles;
    private LocalDateTime timestamp;
}