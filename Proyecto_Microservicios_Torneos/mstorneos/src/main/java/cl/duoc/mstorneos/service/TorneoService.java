package cl.duoc.mstorneos.service;

import cl.duoc.mstorneos.dto.TorneoRequestDTO;
import cl.duoc.mstorneos.entity.Torneo;
import cl.duoc.mstorneos.exception.BusinessRuleException;
import cl.duoc.mstorneos.repository.TorneoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TorneoService {

    private final TorneoRepository torneoRepository;

    private static final Map<String, String> TRANSICIONES_VALIDAS = Map.of(
            "ABIERTO", "EN_CURSO",
            "EN_CURSO", "FINALIZADO"
    );

    public Torneo crearTorneo(TorneoRequestDTO dto) {
        log.info("Iniciando creación del torneo: {}", dto.getNombre());

        // Regla de Negocio 1: El nombre no se puede repetir
        if (torneoRepository.existsByNombre(dto.getNombre())) {
            log.error("El torneo {} ya existe en la base de datos", dto.getNombre());
            throw new BusinessRuleException("Ya existe un torneo con ese nombre.");
        }

        // Regla de Negocio 2: Coherencia de fechas
        if (dto.getFechaFin().isBefore(dto.getFechaInicio())) {
            log.error("Fechas inválidas: La fecha de fin es anterior a la fecha de inicio");
            throw new BusinessRuleException("La fecha de fin no puede ser anterior a la fecha de inicio del torneo.");
        }

        Torneo nuevoTorneo = new Torneo();
        nuevoTorneo.setNombre(dto.getNombre());
        nuevoTorneo.setFechaInicio(dto.getFechaInicio());
        nuevoTorneo.setFechaFin(dto.getFechaFin());
        nuevoTorneo.setPrizePool(dto.getPrizePool());
        nuevoTorneo.setEstado("ABIERTO"); // Estado por defecto al crear

        Torneo guardado = torneoRepository.save(nuevoTorneo);
        log.info("Torneo creado exitosamente con ID: {}", guardado.getId());

        return guardado;
    }

    public Torneo cambiarEstado(Long id, String nuevoEstado) {
        log.info("Intentando cambiar el estado del torneo ID: {} a {}", id, nuevoEstado);

        Torneo torneo = torneoRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Torneo no encontrado con ID: " + id));

        String estadoActual = torneo.getEstado();
        String estadoPermitido = TRANSICIONES_VALIDAS.get(estadoActual);

        if (estadoPermitido == null || !estadoPermitido.equals(nuevoEstado)) {
            log.error("Transición de estado inválida: {} -> {}", estadoActual, nuevoEstado);
            throw new BusinessRuleException(
                    "No se puede cambiar el estado de '" + estadoActual + "' a '" + nuevoEstado +
                    "'. El flujo válido es: ABIERTO -> EN_CURSO -> FINALIZADO.");
        }

        torneo.setEstado(nuevoEstado);
        Torneo actualizado = torneoRepository.save(torneo);
        log.info("Estado del torneo ID: {} cambiado exitosamente a {}", id, nuevoEstado);

        return actualizado;
    }

    public List<Torneo> obtenerTorneos() {
        return torneoRepository.findAll();
    }

    public Torneo obtenerTorneoPorId(Long id) {
        return torneoRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Torneo no encontrado con ID: " + id));
    }
}
