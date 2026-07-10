package com.example.mspremios.repository;

import com.example.mspremios.model.Premio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PremioRepository extends JpaRepository<Premio, Long> {
    // Esto te permitirá buscar premios por torneo más adelante
    List<Premio> findByTorneoId(Long torneoId);
}
