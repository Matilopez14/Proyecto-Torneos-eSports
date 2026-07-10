package cl.duoc.msrankings.repository;

import cl.duoc.msrankings.client.EstadisticaClient;
import cl.duoc.msrankings.client.JugadorClient;
import cl.duoc.msrankings.dto.EstadisticaDTO;
import cl.duoc.msrankings.dto.JugadorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RankingDataRepository {

    private final EstadisticaClient estadisticaClient;
    private final JugadorClient jugadorClient;

    public List<EstadisticaDTO> obtenerTodasEstadisticas() {
        return estadisticaClient.listarTodas();
    }

    public List<EstadisticaDTO> obtenerEstadisticasPorJugador(Long idJugador) {
        return estadisticaClient.listarPorJugador(idJugador);
    }

    public JugadorDTO obtenerJugadorPorId(Long id) {
        return jugadorClient.obtenerPorId(id);
    }

    public List<JugadorDTO> obtenerTodosJugadores() {
        return jugadorClient.listarTodos();
    }
}
