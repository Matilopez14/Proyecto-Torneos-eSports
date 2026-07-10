package cl.duoc.msrankings.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "rankings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_jugador", nullable = false)
    private Long idJugador;

    @Column(name = "riot_id")
    private String riotId;

    @Column(name = "kda_promedio")
    private Double kdaPromedio;

    @Column(name = "total_kills")
    private Integer totalKills;

    @Column(name = "total_deaths")
    private Integer totalDeaths;

    @Column(name = "total_assists")
    private Integer totalAssists;
}
