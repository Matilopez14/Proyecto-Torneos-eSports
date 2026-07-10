package com.example.mspremios.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@Schema(description = "DTO para registrar un premio")
public class PremioDTO {

    @NotNull(message = "El ID del torneo es obligatorio")
    @Schema(description = "ID del torneo asociado", example = "1")
    private Long torneoId;

    @Positive(message = "El monto total debe ser un número positivo")
    @NotNull(message = "El monto total es obligatorio")
    @Schema(description = "Monto total del premio", example = "10000.0")
    private Double montoTotal;

    @NotBlank(message = "La posición de destino (ej: 1er Lugar) no puede estar vacía")
    @Schema(description = "Posición a la que se asigna el premio", example = "1er Lugar")
    private String posicionDestino;

    @Min(value = 1, message = "El porcentaje mínimo es 1%")
    @Max(value = 100, message = "El porcentaje máximo es 100%")
    @NotNull(message = "El porcentaje de distribución es obligatorio")
    @Schema(description = "Porcentaje de distribución del premio", example = "50.0")
    private Double porcentajeDistribucion;
}
