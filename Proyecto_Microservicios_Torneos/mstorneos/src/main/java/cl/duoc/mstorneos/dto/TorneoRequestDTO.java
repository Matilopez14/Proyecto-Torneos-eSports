package cl.duoc.mstorneos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
@Schema(description = "DTO para crear o actualizar un torneo")
public class TorneoRequestDTO {

    @NotBlank(message = "El nombre del torneo es obligatorio")
    @Schema(description = "Nombre del torneo", example = "Copa Invernal 2025")
    private String nombre;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @FutureOrPresent(message = "La fecha de inicio no puede ser en el pasado")
    @Schema(description = "Fecha de inicio del torneo", example = "2025-08-01")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Schema(description = "Fecha de finalización del torneo", example = "2025-08-15")
    private LocalDate fechaFin;

    @Min(value = 0, message = "El premio no puede ser negativo")
    @Schema(description = "Monto total del premio en dinero", example = "50000.0")
    private Double prizePool;
}
