package cl.duoc.mstorneos.service;

import cl.duoc.mstorneos.dto.TorneoRequestDTO;
import cl.duoc.mstorneos.entity.Torneo;
import cl.duoc.mstorneos.exception.BusinessRuleException;
import cl.duoc.mstorneos.repository.TorneoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TorneoServiceTest {

    @Mock
    private TorneoRepository torneoRepository;

    @InjectMocks
    private TorneoService torneoService;

    private TorneoRequestDTO dto;
    private Torneo torneo;

    @BeforeEach
    void setUp() {
        dto = new TorneoRequestDTO();
        dto.setNombre("Copa Primavera");
        dto.setFechaInicio(LocalDate.of(2026, 8, 1));
        dto.setFechaFin(LocalDate.of(2026, 8, 15));
        dto.setPrizePool(50000.0);

        torneo = new Torneo();
        torneo.setId(1L);
        torneo.setNombre("Copa Primavera");
        torneo.setFechaInicio(LocalDate.of(2026, 8, 1));
        torneo.setFechaFin(LocalDate.of(2026, 8, 15));
        torneo.setPrizePool(50000.0);
        torneo.setEstado("ABIERTO");
    }

    @Test
    void crearTorneo_conDatosValidos_deberiaCrearTorneo() {
        // Given
        when(torneoRepository.existsByNombre("Copa Primavera")).thenReturn(false);
        when(torneoRepository.save(any(Torneo.class))).thenReturn(torneo);

        // When
        Torneo resultado = torneoService.crearTorneo(dto);

        // Then
        assertNotNull(resultado);
        assertEquals("Copa Primavera", resultado.getNombre());
        assertEquals("ABIERTO", resultado.getEstado());
        verify(torneoRepository).save(any(Torneo.class));
    }

    @Test
    void crearTorneo_conNombreDuplicado_deberiaLanzarExcepcion() {
        // Given
        when(torneoRepository.existsByNombre("Copa Primavera")).thenReturn(true);

        // When & Then
        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> torneoService.crearTorneo(dto));
        assertEquals("Ya existe un torneo con ese nombre.", ex.getMessage());
        verify(torneoRepository, never()).save(any());
    }

    @Test
    void crearTorneo_conFechasInvalidas_deberiaLanzarExcepcion() {
        // Given
        dto.setFechaFin(LocalDate.of(2026, 7, 1));
        when(torneoRepository.existsByNombre("Copa Primavera")).thenReturn(false);

        // When & Then
        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> torneoService.crearTorneo(dto));
        assertTrue(ex.getMessage().contains("fecha de fin"));
        verify(torneoRepository, never()).save(any());
    }

    @Test
    void obtenerTorneos_deberiaRetornarLista() {
        // Given
        when(torneoRepository.findAll()).thenReturn(Arrays.asList(torneo));

        // When
        List<Torneo> resultado = torneoService.obtenerTorneos();

        // Then
        assertEquals(1, resultado.size());
        verify(torneoRepository).findAll();
    }

    @Test
    void obtenerTorneoPorId_conIdExistente_deberiaRetornarTorneo() {
        // Given
        when(torneoRepository.findById(1L)).thenReturn(Optional.of(torneo));

        // When
        Torneo resultado = torneoService.obtenerTorneoPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void obtenerTorneoPorId_conIdInexistente_deberiaLanzarExcepcion() {
        // Given
        when(torneoRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BusinessRuleException.class,
                () -> torneoService.obtenerTorneoPorId(99L));
    }

    @Test
    void cambiarEstado_deAbiertoAEnCurso_deberiaActualizar() {
        // Given
        when(torneoRepository.findById(1L)).thenReturn(Optional.of(torneo));
        when(torneoRepository.save(any(Torneo.class))).thenReturn(torneo);

        // When
        Torneo resultado = torneoService.cambiarEstado(1L, "EN_CURSO");

        // Then
        verify(torneoRepository).save(any(Torneo.class));
    }

    @Test
    void cambiarEstado_deAbiertoAFinalizado_deberiaLanzarExcepcion() {
        // Given
        when(torneoRepository.findById(1L)).thenReturn(Optional.of(torneo));

        // When & Then
        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> torneoService.cambiarEstado(1L, "FINALIZADO"));
        assertTrue(ex.getMessage().contains("No se puede cambiar el estado"));
        verify(torneoRepository, never()).save(any());
    }

    @Test
    void cambiarEstado_deFinalizadoACualquierEstado_deberiaLanzarExcepcion() {
        // Given
        torneo.setEstado("FINALIZADO");
        when(torneoRepository.findById(1L)).thenReturn(Optional.of(torneo));

        // When & Then
        assertThrows(BusinessRuleException.class,
                () -> torneoService.cambiarEstado(1L, "ABIERTO"));
        verify(torneoRepository, never()).save(any());
    }
}
