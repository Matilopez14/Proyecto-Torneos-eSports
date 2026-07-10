package cl.duoc.mstorneos.controller;

import cl.duoc.mstorneos.dto.TorneoRequestDTO;
import cl.duoc.mstorneos.entity.Torneo;
import cl.duoc.mstorneos.service.TorneoService;
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
@RequestMapping("/api/v1/torneos")
@RequiredArgsConstructor
@Tag(name = "Torneos", description = "Gestión de torneos de eSports")
public class TorneoController {

    private final TorneoService torneoService;

    @PostMapping
    @Operation(summary = "Crear un torneo", description = "Registra un nuevo torneo en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Torneo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Violación de regla de negocio")
    })
    public ResponseEntity<EntityModel<Torneo>> crearTorneo(@Valid @RequestBody TorneoRequestDTO dto) {
        Torneo torneo = torneoService.crearTorneo(dto);
        return new ResponseEntity<>(toEntityModel(torneo), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar torneos", description = "Obtiene la lista completa de torneos registrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de torneos obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Torneo>>> listarTorneos() {
        List<EntityModel<Torneo>> torneos = torneoService.obtenerTorneos().stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(torneos,
                linkTo(methodOn(TorneoController.class).listarTorneos()).withSelfRel()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener torneo por ID", description = "Busca un torneo específico por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Torneo encontrado"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado")
    })
    public ResponseEntity<EntityModel<Torneo>> obtenerPorId(@PathVariable Long id) {
        return new ResponseEntity<>(toEntityModel(torneoService.obtenerTorneoPorId(id)), HttpStatus.OK);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado del torneo", description = "Cambia el estado del torneo respetando el flujo: ABIERTO -> EN_CURSO -> FINALIZADO")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estado cambiado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Transición de estado inválida"),
            @ApiResponse(responseCode = "404", description = "Torneo no encontrado")
    })
    public ResponseEntity<EntityModel<Torneo>> cambiarEstado(@PathVariable Long id, @RequestParam String nuevoEstado) {
        Torneo torneo = torneoService.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok(toEntityModel(torneo));
    }

    private EntityModel<Torneo> toEntityModel(Torneo torneo) {
        return EntityModel.of(torneo,
                linkTo(methodOn(TorneoController.class).obtenerPorId(torneo.getId())).withSelfRel(),
                linkTo(methodOn(TorneoController.class).listarTorneos()).withRel("torneos"));
    }
}
