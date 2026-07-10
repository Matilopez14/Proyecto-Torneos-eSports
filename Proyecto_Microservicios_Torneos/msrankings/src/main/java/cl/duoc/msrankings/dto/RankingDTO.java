package cl.duoc.msrankings.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RankingDTO {
    private Long idJugador;
    private String riotId;
    private Double kdaPromedio;
    private Integer totalKills;
    private Integer totalDeaths;
    private Integer totalAssists;
}
