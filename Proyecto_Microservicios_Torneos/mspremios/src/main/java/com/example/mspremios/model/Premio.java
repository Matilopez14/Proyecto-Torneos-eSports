package com.example.mspremios.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "premios")
@Data
public class Premio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long torneoId;

    @Column(nullable = false)
    private Double montoTotal;

    @Column(nullable = false)
    private String posicionDestino;

    @Column(nullable = false)
    private Double porcentajeDistribucion;
}