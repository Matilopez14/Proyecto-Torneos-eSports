package cl.duoc.msinscripciones.service;

import cl.duoc.msinscripciones.client.EquipoClient;
import cl.duoc.msinscripciones.client.TorneoClient;
import cl.duoc.msinscripciones.dto.InscripcionRequestDTO;
import cl.duoc.msinscripciones.entity.Inscripcion;
import cl.duoc.msinscripciones.exception.BusinessRuleException;
import cl.duoc.msinscripciones.repository.InscripcionRepository;
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
class InscripcionServiceTest {

    @Mock
    private InscripcionRepository inscripcionRepository;

    @Mock
    private TorneoClient torneoClient;

    @Mock
    private EquipoClient equipoClient;

    @InjectMocks
    private InscripcionService inscripcionService;

    private InscripcionRequestDTO dto;
    private Inscripcion inscripcion;

    @BeforeEach
    void setUp() {
        dto = new InscripcionRequestDTO();
        dto.setIdTorneo(1L);
        dto.setIdEquipo(1L);

        inscripcion = new Inscripcion();
        inscripcion.setId(1L);
        inscripcion.setIdTorneo(1L);
        inscripcion.setIdEquipo(1L);
    }

    @Test
    void registrarInscripcion_conDatosValidos_deberiaRegistrar() {
        // Given
        when(inscripcionRepository.existsByIdTorneoAndIdEquipo(1L, 1L)).thenReturn(false);
        when(torneoClient.obtenerTorneoPorId(1L)).thenReturn(new Object());
        when(equipoClient.obtenerEquipoPorId(1L)).thenReturn(new Object());
        when(inscripcionRepository.save(any(Inscripcion.class))).thenReturn(inscripcion);

        // When
        Inscripcion resultado = inscripcionService.registrarInscripcion(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdTorneo());
        verify(inscripcionRepository).save(any(Inscripcion.class));
    }

    @Test
    void registrarInscripcion_conEquipoYaInscrito_deberiaLanzarExcepcion() {
        // Given
        when(inscripcionRepository.existsByIdTorneoAndIdEquipo(1L, 1L)).thenReturn(true);

        // When & Then
        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> inscripcionService.registrarInscripcion(dto));
        assertTrue(ex.getMessage().contains("ya se encuentra inscrito"));
        verify(inscripcionRepository, never()).save(any());
    }

    @Test
    void obtenerTodas_deberiaRetornarLista() {
        // Given
        when(inscripcionRepository.findAll()).thenReturn(Arrays.asList(inscripcion));

        // When
        List<Inscripcion> resultado = inscripcionService.obtenerTodas();

        // Then
        assertEquals(1, resultado.size());
    }

    @Test
    void obtenerPorId_conIdExistente_deberiaRetornar() {
        // Given
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(inscripcion));

        // When
        Inscripcion resultado = inscripcionService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void obtenerPorTorneo_deberiaRetornarLista() {
        // Given
        when(inscripcionRepository.findByIdTorneo(1L)).thenReturn(Arrays.asList(inscripcion));

        // When
        List<Inscripcion> resultado = inscripcionService.obtenerPorTorneo(1L);

        // Then
        assertEquals(1, resultado.size());
    }

    @Test
    void obtenerPorEquipo_deberiaRetornarLista() {
        // Given
        when(inscripcionRepository.findByIdEquipo(1L)).thenReturn(Arrays.asList(inscripcion));

        // When
        List<Inscripcion> resultado = inscripcionService.obtenerPorEquipo(1L);

        // Then
        assertEquals(1, resultado.size());
    }

    @Test
    void eliminarInscripcion_conIdExistente_deberiaEliminar() {
        // Given
        when(inscripcionRepository.findById(1L)).thenReturn(Optional.of(inscripcion));

        // When
        inscripcionService.eliminarInscripcion(1L);

        // Then
        verify(inscripcionRepository).delete(inscripcion);
    }
}
