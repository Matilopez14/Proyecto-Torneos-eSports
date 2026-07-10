package com.example.ms_estadisticas.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PartidaDTO {
    private Long id;
    private Long idTorneo;
    private Long idEquipoA;
    private Long idEquipoB;
    private String mapa;
    private Integer resultadoA;
    private Integer resultadoB;
    private String estado;
    private LocalDateTime fechaCreacion;
}
