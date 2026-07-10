package cl.duoc.msjugadores.controller;

import cl.duoc.msjugadores.dto.JugadorRequestDTO;
import cl.duoc.msjugadores.entity.Jugador;
import cl.duoc.msjugadores.service.JugadorService;
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
@RequestMapping("/api/v1/jugadores")
@RequiredArgsConstructor
@Tag(name = "Jugadores", description = "Gestión de jugadores de eSports")
public class JugadorController {

    private final JugadorService jugadorService;

    @PostMapping
    @Operation(summary = "Registrar un jugador", description = "Crea un nuevo jugador en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Jugador creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Violación de regla de negocio")
    })
    public ResponseEntity<EntityModel<Jugador>> registrarJugador(@Valid @RequestBody JugadorRequestDTO dto) {
        Jugador nuevoJugador = jugadorService.crearJugador(dto);
        return new ResponseEntity<>(toEntityModel(nuevoJugador), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar jugadores", description = "Obtiene la lista completa de jugadores registrados")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de jugadores obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Jugador>>> listarJugadores() {
        List<EntityModel<Jugador>> jugadores = jugadorService.obtenerTodosLosJugadores().stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(jugadores,
                linkTo(methodOn(JugadorController.class).listarJugadores()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar jugador por ID", description = "Obtiene un jugador específico por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Jugador encontrado"),
            @ApiResponse(responseCode = "404", description = "Jugador no encontrado")
    })
    public ResponseEntity<EntityModel<Jugador>> buscarPorId(@PathVariable Long id) {
        Jugador jugador = jugadorService.obtenerPorId(id);
        return ResponseEntity.ok(toEntityModel(jugador));
    }

    private EntityModel<Jugador> toEntityModel(Jugador jugador) {
        return EntityModel.of(jugador,
                linkTo(methodOn(JugadorController.class).buscarPorId(jugador.getId())).withSelfRel(),
                linkTo(methodOn(JugadorController.class).listarJugadores()).withRel("jugadores"));
    }
}
