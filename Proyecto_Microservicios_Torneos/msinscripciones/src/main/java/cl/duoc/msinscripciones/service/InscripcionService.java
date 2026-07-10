package cl.duoc.msinscripciones.service;

import cl.duoc.msinscripciones.client.EquipoClient;
import cl.duoc.msinscripciones.client.TorneoClient;
import cl.duoc.msinscripciones.dto.InscripcionRequestDTO;
import cl.duoc.msinscripciones.entity.Inscripcion;
import cl.duoc.msinscripciones.exception.BusinessRuleException;
import cl.duoc.msinscripciones.repository.InscripcionRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final TorneoClient torneoClient;
    private final EquipoClient equipoClient;

    public Inscripcion registrarInscripcion(InscripcionRequestDTO dto) {
        log.info("Procesando inscripción del equipo {} en el torneo {}", dto.getIdEquipo(), dto.getIdTorneo());

        if (inscripcionRepository.existsByIdTorneoAndIdEquipo(dto.getIdTorneo(), dto.getIdEquipo())) {
            log.warn("El equipo {} ya está inscrito en el torneo {}", dto.getIdEquipo(), dto.getIdTorneo());
            throw new BusinessRuleException("El equipo ya se encuentra inscrito en el torneo seleccionado.");
        }

        validarTorneo(dto.getIdTorneo());
        validarEquipo(dto.getIdEquipo());

        Inscripcion nuevaInscripcion = new Inscripcion();
        nuevaInscripcion.setIdTorneo(dto.getIdTorneo());
        nuevaInscripcion.setIdEquipo(dto.getIdEquipo());

        Inscripcion guardada = inscripcionRepository.save(nuevaInscripcion);
        log.info("Inscripción exitosa. ID de registro: {}", guardada.getId());

        return guardada;
    }

    public List<Inscripcion> obtenerTodas() {
        log.info("Listando todas las inscripciones");
        return inscripcionRepository.findAll();
    }

    public Inscripcion obtenerPorId(Long id) {
        log.info("Buscando inscripción con ID: {}", id);

        return inscripcionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe una inscripción con ID: " + id
                ));
    }

    public List<Inscripcion> obtenerPorTorneo(Long idTorneo) {
        log.info("Listando inscripciones del torneo {}", idTorneo);
        return inscripcionRepository.findByIdTorneo(idTorneo);
    }

    public List<Inscripcion> obtenerPorEquipo(Long idEquipo) {
        log.info("Listando inscripciones del equipo {}", idEquipo);
        return inscripcionRepository.findByIdEquipo(idEquipo);
    }

    public void eliminarInscripcion(Long id) {
        log.info("Eliminando inscripción con ID: {}", id);

        Inscripcion inscripcion = obtenerPorId(id);
        inscripcionRepository.delete(inscripcion);

        log.info("Inscripción eliminada correctamente con ID: {}", id);
    }

    private void validarTorneo(Long idTorneo) {
        try {
            log.info("Consultando MS-Torneos por ID: {}", idTorneo);
            torneoClient.obtenerTorneoPorId(idTorneo);
        } catch (FeignException.NotFound ex) {
            log.warn("Torneo no encontrado con ID: {}", idTorneo);
            throw new BusinessRuleException("El torneo especificado no existe.");
        } catch (FeignException ex) {
            log.error("Error al consultar MS-Torneos: {}", ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible comunicarse con MS-Torneos."
            );
        }
    }

    private void validarEquipo(Long idEquipo) {
        try {
            log.info("Consultando MS-Equipos por ID: {}", idEquipo);
            equipoClient.obtenerEquipoPorId(idEquipo);
        } catch (FeignException.NotFound ex) {
            log.warn("Equipo no encontrado con ID: {}", idEquipo);
            throw new BusinessRuleException("El equipo especificado no existe.");
        } catch (FeignException ex) {
            log.error("Error al consultar MS-Equipos: {}", ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible comunicarse con MS-Equipos."
            );
        }
    }
}