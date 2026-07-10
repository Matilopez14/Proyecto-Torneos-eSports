package cl.duoc.mspartidas.controller;

import cl.duoc.mspartidas.dto.PartidaRequestDTO;
import cl.duoc.mspartidas.dto.ResultadoPartidaDTO;
import cl.duoc.mspartidas.entity.Partida;
import cl.duoc.mspartidas.service.PartidaService;
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
@RequestMapping("/api/v1/partidas")
@RequiredArgsConstructor
@Tag(name = "Partidas", description = "Gestión de partidas de torneos de eSports")
public class PartidaController {

    private final PartidaService partidaService;

    @PostMapping
    @Operation(summary = "Crear una partida", description = "Registra una nueva partida en un torneo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Partida creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Violación de regla de negocio")
    })
    public ResponseEntity<EntityModel<Partida>> crearPartida(@Valid @RequestBody PartidaRequestDTO dto) {
        Partida partida = partidaService.crearPartida(dto);
        return new ResponseEntity<>(toEntityModel(partida), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar partidas", description = "Obtiene todas las partidas registradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Partida>>> listarPartidas() {
        List<EntityModel<Partida>> partidas = partidaService.obtenerPartidas().stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(partidas,
                linkTo(methodOn(PartidaController.class).listarPartidas()).withSelfRel()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener partida por ID", description = "Busca una partida por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partida encontrada"),
            @ApiResponse(responseCode = "404", description = "Partida no encontrada")
    })
    public ResponseEntity<EntityModel<Partida>> obtenerPartidaPorId(@PathVariable Long id) {
        return new ResponseEntity<>(toEntityModel(partidaService.obtenerPorId(id)), HttpStatus.OK);
    }

    @GetMapping("/torneo/{idTorneo}")
    @Operation(summary = "Listar partidas por torneo", description = "Obtiene las partidas de un torneo específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Partida>>> listarPorTorneo(@PathVariable Long idTorneo) {
        List<EntityModel<Partida>> partidas = partidaService.obtenerPorTorneo(idTorneo).stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(partidas,
                linkTo(methodOn(PartidaController.class).listarPorTorneo(idTorneo)).withSelfRel()), HttpStatus.OK);
    }

    @GetMapping("/equipo/{idEquipo}")
    @Operation(summary = "Listar partidas por equipo", description = "Obtiene las partidas de un equipo específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Partida>>> listarPorEquipo(@PathVariable Long idEquipo) {
        List<EntityModel<Partida>> partidas = partidaService.obtenerPorEquipo(idEquipo).stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(partidas,
                linkTo(methodOn(PartidaController.class).listarPorEquipo(idEquipo)).withSelfRel()), HttpStatus.OK);
    }

    @PatchMapping("/{id}/iniciar")
    @Operation(summary = "Iniciar partida", description = "Cambia el estado de una partida a EN_CURSO")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partida iniciada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Estado inválido para iniciar"),
            @ApiResponse(responseCode = "404", description = "Partida no encontrada")
    })
    public ResponseEntity<EntityModel<Partida>> iniciarPartida(@PathVariable Long id) {
        return new ResponseEntity<>(toEntityModel(partidaService.iniciarPartida(id)), HttpStatus.OK);
    }

    @PatchMapping("/{id}/finalizar")
    @Operation(summary = "Finalizar partida", description = "Finaliza una partida registrando los resultados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Partida finalizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o estado incorrecto"),
            @ApiResponse(responseCode = "404", description = "Partida no encontrada")
    })
    public ResponseEntity<EntityModel<Partida>> finalizarPartida(
            @PathVariable Long id,
            @Valid @RequestBody ResultadoPartidaDTO dto
    ) {
        return new ResponseEntity<>(toEntityModel(partidaService.finalizarPartida(id, dto)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar partida", description = "Elimina una partida por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Partida eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Partida no encontrada")
    })
    public ResponseEntity<Void> eliminarPartida(@PathVariable Long id) {
        partidaService.eliminarPartida(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private EntityModel<Partida> toEntityModel(Partida partida) {
        return EntityModel.of(partida,
                linkTo(methodOn(PartidaController.class).obtenerPartidaPorId(partida.getId())).withSelfRel(),
                linkTo(methodOn(PartidaController.class).listarPartidas()).withRel("partidas"));
    }
}
