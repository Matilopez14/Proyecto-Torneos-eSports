package cl.duoc.msequipos.service;

import cl.duoc.msequipos.client.JugadorClient;
import cl.duoc.msequipos.dto.EquipoRequestDTO;
import cl.duoc.msequipos.entity.Equipo;
import cl.duoc.msequipos.exception.BusinessRuleException;
import cl.duoc.msequipos.repository.EquipoRepository;
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
class EquipoServiceTest {

    @Mock
    private EquipoRepository equipoRepository;

    @Mock
    private JugadorClient jugadorClient;

    @InjectMocks
    private EquipoService equipoService;

    private EquipoRequestDTO dto;
    private Equipo equipo;

    @BeforeEach
    void setUp() {
        dto = new EquipoRequestDTO();
        dto.setNombre("Team Liquid");
        dto.setTagAcronimo("tl");
        dto.setRegion("NA");
        dto.setIdCapitan(1L);

        equipo = new Equipo();
        equipo.setId(1L);
        equipo.setNombre("Team Liquid");
        equipo.setTagAcronimo("TL");
        equipo.setRegion("NA");
        equipo.setIdCapitan(1L);
    }

    @Test
    void crearEquipo_conDatosValidos_deberiaCrearEquipo() {
        // Given
        when(jugadorClient.obtenerJugadorPorId(1L)).thenReturn(new Object());
        when(equipoRepository.save(any(Equipo.class))).thenReturn(equipo);

        // When
        Equipo resultado = equipoService.crearEquipo(dto);

        // Then
        assertNotNull(resultado);
        assertEquals("Team Liquid", resultado.getNombre());
        assertEquals("TL", resultado.getTagAcronimo());
        verify(equipoRepository).save(any(Equipo.class));
    }

    @Test
    void crearEquipo_conCapitanInexistente_deberiaLanzarExcepcion() {
        // Given
        when(jugadorClient.obtenerJugadorPorId(1L)).thenThrow(new RuntimeException("Not found"));

        // When & Then
        BusinessRuleException ex = assertThrows(BusinessRuleException.class,
                () -> equipoService.crearEquipo(dto));
        assertTrue(ex.getMessage().contains("capitán no existe"));
        verify(equipoRepository, never()).save(any());
    }

    @Test
    void crearEquipo_deberiaConvertirTagAMayusculas() {
        // Given
        dto.setTagAcronimo("abc");
        when(jugadorClient.obtenerJugadorPorId(1L)).thenReturn(new Object());
        when(equipoRepository.save(any(Equipo.class))).thenAnswer(invocation -> {
            Equipo e = invocation.getArgument(0);
            e.setId(1L);
            return e;
        });

        // When
        Equipo resultado = equipoService.crearEquipo(dto);

        // Then
        assertEquals("ABC", resultado.getTagAcronimo());
    }

    @Test
    void listarTodos_deberiaRetornarLista() {
        // Given
        when(equipoRepository.findAll()).thenReturn(Arrays.asList(equipo));

        // When
        List<Equipo> resultado = equipoService.listarTodos();

        // Then
        assertEquals(1, resultado.size());
        verify(equipoRepository).findAll();
    }

    @Test
    void obtenerPorId_conIdExistente_deberiaRetornarEquipo() {
        // Given
        when(equipoRepository.findById(1L)).thenReturn(Optional.of(equipo));

        // When
        Equipo resultado = equipoService.obtenerPorId(1L);

        // Then
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    void obtenerPorId_conIdInexistente_deberiaLanzarExcepcion() {
        // Given
        when(equipoRepository.findById(99L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(BusinessRuleException.class,
                () -> equipoService.obtenerPorId(99L));
    }
}
