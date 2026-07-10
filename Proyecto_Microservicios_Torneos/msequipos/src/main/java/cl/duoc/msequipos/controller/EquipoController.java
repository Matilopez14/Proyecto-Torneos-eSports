package cl.duoc.msequipos.controller;

import cl.duoc.msequipos.dto.EquipoRequestDTO;
import cl.duoc.msequipos.entity.Equipo;
import cl.duoc.msequipos.service.EquipoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@RequestMapping("/api/v1/equipos")
@Tag(name = "Equipos", description = "Gestión de equipos de eSports")
public class EquipoController {

    private final EquipoService equipoService;

    public EquipoController(EquipoService equipoService) {
        this.equipoService = equipoService;
    }

    @PostMapping
    @Operation(summary = "Registrar un equipo", description = "Crea un nuevo equipo en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Equipo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Violación de regla de negocio")
    })
    public ResponseEntity<EntityModel<Equipo>> registrarEquipo(@Valid @RequestBody EquipoRequestDTO dto) {
        Equipo equipoGuardado = equipoService.crearEquipo(dto);
        return new ResponseEntity<>(toEntityModel(equipoGuardado), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar equipos", description = "Obtiene la lista completa de equipos registrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de equipos obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Equipo>>> listarEquipos() {
        List<EntityModel<Equipo>> equipos = equipoService.listarTodos().stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(equipos,
                linkTo(methodOn(EquipoController.class).listarEquipos()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar equipo por ID", description = "Obtiene un equipo específico por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Equipo encontrado"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    public ResponseEntity<EntityModel<Equipo>> buscarPorId(@PathVariable Long id) {
        Equipo equipo = equipoService.obtenerPorId(id);
        return ResponseEntity.ok(toEntityModel(equipo));
    }

    private EntityModel<Equipo> toEntityModel(Equipo equipo) {
        return EntityModel.of(equipo,
                linkTo(methodOn(EquipoController.class).buscarPorId(equipo.getId())).withSelfRel(),
                linkTo(methodOn(EquipoController.class).listarEquipos()).withRel("equipos"));
    }
}
