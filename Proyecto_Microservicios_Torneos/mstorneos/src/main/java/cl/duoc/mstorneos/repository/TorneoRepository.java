package cl.duoc.mstorneos.repository;

import cl.duoc.mstorneos.entity.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TorneoRepository extends JpaRepository<Torneo, Long> {
    boolean existsByNombre(String nombre);
}
