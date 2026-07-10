package cl.duoc.mspartidas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "partidas")
@Data
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_torneo", nullable = false)
    private Long idTorneo;

    @Column(name = "id_equipo_a", nullable = false)
    private Long idEquipoA;

    @Column(name = "id_equipo_b", nullable = false)
    private Long idEquipoB;

    @Column(nullable = false, length = 50)
    private String mapa;

    @Column(name = "resultado_a")
    private Integer resultadoA;

    @Column(name = "resultado_b")
    private Integer resultadoB;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
    }
}