package com.example.ms_estadisticas.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "estadisticas")
@Data
public class Estadistica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_partida", nullable = false)
    private Long idPartida;

    @Column(name = "id_jugador", nullable = false)
    private Long idJugador;

    @Column(nullable = false)
    private Integer kills;

    @Column(nullable = false)
    private Integer deaths;

    @Column(nullable = false)
    private Integer assists;

    @Column(nullable = false)
    private Double kda; // (Kills + Assists) / Deaths
}
