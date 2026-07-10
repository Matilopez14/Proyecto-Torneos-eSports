package cl.duoc.mstransmisiones.client;

import cl.duoc.mstransmisiones.dto.PartidaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mspartidas", path = "/api/v1/partidas")
public interface PartidaClient {

    @GetMapping("/{id}")
    PartidaDTO obtenerPartidaPorId(@PathVariable("id") Long id);
}
