package cl.duoc.msinscripciones.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InscripcionRequestDTO {

    @NotNull(message = "El ID del torneo es obligatorio")
    private Long idTorneo;

    @NotNull(message = "El ID del equipo es obligatorio")
    private Long idEquipo;
}
