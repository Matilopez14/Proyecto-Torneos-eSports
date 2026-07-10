package com.example.ms_estadisticas.controller;

import com.example.ms_estadisticas.dto.EstadisticaRequestDTO;
import com.example.ms_estadisticas.entity.Estadistica;
import com.example.ms_estadisticas.service.EstadisticaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/estadisticas")
@RequiredArgsConstructor
@Tag(name = "Estadísticas", description = "Gestión de estadísticas de jugadores por partida")
public class EstadisticaController {

    private final EstadisticaService estadisticaService;

    @PostMapping
    @Operation(summary = "Registrar estadística", description = "Registra las estadísticas de un jugador en una partida")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Estadística registrada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Violación de regla de negocio")
    })
    public ResponseEntity<EntityModel<Estadistica>> registrar(@Valid @RequestBody EstadisticaRequestDTO dto) {
        Estadistica estadistica = estadisticaService.registrarEstadistica(dto);
        return new ResponseEntity<>(toEntityModel(estadistica), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar estadísticas", description = "Obtiene todas las estadísticas registradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Estadistica>>> listar() {
        List<EntityModel<Estadistica>> estadisticas = estadisticaService.obtenerTodas().stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(estadisticas,
                linkTo(methodOn(EstadisticaController.class).listar()).withSelfRel()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener estadística por ID", description = "Busca una estadística por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estadística encontrada"),
            @ApiResponse(responseCode = "404", description = "Estadística no encontrada")
    })
    public ResponseEntity<EntityModel<Estadistica>> obtenerPorId(@PathVariable Long id) {
        return new ResponseEntity<>(toEntityModel(estadisticaService.obtenerPorId(id)), HttpStatus.OK);
    }

    @GetMapping("/partida/{idPartida}")
    @Operation(summary = "Listar estadísticas por partida", description = "Obtiene las estadísticas de una partida específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Estadistica>>> listarPorPartida(@PathVariable Long idPartida) {
        List<EntityModel<Estadistica>> estadisticas = estadisticaService.obtenerPorPartida(idPartida).stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(estadisticas,
                linkTo(methodOn(EstadisticaController.class).listarPorPartida(idPartida)).withSelfRel()), HttpStatus.OK);
    }

    @GetMapping("/jugador/{idJugador}")
    @Operation(summary = "Listar estadísticas por jugador", description = "Obtiene las estadísticas de un jugador específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Estadistica>>> listarPorJugador(@PathVariable Long idJugador) {
        List<EntityModel<Estadistica>> estadisticas = estadisticaService.obtenerPorJugador(idJugador).stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(estadisticas,
                linkTo(methodOn(EstadisticaController.class).listarPorJugador(idJugador)).withSelfRel()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar estadística", description = "Elimina una estadística por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Estadística eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Estadística no encontrada")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        estadisticaService.eliminarEstadistica(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private EntityModel<Estadistica> toEntityModel(Estadistica estadistica) {
        return EntityModel.of(estadistica,
                linkTo(methodOn(EstadisticaController.class).obtenerPorId(estadistica.getId())).withSelfRel(),
                linkTo(methodOn(EstadisticaController.class).listar()).withRel("estadisticas"));
    }
}
