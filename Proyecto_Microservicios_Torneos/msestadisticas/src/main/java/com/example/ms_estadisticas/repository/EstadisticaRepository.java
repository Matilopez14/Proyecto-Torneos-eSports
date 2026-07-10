package com.example.ms_estadisticas.repository;

import com.example.ms_estadisticas.entity.Estadistica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstadisticaRepository extends JpaRepository<Estadistica, Long> {

    boolean existsByIdPartidaAndIdJugador(Long idPartida, Long idJugador);

    List<Estadistica> findByIdPartida(Long idPartida);

    List<Estadistica> findByIdJugador(Long idJugador);
}