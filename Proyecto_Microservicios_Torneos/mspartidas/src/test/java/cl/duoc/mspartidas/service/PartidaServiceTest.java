package cl.duoc.mspartidas.service;

import cl.duoc.mspartidas.client.EquipoClient;
import cl.duoc.mspartidas.client.TorneoClient;
import cl.duoc.mspartidas.dto.PartidaRequestDTO;
import cl.duoc.mspartidas.dto.ResultadoPartidaDTO;
import cl.duoc.mspartidas.entity.Partida;
import cl.duoc.mspartidas.exception.BusinessRuleException;
import cl.duoc.mspartidas.repository.PartidaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartidaServiceTest {

    @Mock
    private PartidaRepository partidaRepository;

    @Mock
    private TorneoClient torneoClient;

    @Mock
    private EquipoClient equipoClient;

    @InjectMocks
    private PartidaService partidaService;

    private PartidaRequestDTO dto;
    private Partida partida;

    @BeforeEach
    void setUp() {
        dto = new PartidaRequestDTO();
        dto.setIdTorneo(1L);
        dto.setIdEquipoA(1L);
        dto.setIdEquipoB(2L);
        dto.setMapa("Summoner's Rift");

        partida = new Partida();
        partida.setId(1L);
        partida.setIdTorneo(1L);
        partida.setIdEquipoA(1L);
        partida.setIdEquipoB(2L);
        partida.setMapa("Summoner's Rift");
        partida.setEstado("PROGRAMADA");
        partida.setResultadoA(0);
        partida.setResultadoB(0);
    }

    @Test
    void crearPartida_conDatosValidos_deberiaCrear() {
        // Given
        when(torneoClient.obtenerTorneoPorId(1L)).thenReturn(new Object());
        when(equipoClient.obtenerEquipoPorId(anyLong())).thenReturn(new Object());
        when(partidaRepository.save(any(Partida.class))).thenReturn(partida);

        // When
        Partida resultado = partidaService.crearPartida(dto);

        // Then
        assertNotNull(resultado);
        assertEquals("PROGRAMADA", resultado.getEstado());
        verify(partidaRepository).save(any(Partida.class));
    }

    @Test
    void crearPartida_conMismoEquipo_deberiaLanzarExcepcion() {
        // Given
        dto.setIdEquipoB(1L);

        // When & Then
        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> partidaService.crearPartida(dto));
        assertTrue(ex.getMessage().contains("contra sí mismo"));
    }

    @Test
    void iniciarPartida_conEstadoProgramada_deberiaIniciar() {
        // Given
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));
        when(partidaRepository.save(any(Partida.class))).thenReturn(partida);

        // When
        Partida resultado = partidaService.iniciarPartida(1L);

        // Then
        verify(partidaRepository).save(any(Partida.class));
    }

    @Test
    void iniciarPartida_conEstadoNoProgamada_deberiaLanzarExcepcion() {
        // Given
        partida.setEstado("EN_CURSO");
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));

        // When & Then
        assertThrows(BusinessRuleException.class,
                () -> partidaService.iniciarPartida(1L));
    }

    @Test
    void finalizarPartida_conEstadoNoFinalizada_deberiaFinalizar() {
        // Given
        partida.setEstado("EN_CURSO");
        ResultadoPartidaDTO resultado = new ResultadoPartidaDTO();
        resultado.setResultadoA(3);
        resultado.setResultadoB(1);
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));
        when(partidaRepository.save(any(Partida.class))).thenReturn(partida);

        // When
        Partida finalizada = partidaService.finalizarPartida(1L, resultado);

        // Then
        verify(partidaRepository).save(any(Partida.class));
    }

    @Test
    void finalizarPartida_yaFinalizada_deberiaLanzarExcepcion() {
        // Given
        partida.setEstado("FINALIZADA");
        ResultadoPartidaDTO resultado = new ResultadoPartidaDTO();
        resultado.setResultadoA(3);
        resultado.setResultadoB(1);
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));

        // When & Then
        assertThrows(BusinessRuleException.class,
                () -> partidaService.finalizarPartida(1L, resultado));
    }

    @Test
    void obtenerPartidas_deberiaRetornarLista() {
        // Given
        when(partidaRepository.findAll()).thenReturn(Arrays.asList(partida));

        // When
        List<Partida> resultado = partidaService.obtenerPartidas();

        // Then
        assertEquals(1, resultado.size());
    }

    @Test
    void eliminarPartida_conIdExistente_deberiaEliminar() {
        // Given
        when(partidaRepository.findById(1L)).thenReturn(Optional.of(partida));

        // When
        partidaService.eliminarPartida(1L);

        // Then
        verify(partidaRepository).delete(partida);
    }
}
