package com.example.msnotificaciones.service;

import com.example.msnotificaciones.dto.NotificacionDTO;
import com.example.msnotificaciones.exception.BusinessRuleException;
import com.example.msnotificaciones.model.Notificacion;
import com.example.msnotificaciones.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;

    public Notificacion registrarYEnviar(NotificacionDTO dto) {
        log.info("Recibiendo solicitud de notificación para: {}", dto.getDestinatario());

        // Simulación lógica de envío de correo/alerta
        log.info("Enviando alerta en tiempo real... Mensaje: '{}'", dto.getMensaje());

        Notificacion notificacion = new Notificacion();
        notificacion.setDestinatario(dto.getDestinatario());
        notificacion.setMensaje(dto.getMensaje());
        notificacion.setFechaEnvio(LocalDateTime.now());

        Notificacion guardada = notificacionRepository.save(notificacion);
        log.info("Historial de notificación respaldado en BD con ID: {}", guardada.getId());
        return guardada;
    }

    public List<Notificacion> obtenerTodas() {
        return notificacionRepository.findAll();
    }

    public Notificacion obtenerPorId(Long id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Notificación no encontrada con id: " + id));
    }
}
