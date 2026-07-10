package com.example.msnotificaciones.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String destinatario; // Correo o Nickname del usuario/equipo

    @Column(nullable = false)
    private String mensaje; // Contenido del aviso (Ej: "Tu inscripción al torneo fue aceptada")

    @Column(nullable = false)
    private LocalDateTime fechaEnvio;
}
