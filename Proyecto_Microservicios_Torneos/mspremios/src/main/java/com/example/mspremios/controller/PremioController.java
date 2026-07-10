package com.example.mspremios.controller;

import com.example.mspremios.dto.PremioDTO;
import com.example.mspremios.model.Premio;
import com.example.mspremios.service.PremioService;
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
@RequestMapping("/api/v1/premios")
@RequiredArgsConstructor
@Tag(name = "Premios", description = "Gestión de premios de torneos de eSports")
public class PremioController {

    private final PremioService premioService;

    @PostMapping
    @Operation(summary = "Crear un premio", description = "Registra un nuevo premio asociado a un torneo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Premio creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Violación de regla de negocio")
    })
    public ResponseEntity<EntityModel<Premio>> crearPremio(@Valid @RequestBody PremioDTO premioDTO) {
        Premio nuevoPremio = premioService.guardarPremio(premioDTO);
        return new ResponseEntity<>(toEntityModel(nuevoPremio), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar premios", description = "Obtiene la lista completa de premios registrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de premios obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Premio>>> listarTodos() {
        List<EntityModel<Premio>> premios = premioService.obtenerTodos().stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(premios,
                linkTo(methodOn(PremioController.class).listarTodos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener premio por ID", description = "Busca un premio específico por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Premio encontrado"),
            @ApiResponse(responseCode = "404", description = "Premio no encontrado")
    })
    public ResponseEntity<EntityModel<Premio>> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(toEntityModel(premioService.obtenerPorId(id)));
    }

    @GetMapping("/torneo/{torneoId}")
    @Operation(summary = "Listar premios por torneo", description = "Obtiene los premios asociados a un torneo específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de premios del torneo obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Premio>>> listarPorTorneo(@PathVariable Long torneoId) {
        List<EntityModel<Premio>> premios = premioService.obtenerPremiosPorTorneo(torneoId).stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(premios,
                linkTo(methodOn(PremioController.class).listarPorTorneo(torneoId)).withSelfRel()));
    }

    private EntityModel<Premio> toEntityModel(Premio premio) {
        return EntityModel.of(premio,
                linkTo(methodOn(PremioController.class).obtenerPorId(premio.getId())).withSelfRel(),
                linkTo(methodOn(PremioController.class).listarTodos()).withRel("premios"));
    }
}
