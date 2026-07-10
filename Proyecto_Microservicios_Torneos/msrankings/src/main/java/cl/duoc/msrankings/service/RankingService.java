package cl.duoc.msrankings.service;

import cl.duoc.msrankings.dto.EstadisticaDTO;
import cl.duoc.msrankings.dto.JugadorDTO;
import cl.duoc.msrankings.dto.RankingDTO;
import cl.duoc.msrankings.repository.RankingDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RankingService {

    private final RankingDataRepository rankingDataRepository;

    public List<RankingDTO> obtenerTop10Jugadores() {
        List<EstadisticaDTO> todasEstadisticas = rankingDataRepository.obtenerTodasEstadisticas();

        // Agrupar estadísticas por jugador
        Map<Long, List<EstadisticaDTO>> estadisticasPorJugador = todasEstadisticas.stream()
                .collect(Collectors.groupingBy(EstadisticaDTO::getIdJugador));

        List<RankingDTO> rankings = new ArrayList<>();

        for (Map.Entry<Long, List<EstadisticaDTO>> entry : estadisticasPorJugador.entrySet()) {
            Long idJugador = entry.getKey();
            List<EstadisticaDTO> stats = entry.getValue();

            int totalKills = stats.stream().mapToInt(EstadisticaDTO::getKills).sum();
            int totalDeaths = stats.stream().mapToInt(EstadisticaDTO::getDeaths).sum();
            int totalAssists = stats.stream().mapToInt(EstadisticaDTO::getAssists).sum();

            double kdaPromedio = stats.stream()
                    .mapToDouble(EstadisticaDTO::getKda)
                    .average()
                    .orElse(0.0);

            String riotId = "Desconocido";
            try {
                JugadorDTO jugador = rankingDataRepository.obtenerJugadorPorId(idJugador);
                if (jugador != null) {
                    riotId = jugador.getRiotId();
                }
            } catch (Exception e) {
                // Si el jugador no se encuentra, usamos el nombre predeterminado
            }

            rankings.add(new RankingDTO(idJugador, riotId, kdaPromedio, totalKills, totalDeaths, totalAssists));
        }

        // Ordenar por KDA promedio descendente y tomar los 10 mejores
        return rankings.stream()
                .sorted(Comparator.comparing(RankingDTO::getKdaPromedio).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
}
