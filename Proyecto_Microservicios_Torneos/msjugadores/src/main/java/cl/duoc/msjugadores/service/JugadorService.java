package cl.duoc.msjugadores.service;

import cl.duoc.msjugadores.dto.JugadorRequestDTO;
import cl.duoc.msjugadores.entity.Jugador;
import cl.duoc.msjugadores.exception.BusinessRuleException;
import cl.duoc.msjugadores.repository.JugadorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JugadorService {

    private static final Logger log = LoggerFactory.getLogger(JugadorService.class);

    private final JugadorRepository jugadorRepository;

    public JugadorService(JugadorRepository jugadorRepository) {
        this.jugadorRepository = jugadorRepository;
    }

    public Jugador crearJugador(JugadorRequestDTO dto) {
        log.info("Iniciando creación de jugador con Riot ID: {}", dto.getRiotId());

        if (jugadorRepository.existsByRiotId(dto.getRiotId())) {
            log.error("Fallo al crear: El Riot ID {} ya está registrado", dto.getRiotId());
            throw new BusinessRuleException("El Riot ID " + dto.getRiotId() + " ya se encuentra en uso.");
        }

        Jugador nuevoJugador = new Jugador();
        nuevoJugador.setRiotId(dto.getRiotId());
        nuevoJugador.setEmail(dto.getEmail());
        nuevoJugador.setRangoActual(dto.getRangoActual());
        nuevoJugador.setNivel(dto.getNivel());

        Jugador jugadorGuardado = jugadorRepository.save(nuevoJugador);
        log.info("Jugador guardado exitosamente en BD con ID: {}", jugadorGuardado.getId());

        return jugadorGuardado;
    }

    public List<Jugador> obtenerTodosLosJugadores() {
        log.info("Consultando lista completa de jugadores");
        return jugadorRepository.findAll();
    }

    public Jugador obtenerPorId(Long id) {
        log.info("Buscando jugador con ID: {}", id);

        return jugadorRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Jugador no encontrado con ID: " + id));
    }
}
