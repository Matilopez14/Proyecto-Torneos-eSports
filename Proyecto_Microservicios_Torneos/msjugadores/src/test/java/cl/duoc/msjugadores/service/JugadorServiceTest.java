package cl.duoc.msjugadores.service;

import cl.duoc.msjugadores.dto.JugadorRequestDTO;
import cl.duoc.msjugadores.entity.Jugador;
import cl.duoc.msjugadores.exception.BusinessRuleException;
import cl.duoc.msjugadores.repository.JugadorRepository;
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
class JugadorServiceTest {

    @Mock
    private JugadorRepository jugadorRepository;

    @InjectMocks
    private JugadorService jugadorService;

    private JugadorRequestDTO dto;
    private Jugador jugador;

    @BeforeEach
    void setUp() {
        dto = new JugadorRequestDTO();
        dto.setRiotId("Faker#KR1");
        dto.setEmail("faker@t1.com");
        dto.setRangoActual("Challenger");
        dto.setNivel(500);

        jugador = new Jugador();
        jugador.setId(1L);
        jugador.setRiotId("Faker#KR1");
        jugador.setEmail("faker@t1.com");
        jugador.setRangoActual("Challenger");
        jugador.setNivel(500);
    }

    @Test
    void crearJugador_conDatosValidos_deberiaCrearJugador() {
        // Given
        when(jugadorRepository.existsByRiotId("Faker#KR1")).thenReturn(false);
        when(jugadorRepository.save(any(Jugador.class))).thenReturn(jugador);

        // When
        Jugador resultado = jugadorService.crearJugador(dto);

        // Then
        assertNotNull(resultado);
        assertEquals("Faker#KR1", resultado.getRiotId());
        verify(jugadorRepository).save(any(Jugador.class));
    }

    @Test
    void crearJugador_conRiotIdDuplicado_deberiaLanzarExcepcion() {
        // Given
        when(jugadorRepository.existsByRiotId("Faker#KR1")).thenReturn(true);

        // When & Then
        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> jugadorService.crearJugador(dto));
        assertTrue(ex.getMessage().contains("ya se encuentra en uso"));
        verify(jugadorRepository, never()).save(any());
    }

    @Test
    void obtenerTodosLosJugadores_deberiaRetornarLista() {
        // Given
        when(jugadorRepository.findAll()).thenReturn(Arrays.asList(jugador));

        // When
        List<Jugador> resultado = jugadorService.obtenerTodosLosJugadores();

        // Then
        assertEquals(1, resultado.size());
        verify(jugadorRepository).findAll();
    }

    @Test
    void obtenerPorId_conIdExistente_deberiaRetornarJugador() {
        // Given
        when(jugadorRepository.findById(1L)).thenReturn(Optional.of(jugador));

        // When
        Jugador resultado = jugadorService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void obtenerPorId_conIdInexistente_deberiaLanzarExcepcion() {
        // Given
        when(jugadorRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BusinessRuleException.class,
                () -> jugadorService.obtenerPorId(99L));
    }
}
