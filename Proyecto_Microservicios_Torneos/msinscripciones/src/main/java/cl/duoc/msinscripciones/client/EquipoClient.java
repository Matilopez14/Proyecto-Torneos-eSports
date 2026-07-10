package cl.duoc.msinscripciones.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msequipos", path = "/api/v1/equipos")
public interface EquipoClient {

    @GetMapping("/{id}")
    Object obtenerEquipoPorId(@PathVariable("id") Long id);
}
