package cl.duoc.msjugadores.repository;

import cl.duoc.msjugadores.entity.Jugador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JugadorRepository extends JpaRepository<Jugador, Long> {

    // Spring Data JPA hace la magia aquí. Esto nos servirá para la lógica de negocio después.
    Optional<Jugador> findByRiotId(String riotId);
    Optional<Jugador> findByEmail(String email);
    boolean existsByRiotId(String riotId);
}
