package cl.duoc.mspartidas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PartidaRequestDTO {

    @NotNull(message = "El ID del torneo es obligatorio")
    private Long idTorneo;

    @NotNull(message = "El ID del Equipo A es obligatorio")
    private Long idEquipoA;

    @NotNull(message = "El ID del Equipo B es obligatorio")
    private Long idEquipoB;

    @NotBlank(message = "El mapa es obligatorio")
    @Size(max = 50, message = "El mapa no puede superar los 50 caracteres")
    private String mapa;
}