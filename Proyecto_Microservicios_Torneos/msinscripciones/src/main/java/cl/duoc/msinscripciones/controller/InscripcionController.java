package cl.duoc.msinscripciones.controller;

import cl.duoc.msinscripciones.dto.InscripcionRequestDTO;
import cl.duoc.msinscripciones.entity.Inscripcion;
import cl.duoc.msinscripciones.service.InscripcionService;
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
@RequestMapping("/api/v1/inscripciones")
@RequiredArgsConstructor
@Tag(name = "Inscripciones", description = "Gestión de inscripciones de equipos a torneos")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @PostMapping
    @Operation(summary = "Crear inscripción", description = "Registra la inscripción de un equipo en un torneo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Inscripción creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Violación de regla de negocio")
    })
    public ResponseEntity<EntityModel<Inscripcion>> crearInscripcion(@Valid @RequestBody InscripcionRequestDTO dto) {
        Inscripcion inscripcion = inscripcionService.registrarInscripcion(dto);
        return new ResponseEntity<>(toEntityModel(inscripcion), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar inscripciones", description = "Obtiene todas las inscripciones registradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Inscripcion>>> listarInscripciones() {
        List<EntityModel<Inscripcion>> inscripciones = inscripcionService.obtenerTodas().stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(inscripciones,
                linkTo(methodOn(InscripcionController.class).listarInscripciones()).withSelfRel()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener inscripción por ID", description = "Busca una inscripción por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Inscripción encontrada"),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
    })
    public ResponseEntity<EntityModel<Inscripcion>> obtenerInscripcionPorId(@PathVariable Long id) {
        return new ResponseEntity<>(toEntityModel(inscripcionService.obtenerPorId(id)), HttpStatus.OK);
    }

    @GetMapping("/torneo/{idTorneo}")
    @Operation(summary = "Listar inscripciones por torneo", description = "Obtiene las inscripciones de un torneo específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Inscripcion>>> listarPorTorneo(@PathVariable Long idTorneo) {
        List<EntityModel<Inscripcion>> inscripciones = inscripcionService.obtenerPorTorneo(idTorneo).stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(inscripciones,
                linkTo(methodOn(InscripcionController.class).listarPorTorneo(idTorneo)).withSelfRel()), HttpStatus.OK);
    }

    @GetMapping("/equipo/{idEquipo}")
    @Operation(summary = "Listar inscripciones por equipo", description = "Obtiene las inscripciones de un equipo específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Inscripcion>>> listarPorEquipo(@PathVariable Long idEquipo) {
        List<EntityModel<Inscripcion>> inscripciones = inscripcionService.obtenerPorEquipo(idEquipo).stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(inscripciones,
                linkTo(methodOn(InscripcionController.class).listarPorEquipo(idEquipo)).withSelfRel()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar inscripción", description = "Elimina una inscripción por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Inscripción eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Inscripción no encontrada")
    })
    public ResponseEntity<Void> eliminarInscripcion(@PathVariable Long id) {
        inscripcionService.eliminarInscripcion(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private EntityModel<Inscripcion> toEntityModel(Inscripcion inscripcion) {
        return EntityModel.of(inscripcion,
                linkTo(methodOn(InscripcionController.class).obtenerInscripcionPorId(inscripcion.getId())).withSelfRel(),
                linkTo(methodOn(InscripcionController.class).listarInscripciones()).withRel("inscripciones"));
    }
}
