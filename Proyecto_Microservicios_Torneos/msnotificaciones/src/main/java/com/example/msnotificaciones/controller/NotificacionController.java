package com.example.msnotificaciones.controller;

import com.example.msnotificaciones.dto.NotificacionDTO;
import com.example.msnotificaciones.model.Notificacion;
import com.example.msnotificaciones.service.NotificacionService;
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
@RequestMapping("/api/v1/notificaciones")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Gestión de notificaciones y alertas")
public class NotificacionController {

    private final NotificacionService notificacionService;

    @PostMapping
    @Operation(summary = "Enviar notificación", description = "Registra y envía una nueva notificación")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Notificación enviada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<EntityModel<Notificacion>> enviarNotificacion(@Valid @RequestBody NotificacionDTO dto) {
        Notificacion nueva = notificacionService.registrarYEnviar(dto);
        return new ResponseEntity<>(toEntityModel(nueva), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Listar notificaciones", description = "Obtiene todas las notificaciones registradas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    })
    public ResponseEntity<CollectionModel<EntityModel<Notificacion>>> listarNotificaciones() {
        List<EntityModel<Notificacion>> notificaciones = notificacionService.obtenerTodas().stream()
                .map(this::toEntityModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(notificaciones,
                linkTo(methodOn(NotificacionController.class).listarNotificaciones()).withSelfRel()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener notificación por ID", description = "Busca una notificación por su identificador")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
            @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    public ResponseEntity<EntityModel<Notificacion>> obtenerPorId(@PathVariable Long id) {
        Notificacion notificacion = notificacionService.obtenerPorId(id);
        return ResponseEntity.ok(toEntityModel(notificacion));
    }

    private EntityModel<Notificacion> toEntityModel(Notificacion notificacion) {
        return EntityModel.of(notificacion,
                linkTo(methodOn(NotificacionController.class).obtenerPorId(notificacion.getId())).withSelfRel(),
                linkTo(methodOn(NotificacionController.class).listarNotificaciones()).withRel("notificaciones"));
    }
}
