package cl.duoc.msrankings.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {

    private String mensaje;
    private int codigoHttp;
    private List<String> detalles;
    private LocalDateTime timestamp;
}
