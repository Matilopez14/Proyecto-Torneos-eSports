package cl.duoc.msjugadores.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "jugadores")
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String riotId;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String rangoActual;

    @Column(nullable = false)
    private Integer nivel;

    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getRiotId() {
        return riotId;
    }

    public String getEmail() {
        return email;
    }

    public String getRangoActual() {
        return rangoActual;
    }

    public Integer getNivel() {
        return nivel;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRiotId(String riotId) {
        this.riotId = riotId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRangoActual(String rangoActual) {
        this.rangoActual = rangoActual;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}