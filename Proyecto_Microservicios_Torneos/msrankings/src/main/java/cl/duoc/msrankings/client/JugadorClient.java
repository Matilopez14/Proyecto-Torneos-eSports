package cl.duoc.msrankings.client;

import cl.duoc.msrankings.dto.JugadorDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "msjugadores", path = "/api/v1/jugadores")
public interface JugadorClient {

    @GetMapping
    List<JugadorDTO> listarTodos();

    @GetMapping("/{id}")
    JugadorDTO obtenerPorId(@PathVariable("id") Long id);
}
