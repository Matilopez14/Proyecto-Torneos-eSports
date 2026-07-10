package com.example.ms_estadisticas.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EstadisticaRequestDTO {

    @NotNull(message = "El ID de la partida es obligatorio")
    private Long idPartida;

    @NotNull(message = "El ID del jugador es obligatorio")
    private Long idJugador;

    @NotNull(message = "Debe ingresar la cantidad de kills")
    @Min(value = 0, message = "Las kills no pueden ser negativas")
    private Integer kills;

    @NotNull(message = "Debe ingresar la cantidad de deaths")
    @Min(value = 0, message = "Las deaths no pueden ser negativas")
    private Integer deaths;

    @NotNull(message = "Debe ingresar la cantidad de assists")
    @Min(value = 0, message = "Las assists no pueden ser negativas")
    private Integer assists;
}
