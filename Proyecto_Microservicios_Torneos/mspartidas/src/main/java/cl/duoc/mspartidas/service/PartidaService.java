package cl.duoc.mspartidas.service;

import cl.duoc.mspartidas.client.EquipoClient;
import cl.duoc.mspartidas.client.TorneoClient;
import cl.duoc.mspartidas.dto.PartidaRequestDTO;
import cl.duoc.mspartidas.dto.ResultadoPartidaDTO;
import cl.duoc.mspartidas.entity.Partida;
import cl.duoc.mspartidas.exception.BusinessRuleException;
import cl.duoc.mspartidas.repository.PartidaRepository;
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
public class PartidaService {

    private final PartidaRepository partidaRepository;
    private final TorneoClient torneoClient;
    private final EquipoClient equipoClient;

    public Partida crearPartida(PartidaRequestDTO dto) {
        log.info(
                "Programando partida entre Equipo {} y Equipo {} en torneo {}",
                dto.getIdEquipoA(),
                dto.getIdEquipoB(),
                dto.getIdTorneo()
        );

        if (dto.getIdEquipoA().equals(dto.getIdEquipoB())) {
            log.warn("El Equipo A y el Equipo B son el mismo");
            throw new BusinessRuleException("Un equipo no puede jugar contra sí mismo.");
        }

        validarTorneo(dto.getIdTorneo());
        validarEquipo(dto.getIdEquipoA(), "A");
        validarEquipo(dto.getIdEquipoB(), "B");

        Partida partida = new Partida();
        partida.setIdTorneo(dto.getIdTorneo());
        partida.setIdEquipoA(dto.getIdEquipoA());
        partida.setIdEquipoB(dto.getIdEquipoB());
        partida.setMapa(dto.getMapa().trim());
        partida.setEstado("PROGRAMADA");
        partida.setResultadoA(0);
        partida.setResultadoB(0);

        Partida guardada = partidaRepository.save(partida);
        log.info("Partida programada correctamente con ID: {}", guardada.getId());

        return guardada;
    }

    public List<Partida> obtenerPartidas() {
        log.info("Listando todas las partidas");
        return partidaRepository.findAll();
    }

    public Partida obtenerPorId(Long id) {
        log.info("Buscando partida con ID: {}", id);

        return partidaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe una partida con ID: " + id
                ));
    }

    public List<Partida> obtenerPorTorneo(Long idTorneo) {
        log.info("Listando partidas del torneo {}", idTorneo);
        return partidaRepository.findByIdTorneo(idTorneo);
    }

    public List<Partida> obtenerPorEquipo(Long idEquipo) {
        log.info("Listando partidas del equipo {}", idEquipo);
        return partidaRepository.findByIdEquipoAOrIdEquipoB(idEquipo, idEquipo);
    }

    public Partida iniciarPartida(Long id) {
        log.info("Iniciando partida con ID: {}", id);

        Partida partida = obtenerPorId(id);

        if (!partida.getEstado().equals("PROGRAMADA")) {
            throw new BusinessRuleException("Solo se pueden iniciar partidas en estado PROGRAMADA.");
        }

        partida.setEstado("EN_CURSO");

        Partida actualizada = partidaRepository.save(partida);
        log.info("Partida iniciada correctamente con ID: {}", actualizada.getId());

        return actualizada;
    }

    public Partida finalizarPartida(Long id, ResultadoPartidaDTO dto) {
        log.info("Finalizando partida con ID: {}", id);

        Partida partida = obtenerPorId(id);

        if (partida.getEstado().equals("FINALIZADA")) {
            throw new BusinessRuleException("La partida ya se encuentra finalizada.");
        }

        partida.setResultadoA(dto.getResultadoA());
        partida.setResultadoB(dto.getResultadoB());
        partida.setEstado("FINALIZADA");

        Partida finalizada = partidaRepository.save(partida);
        log.info("Partida finalizada correctamente con ID: {}", finalizada.getId());

        return finalizada;
    }

    public void eliminarPartida(Long id) {
        log.info("Eliminando partida con ID: {}", id);

        Partida partida = obtenerPorId(id);
        partidaRepository.delete(partida);

        log.info("Partida eliminada correctamente con ID: {}", id);
    }

    private void validarTorneo(Long idTorneo) {
        try {
            log.info("Consultando MS-Torneos por ID: {}", idTorneo);
            torneoClient.obtenerTorneoPorId(idTorneo);
        } catch (FeignException.NotFound ex) {
            throw new BusinessRuleException("El torneo especificado no existe.");
        } catch (FeignException ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible comunicarse con MS-Torneos."
            );
        }
    }

    private void validarEquipo(Long idEquipo, String etiqueta) {
        try {
            log.info("Consultando MS-Equipos por ID: {}", idEquipo);
            equipoClient.obtenerEquipoPorId(idEquipo);
        } catch (FeignException.NotFound ex) {
            throw new BusinessRuleException("El Equipo " + etiqueta + " especificado no existe.");
        } catch (FeignException ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible comunicarse con MS-Equipos."
            );
        }
    }
}