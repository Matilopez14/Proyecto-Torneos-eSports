package com.example.msnotificaciones.service;

import com.example.msnotificaciones.dto.NotificacionDTO;
import com.example.msnotificaciones.model.Notificacion;
import com.example.msnotificaciones.repository.NotificacionRepository;
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
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @InjectMocks
    private NotificacionService notificacionService;

    private NotificacionDTO dto;
    private Notificacion notificacion;

    @BeforeEach
    void setUp() {
        dto = new NotificacionDTO();
        dto.setDestinatario("equipo@test.com");
        dto.setMensaje("Tu inscripción fue aceptada");

        notificacion = new Notificacion();
        notificacion.setId(1L);
        notificacion.setDestinatario("equipo@test.com");
        notificacion.setMensaje("Tu inscripción fue aceptada");
        notificacion.setFechaEnvio(LocalDateTime.now());
    }

    @Test
    void registrarYEnviar_conDatosValidos_deberiaRegistrar() {
        // Given
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacion);

        // When
        Notificacion resultado = notificacionService.registrarYEnviar(dto);

        // Then
        assertNotNull(resultado);
        assertEquals("equipo@test.com", resultado.getDestinatario());
        verify(notificacionRepository).save(any(Notificacion.class));
    }

    @Test
    void obtenerTodas_deberiaRetornarLista() {
        // Given
        when(notificacionRepository.findAll()).thenReturn(Arrays.asList(notificacion));

        // When
        List<Notificacion> resultado = notificacionService.obtenerTodas();

        // Then
        assertEquals(1, resultado.size());
        verify(notificacionRepository).findAll();
    }

    @Test
    void obtenerPorId_conIdExistente_deberiaRetornar() {
        // Given
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacion));

        // When
        Notificacion resultado = notificacionService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void obtenerPorId_conIdInexistente_deberiaLanzarExcepcion() {
        // Given
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class,
                () -> notificacionService.obtenerPorId(99L));
    }
}
