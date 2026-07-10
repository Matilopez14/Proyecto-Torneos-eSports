package cl.duoc.mstorneos.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "torneos")
@Data
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "prize_pool")
    private Double prizePool; // El premio en dinero

    @Column(nullable = false, length = 20)
    private String estado; // Ej: "ABIERTO", "EN_CURSO", "FINALIZADO"
}
