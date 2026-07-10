package com.example.ms_estadisticas.client;

import com.example.ms_estadisticas.dto.PartidaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mspartidas", path = "/api/v1/partidas")
public interface PartidaClient {
    @GetMapping("/{id}")
    PartidaDTO obtenerPartidaPorId(@PathVariable("id") Long id);
}
