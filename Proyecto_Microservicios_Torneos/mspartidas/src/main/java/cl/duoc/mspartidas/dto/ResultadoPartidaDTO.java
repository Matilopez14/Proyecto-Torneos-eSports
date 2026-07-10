package cl.duoc.mspartidas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResultadoPartidaDTO {

    @NotNull(message = "El resultado del Equipo A es obligatorio")
    @Min(value = 0, message = "El resultado del Equipo A no puede ser negativo")
    private Integer resultadoA;

    @NotNull(message = "El resultado del Equipo B es obligatorio")
    @Min(value = 0, message = "El resultado del Equipo B no puede ser negativo")
    private Integer resultadoB;
}