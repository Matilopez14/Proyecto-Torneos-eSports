package cl.duoc.msrankings.dto;

import lombok.Data;

@Data
public class EstadisticaDTO {
    private Long id;
    private Long idPartida;
    private Long idJugador;
    private Integer kills;
    private Integer deaths;
    private Integer assists;
    private Double kda;
}
