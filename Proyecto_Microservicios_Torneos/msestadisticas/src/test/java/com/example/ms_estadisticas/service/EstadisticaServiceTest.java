package com.example.ms_estadisticas.service;

import com.example.ms_estadisticas.client.JugadorClient;
import com.example.ms_estadisticas.client.PartidaClient;
import com.example.ms_estadisticas.dto.EstadisticaRequestDTO;
import com.example.ms_estadisticas.dto.PartidaDTO;
import com.example.ms_estadisticas.entity.Estadistica;
import com.example.ms_estadisticas.exception.BusinessRuleException;
import com.example.ms_estadisticas.repository.EstadisticaRepository;
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
class EstadisticaServiceTest {

    @Mock
    private EstadisticaRepository estadisticaRepository;

    @Mock
    private JugadorClient jugadorClient;

    @Mock
    private PartidaClient partidaClient;

    @InjectMocks
    private EstadisticaService estadisticaService;

    private EstadisticaRequestDTO dto;
    private Estadistica estadistica;

    @BeforeEach
    void setUp() {
        dto = new EstadisticaRequestDTO();
        dto.setIdPartida(1L);
        dto.setIdJugador(1L);
        dto.setKills(10);
        dto.setDeaths(2);
        dto.setAssists(5);

        estadistica = new Estadistica();
        estadistica.setId(1L);
        estadistica.setIdPartida(1L);
        estadistica.setIdJugador(1L);
        estadistica.setKills(10);
        estadistica.setDeaths(2);
        estadistica.setAssists(5);
        estadistica.setKda(7.5);
    }

    @Test
    void registrarEstadistica_conDatosValidos_deberiaRegistrar() {
        // Given
        when(estadisticaRepository.existsByIdPartidaAndIdJugador(1L, 1L)).thenReturn(false);
        when(partidaClient.obtenerPartidaPorId(1L)).thenReturn(new PartidaDTO());
        when(jugadorClient.obtenerJugadorPorId(1L)).thenReturn(new Object());
        when(estadisticaRepository.save(any(Estadistica.class))).thenReturn(estadistica);

        // When
        Estadistica resultado = estadisticaService.registrarEstadistica(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(7.5, resultado.getKda());
        verify(estadisticaRepository).save(any(Estadistica.class));
    }

    @Test
    void registrarEstadistica_conDuplicado_deberiaLanzarExcepcion() {
        // Given
        when(estadisticaRepository.existsByIdPartidaAndIdJugador(1L, 1L)).thenReturn(true);

        // When & Then
        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> estadisticaService.registrarEstadistica(dto));
        assertTrue(ex.getMessage().contains("ya tiene estadísticas"));
        verify(estadisticaRepository, never()).save(any());
    }

    @Test
    void registrarEstadistica_conCeroMuertes_deberiaCalcularKdaCorrectamente() {
        // Given
        dto.setDeaths(0);
        when(estadisticaRepository.existsByIdPartidaAndIdJugador(1L, 1L)).thenReturn(false);
        when(partidaClient.obtenerPartidaPorId(1L)).thenReturn(new PartidaDTO());
        when(jugadorClient.obtenerJugadorPorId(1L)).thenReturn(new Object());
        when(estadisticaRepository.save(any(Estadistica.class))).thenAnswer(inv -> {
            Estadistica e = inv.getArgument(0);
            e.setId(1L);
            return e;
        });

        // When
        Estadistica resultado = estadisticaService.registrarEstadistica(dto);

        // Then
        assertEquals(15.0, resultado.getKda()); // kills + assists = 10 + 5
    }

    @Test
    void obtenerTodas_deberiaRetornarLista() {
        // Given
        when(estadisticaRepository.findAll()).thenReturn(Arrays.asList(estadistica));

        // When
        List<Estadistica> resultado = estadisticaService.obtenerTodas();

        // Then
        assertEquals(1, resultado.size());
    }

    @Test
    void obtenerPorId_conIdExistente_deberiaRetornar() {
        // Given
        when(estadisticaRepository.findById(1L)).thenReturn(Optional.of(estadistica));

        // When
        Estadistica resultado = estadisticaService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void obtenerPorPartida_deberiaRetornarLista() {
        // Given
        when(estadisticaRepository.findByIdPartida(1L)).thenReturn(Arrays.asList(estadistica));

        // When
        List<Estadistica> resultado = estadisticaService.obtenerPorPartida(1L);

        // Then
        assertEquals(1, resultado.size());
    }

    @Test
    void eliminarEstadistica_conIdExistente_deberiaEliminar() {
        // Given
        when(estadisticaRepository.findById(1L)).thenReturn(Optional.of(estadistica));

        // When
        estadisticaService.eliminarEstadistica(1L);

        // Then
        verify(estadisticaRepository).delete(estadistica);
    }
}
