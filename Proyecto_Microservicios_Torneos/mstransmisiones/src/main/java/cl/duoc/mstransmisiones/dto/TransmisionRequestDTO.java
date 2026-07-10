package cl.duoc.mstransmisiones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransmisionRequestDTO {

    @NotNull(message = "El ID de la partida no puede ser nulo")
    private Long idPartida;

    @NotBlank(message = "La plataforma no puede estar vacía")
    private String plataforma;

    @NotBlank(message = "La URL no puede estar vacía")
    private String url;

    @NotBlank(message = "El estado no puede estar vacío")
    private String estado;

    private LocalDateTime fechaInicio;
}