package com.example.ms_estadisticas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msjugadores", path = "/api/v1/jugadores")
public interface JugadorClient {
    @GetMapping("/{id}")
    Object obtenerJugadorPorId(@PathVariable("id") Long id);
}
