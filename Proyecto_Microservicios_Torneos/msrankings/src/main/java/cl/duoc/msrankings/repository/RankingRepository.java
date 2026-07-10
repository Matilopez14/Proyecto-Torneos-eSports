package cl.duoc.msrankings.repository;

import cl.duoc.msrankings.entity.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
    Optional<Ranking> findByIdJugador(Long idJugador);
}
