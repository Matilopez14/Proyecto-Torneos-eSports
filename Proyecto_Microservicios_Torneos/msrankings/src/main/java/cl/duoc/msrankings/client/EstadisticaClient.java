package cl.duoc.msrankings.client;

import cl.duoc.msrankings.dto.EstadisticaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "msestadisticas", path = "/api/v1/estadisticas")
public interface EstadisticaClient {

    @GetMapping
    List<EstadisticaDTO> listarTodas();

    @GetMapping("/jugador/{idJugador}")
    List<EstadisticaDTO> listarPorJugador(@PathVariable("idJugador") Long idJugador);
}
