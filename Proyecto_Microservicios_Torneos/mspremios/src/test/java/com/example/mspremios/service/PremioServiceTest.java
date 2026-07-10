package com.example.mspremios.service;

import com.example.mspremios.client.TorneoClient;
import com.example.mspremios.dto.PremioDTO;
import com.example.mspremios.exception.BusinessRuleException;
import com.example.mspremios.model.Premio;
import com.example.mspremios.repository.PremioRepository;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PremioServiceTest {

    @Mock
    private PremioRepository premioRepository;

    @Mock
    private TorneoClient torneoClient;

    @InjectMocks
    private PremioService premioService;

    private PremioDTO dto;
    private Premio premio;

    @BeforeEach
    void setUp() {
        dto = new PremioDTO();
        dto.setTorneoId(1L);
        dto.setMontoTotal(10000.0);
        dto.setPosicionDestino("1er Lugar");
        dto.setPorcentajeDistribucion(50.0);

        premio = new Premio();
        premio.setId(1L);
        premio.setTorneoId(1L);
        premio.setMontoTotal(10000.0);
        premio.setPosicionDestino("1er Lugar");
        premio.setPorcentajeDistribucion(50.0);
    }

    @Test
    void guardarPremio_conDatosValidos_deberiaGuardarPremio() {
        // Given
        when(torneoClient.obtenerTorneoPorId(1L)).thenReturn(new Object());
        when(premioRepository.save(any(Premio.class))).thenReturn(premio);

        // When
        Premio resultado = premioService.guardarPremio(dto);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getTorneoId());
        assertEquals(10000.0, resultado.getMontoTotal());
        verify(premioRepository).save(any(Premio.class));
    }

    @Test
    void guardarPremio_conTorneoInexistente_deberiaLanzarExcepcion() {
        // Given
        Request request = Request.create(Request.HttpMethod.GET, "/test",
                Collections.emptyMap(), null, new RequestTemplate());
        when(torneoClient.obtenerTorneoPorId(1L))
                .thenThrow(new FeignException.NotFound("Not found", request, null, null));

        // When & Then
        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> premioService.guardarPremio(dto));
        assertTrue(ex.getMessage().contains("Torneo especificado no existe"));
        verify(premioRepository, never()).save(any());
    }

    @Test
    void guardarPremio_conErrorDeConexion_deberiaLanzarExcepcion() {
        // Given
        Request request = Request.create(Request.HttpMethod.GET, "/test",
                Collections.emptyMap(), null, new RequestTemplate());
        when(torneoClient.obtenerTorneoPorId(1L))
                .thenThrow(new FeignException.ServiceUnavailable("Service down", request, null, null));

        // When & Then
        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> premioService.guardarPremio(dto));
        assertTrue(ex.getMessage().contains("Error al comunicarse"));
        verify(premioRepository, never()).save(any());
    }

    @Test
    void obtenerPremiosPorTorneo_deberiaRetornarLista() {
        // Given
        when(premioRepository.findByTorneoId(1L)).thenReturn(Arrays.asList(premio));

        // When
        List<Premio> resultado = premioService.obtenerPremiosPorTorneo(1L);

        // Then
        assertEquals(1, resultado.size());
        verify(premioRepository).findByTorneoId(1L);
    }

    @Test
    void obtenerTodos_deberiaRetornarLista() {
        // Given
        when(premioRepository.findAll()).thenReturn(Arrays.asList(premio));

        // When
        List<Premio> resultado = premioService.obtenerTodos();

        // Then
        assertEquals(1, resultado.size());
    }

    @Test
    void obtenerPorId_conIdExistente_deberiaRetornarPremio() {
        // Given
        when(premioRepository.findById(1L)).thenReturn(Optional.of(premio));

        // When
        Premio resultado = premioService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void obtenerPorId_conIdInexistente_deberiaLanzarExcepcion() {
        // Given
        when(premioRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BusinessRuleException.class,
                () -> premioService.obtenerPorId(99L));
    }
}
