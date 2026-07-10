package com.example.mspremios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "mstorneos", path = "/api/v1/torneos")
public interface TorneoClient {

    @GetMapping("/{id}")
    Object obtenerTorneoPorId(@PathVariable("id") Long id);
}
