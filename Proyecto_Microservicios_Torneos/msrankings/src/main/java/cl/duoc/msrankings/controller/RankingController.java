package cl.duoc.msrankings.controller;

import cl.duoc.msrankings.dto.RankingDTO;
import cl.duoc.msrankings.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1/rankings")
@RequiredArgsConstructor
@Tag(name = "Rankings", description = "Rankings de jugadores basados en estadísticas")
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/top10")
    @Operation(summary = "Obtener Top 10 jugadores", description = "Devuelve los 10 mejores jugadores ordenados por KDA promedio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Top 10 obtenido exitosamente"),
            @ApiResponse(responseCode = "400", description = "Error en la solicitud")
    })
    public ResponseEntity<CollectionModel<EntityModel<RankingDTO>>> obtenerTop10() {
        List<EntityModel<RankingDTO>> rankings = rankingService.obtenerTop10Jugadores().stream()
                .map(dto -> EntityModel.of(dto,
                        linkTo(methodOn(RankingController.class).obtenerTop10()).withSelfRel()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(rankings,
                linkTo(methodOn(RankingController.class).obtenerTop10()).withSelfRel()));
    }
}
