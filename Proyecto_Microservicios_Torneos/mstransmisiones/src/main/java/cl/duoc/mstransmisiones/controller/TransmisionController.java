package cl.duoc.mstransmisiones.controller;

import cl.duoc.mstransmisiones.dto.TransmisionRequestDTO;
import cl.duoc.mstransmisiones.entity.Transmision;
import cl.duoc.mstransmisiones.service.TransmisionService;
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
@RequestMapping("/api/v1/transmisiones")
@RequiredArgsConstructor
@Tag(name = "Transmisiones", description = "Gestión de transmisiones en vivo de partidas")
public class TransmisionController {

    private final TransmisionService transmisionService;

    @PostMapping
    @Operation(summary = "Crear transmisión", description = "Registra una nueva transmisión para una partida")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transmisión creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Violación de regla de negocio")
    })
    public ResponseEntity<EntityModel<Transmision>> crearTransmision(@Valid @RequestBody TransmisionRequestDTO dto) {
        Transmision transmision = transmisionService.crearTransmision(dto);
        return new ResponseEntity<>(toEntityModel(transmision), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar transmisiones", description = "Obtiene todas las transmisiones registradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Transmision>>> listarTodas() {
        List<EntityModel<Transmision>> transmisiones = transmisionService.obtenerTodas().stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(transmisiones,
                linkTo(methodOn(TransmisionController.class).listarTodas()).withSelfRel()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener transmisión por ID", description = "Busca una transmisión por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transmisión encontrada"),
            @ApiResponse(responseCode = "404", description = "Transmisión no encontrada")
    })
    public ResponseEntity<EntityModel<Transmision>> obtenerPorId(@PathVariable Long id) {
        return new ResponseEntity<>(toEntityModel(transmisionService.obtenerPorId(id)), HttpStatus.OK);
    }

    @GetMapping("/partida/{idPartida}")
    @Operation(summary = "Listar transmisiones por partida", description = "Obtiene las transmisiones de una partida específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Transmision>>> listarPorPartida(@PathVariable Long idPartida) {
        List<EntityModel<Transmision>> transmisiones = transmisionService.obtenerPorPartida(idPartida).stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(CollectionModel.of(transmisiones,
                linkTo(methodOn(TransmisionController.class).listarPorPartida(idPartida)).withSelfRel()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar transmisión", description = "Elimina una transmisión por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Transmisión eliminada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Transmisión no encontrada")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        transmisionService.eliminarTransmision(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private EntityModel<Transmision> toEntityModel(Transmision transmision) {
        return EntityModel.of(transmision,
                linkTo(methodOn(TransmisionController.class).obtenerPorId(transmision.getId())).withSelfRel(),
                linkTo(methodOn(TransmisionController.class).listarTodas()).withRel("transmisiones"));
    }
}
