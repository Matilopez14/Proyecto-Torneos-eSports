package cl.duoc.mspartidas.repository;

import cl.duoc.mspartidas.entity.Partida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartidaRepository extends JpaRepository<Partida, Long> {

    List<Partida> findByIdTorneo(Long idTorneo);

    List<Partida> findByIdEquipoAOrIdEquipoB(Long idEquipoA, Long idEquipoB);
}