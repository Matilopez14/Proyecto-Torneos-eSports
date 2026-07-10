package cl.duoc.msrankings.service;

import cl.duoc.msrankings.dto.EstadisticaDTO;
import cl.duoc.msrankings.dto.JugadorDTO;
import cl.duoc.msrankings.dto.RankingDTO;
import cl.duoc.msrankings.repository.RankingDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RankingServiceTest {

    @Mock
    private RankingDataRepository rankingDataRepository;

    @InjectMocks
    private RankingService rankingService;

    private EstadisticaDTO estadistica1;
    private EstadisticaDTO estadistica2;
    private JugadorDTO jugador;

    @BeforeEach
    void setUp() {
        estadistica1 = new EstadisticaDTO();
        estadistica1.setId(1L);
        estadistica1.setIdJugador(1L);
        estadistica1.setIdPartida(1L);
        estadistica1.setKills(10);
        estadistica1.setDeaths(2);
        estadistica1.setAssists(5);
        estadistica1.setKda(7.5);

        estadistica2 = new EstadisticaDTO();
        estadistica2.setId(2L);
        estadistica2.setIdJugador(2L);
        estadistica2.setIdPartida(1L);
        estadistica2.setKills(5);
        estadistica2.setDeaths(5);
        estadistica2.setAssists(3);
        estadistica2.setKda(1.6);

        jugador = new JugadorDTO();
        jugador.setId(1L);
        jugador.setRiotId("Faker#KR1");
    }

    @Test
    void obtenerTop10Jugadores_conEstadisticas_deberiaRetornarRanking() {
        // Given
        when(rankingDataRepository.obtenerTodasEstadisticas())
                .thenReturn(Arrays.asList(estadistica1, estadistica2));
        when(rankingDataRepository.obtenerJugadorPorId(1L)).thenReturn(jugador);
        when(rankingDataRepository.obtenerJugadorPorId(2L)).thenThrow(new RuntimeException("Not found"));

        // When
        List<RankingDTO> resultado = rankingService.obtenerTop10Jugadores();

        // Then
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Faker#KR1", resultado.get(0).getRiotId());
        assertEquals(7.5, resultado.get(0).getKdaPromedio());
    }

    @Test
    void obtenerTop10Jugadores_sinEstadisticas_deberiaRetornarListaVacia() {
        // Given
        when(rankingDataRepository.obtenerTodasEstadisticas()).thenReturn(Collections.emptyList());

        // When
        List<RankingDTO> resultado = rankingService.obtenerTop10Jugadores();

        // Then
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void obtenerTop10Jugadores_deberiaOrdenarPorKdaDescendente() {
        // Given
        when(rankingDataRepository.obtenerTodasEstadisticas())
                .thenReturn(Arrays.asList(estadistica1, estadistica2));
        when(rankingDataRepository.obtenerJugadorPorId(anyLong())).thenThrow(new RuntimeException());

        // When
        List<RankingDTO> resultado = rankingService.obtenerTop10Jugadores();

        // Then
        assertTrue(resultado.get(0).getKdaPromedio() >= resultado.get(1).getKdaPromedio());
    }

    @Test
    void obtenerTop10Jugadores_conJugadorNoEncontrado_deberiaUsarDesconocido() {
        // Given
        when(rankingDataRepository.obtenerTodasEstadisticas())
                .thenReturn(Arrays.asList(estadistica1));
        when(rankingDataRepository.obtenerJugadorPorId(1L)).thenThrow(new RuntimeException("Not found"));

        // When
        List<RankingDTO> resultado = rankingService.obtenerTop10Jugadores();

        // Then
        assertEquals("Desconocido", resultado.get(0).getRiotId());
    }
}
