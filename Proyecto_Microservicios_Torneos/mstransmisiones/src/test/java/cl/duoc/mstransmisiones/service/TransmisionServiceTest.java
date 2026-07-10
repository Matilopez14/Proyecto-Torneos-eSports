package cl.duoc.mstransmisiones.service;

import cl.duoc.mstransmisiones.client.PartidaClient;
import cl.duoc.mstransmisiones.dto.PartidaDTO;
import cl.duoc.mstransmisiones.dto.TransmisionRequestDTO;
import cl.duoc.mstransmisiones.entity.Transmision;
import cl.duoc.mstransmisiones.exception.BusinessRuleException;
import cl.duoc.mstransmisiones.repository.TransmisionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransmisionServiceTest {

    @Mock
    private TransmisionRepository transmisionRepository;

    @Mock
    private PartidaClient partidaClient;

    @InjectMocks
    private TransmisionService transmisionService;

    private TransmisionRequestDTO dto;
    private Transmision transmision;

    @BeforeEach
    void setUp() {
        dto = new TransmisionRequestDTO();
        dto.setIdPartida(1L);
        dto.setPlataforma("Twitch");
        dto.setUrl("https://twitch.tv/torneo");
        dto.setEstado("PROGRAMADA");
        dto.setFechaInicio(LocalDateTime.now());

        transmision = new Transmision();
        transmision.setId(1L);
        transmision.setIdPartida(1L);
        transmision.setPlataforma("Twitch");
        transmision.setUrl("https://twitch.tv/torneo");
        transmision.setEstado("PROGRAMADA");
    }

    @Test
    void crearTransmision_conDatosValidos_deberiaCrear() {
        // Given
        when(partidaClient.obtenerPartidaPorId(1L)).thenReturn(new PartidaDTO());
        when(transmisionRepository.save(any(Transmision.class))).thenReturn(transmision);

        // When
        Transmision resultado = transmisionService.crearTransmision(dto);

        // Then
        assertNotNull(resultado);
        assertEquals("Twitch", resultado.getPlataforma());
        verify(transmisionRepository).save(any(Transmision.class));
    }

    @Test
    void crearTransmision_conPartidaInexistente_deberiaLanzarExcepcion() {
        // Given
        when(partidaClient.obtenerPartidaPorId(1L)).thenThrow(new RuntimeException("Not found"));

        // When & Then
        assertThrows(BusinessRuleException.class,
                () -> transmisionService.crearTransmision(dto));
        verify(transmisionRepository, never()).save(any());
    }

    @Test
    void obtenerTodas_deberiaRetornarLista() {
        // Given
        when(transmisionRepository.findAll()).thenReturn(Arrays.asList(transmision));

        // When
        List<Transmision> resultado = transmisionService.obtenerTodas();

        // Then
        assertEquals(1, resultado.size());
    }

    @Test
    void obtenerPorId_conIdExistente_deberiaRetornar() {
        // Given
        when(transmisionRepository.findById(1L)).thenReturn(Optional.of(transmision));

        // When
        Transmision resultado = transmisionService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void obtenerPorId_conIdInexistente_deberiaLanzarExcepcion() {
        // Given
        when(transmisionRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BusinessRuleException.class,
                () -> transmisionService.obtenerPorId(99L));
    }

    @Test
    void eliminarTransmision_conIdExistente_deberiaEliminar() {
        // Given
        when(transmisionRepository.existsById(1L)).thenReturn(true);

        // When
        transmisionService.eliminarTransmision(1L);

        // Then
        verify(transmisionRepository).deleteById(1L);
    }

    @Test
    void eliminarTransmision_conIdInexistente_deberiaLanzarExcepcion() {
        // Given
        when(transmisionRepository.existsById(99L)).thenReturn(false);

        // When & Then
        assertThrows(BusinessRuleException.class,
                () -> transmisionService.eliminarTransmision(99L));
    }
}
