package com.example.ms_estadisticas.service;

import com.example.ms_estadisticas.client.JugadorClient;
import com.example.ms_estadisticas.client.PartidaClient;
import com.example.ms_estadisticas.dto.EstadisticaRequestDTO;
import com.example.ms_estadisticas.entity.Estadistica;
import com.example.ms_estadisticas.exception.BusinessRuleException;
import com.example.ms_estadisticas.repository.EstadisticaRepository;
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
public class EstadisticaService {

    private final EstadisticaRepository estadisticaRepository;
    private final JugadorClient jugadorClient;
    private final PartidaClient partidaClient;

    public Estadistica registrarEstadistica(EstadisticaRequestDTO dto) {
        log.info("Registrando estadísticas del jugador {} en la partida {}", dto.getIdJugador(), dto.getIdPartida());

        if (estadisticaRepository.existsByIdPartidaAndIdJugador(dto.getIdPartida(), dto.getIdJugador())) {
            throw new BusinessRuleException("El jugador ya tiene estadísticas registradas en esta partida.");
        }

        validarPartida(dto.getIdPartida());
        validarJugador(dto.getIdJugador());

        double kda = calcularKda(dto.getKills(), dto.getDeaths(), dto.getAssists());

        Estadistica estadistica = new Estadistica();
        estadistica.setIdPartida(dto.getIdPartida());
        estadistica.setIdJugador(dto.getIdJugador());
        estadistica.setKills(dto.getKills());
        estadistica.setDeaths(dto.getDeaths());
        estadistica.setAssists(dto.getAssists());
        estadistica.setKda(kda);

        Estadistica guardada = estadisticaRepository.save(estadistica);
        log.info("Estadística registrada correctamente con ID: {}", guardada.getId());

        return guardada;
    }

    public List<Estadistica> obtenerTodas() {
        log.info("Listando todas las estadísticas");
        return estadisticaRepository.findAll();
    }

    public Estadistica obtenerPorId(Long id) {
        log.info("Buscando estadística con ID: {}", id);

        return estadisticaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existe una estadística con ID: " + id
                ));
    }

    public List<Estadistica> obtenerPorPartida(Long idPartida) {
        log.info("Listando estadísticas de la partida {}", idPartida);
        return estadisticaRepository.findByIdPartida(idPartida);
    }

    public List<Estadistica> obtenerPorJugador(Long idJugador) {
        log.info("Listando estadísticas del jugador {}", idJugador);
        return estadisticaRepository.findByIdJugador(idJugador);
    }

    public void eliminarEstadistica(Long id) {
        log.info("Eliminando estadística con ID: {}", id);

        Estadistica estadistica = obtenerPorId(id);
        estadisticaRepository.delete(estadistica);

        log.info("Estadística eliminada correctamente con ID: {}", id);
    }

    private void validarPartida(Long idPartida) {
        try {
            log.info("Consultando MS-Partidas por ID: {}", idPartida);
            partidaClient.obtenerPartidaPorId(idPartida);
        } catch (FeignException.NotFound ex) {
            throw new BusinessRuleException("La partida especificada no existe.");
        } catch (FeignException ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible comunicarse con MS-Partidas."
            );
        }
    }

    private void validarJugador(Long idJugador) {
        try {
            log.info("Consultando MS-Jugadores por ID: {}", idJugador);
            jugadorClient.obtenerJugadorPorId(idJugador);
        } catch (FeignException.NotFound ex) {
            throw new BusinessRuleException("El jugador especificado no existe.");
        } catch (FeignException ex) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "No fue posible comunicarse con MS-Jugadores."
            );
        }
    }

    private double calcularKda(Integer kills, Integer deaths, Integer assists) {
        double kda;

        if (deaths == 0) {
            kda = kills + assists;
        } else {
            kda = (double) (kills + assists) / deaths;
        }

        return Math.round(kda * 100.0) / 100.0;
    }
}