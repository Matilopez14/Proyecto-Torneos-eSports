package cl.duoc.mstransmisiones.repository;

import cl.duoc.mstransmisiones.entity.Transmision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransmisionRepository extends JpaRepository<Transmision, Long> {
    List<Transmision> findByIdPartida(Long idPartida);
}