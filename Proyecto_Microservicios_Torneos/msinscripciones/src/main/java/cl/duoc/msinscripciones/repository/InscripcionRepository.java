package cl.duoc.msinscripciones.repository;

import cl.duoc.msinscripciones.entity.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    boolean existsByIdTorneoAndIdEquipo(Long idTorneo, Long idEquipo);

    List<Inscripcion> findByIdTorneo(Long idTorneo);

    List<Inscripcion> findByIdEquipo(Long idEquipo);
}