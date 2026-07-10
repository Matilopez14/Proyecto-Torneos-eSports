package cl.duoc.mstransmisiones.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "transmisiones")
@Data
public class Transmision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_partida", nullable = false)
    private Long idPartida;

    @Column(nullable = false)
    private String plataforma; // YouTube, Twitch, etc.

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String estado; // EN_VIVO, FINALIZADA, PROGRAMADA

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;
}