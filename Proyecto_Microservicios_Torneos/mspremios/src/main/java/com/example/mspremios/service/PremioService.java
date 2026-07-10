package com.example.mspremios.service;

import com.example.mspremios.client.TorneoClient;
import com.example.mspremios.dto.PremioDTO;
import com.example.mspremios.exception.BusinessRuleException;
import com.example.mspremios.model.Premio;
import com.example.mspremios.repository.PremioRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j // Esta anotación de Lombok nos da la variable "log" automáticamente para la pauta
@RequiredArgsConstructor
public class PremioService {

    private final PremioRepository premioRepository;
    private final TorneoClient torneoClient;

    public Premio guardarPremio(PremioDTO dto) {
        log.info("Iniciando proceso para registrar un premio. Torneo ID destino: {}", dto.getTorneoId());

        // 1. Validar remotamente usando Feign si el torneo existe en 'mstorneos'
        try {
            log.info("Llamando de forma remota a mstorneos para verificar el ID: {}", dto.getTorneoId());
            torneoClient.obtenerTorneoPorId(dto.getTorneoId());
            log.info("Verificación remota exitosa. El torneo existe.");
        } catch (FeignException.NotFound e) {
            log.error("El torneo con ID {} no fue encontrado en mstorneos (404).", dto.getTorneoId());
            throw new BusinessRuleException("No se puede crear el premio porque el Torneo especificado no existe.");
        } catch (FeignException e) {
            log.error("Error de comunicación con mstorneos (HTTP {}): {}", e.status(), e.getMessage());
            throw new BusinessRuleException("Error al comunicarse con el servicio de torneos. Intente más tarde.");
        }

        // 2. Mapear el DTO a la Entidad real
        Premio premio = new Premio();
        premio.setTorneoId(dto.getTorneoId());
        premio.setMontoTotal(dto.getMontoTotal());
        premio.setPosicionDestino(dto.getPosicionDestino());
        premio.setPorcentajeDistribucion(dto.getPorcentajeDistribucion());

        // 3. Guardar en la base de datos
        Premio guardado = premioRepository.save(premio);
        log.info("Premio creado con éxito en la base de datos. Asignado ID autoincremental: {}", guardado.getId());

        return guardado;
    }

    public List<Premio> obtenerPremiosPorTorneo(Long torneoId) {
        log.info("Consultando la base de datos para obtener los premios del torneo ID: {}", torneoId);
        return premioRepository.findByTorneoId(torneoId);
    }

    public List<Premio> obtenerTodos() {
        return premioRepository.findAll();
    }

    public Premio obtenerPorId(Long id) {
        return premioRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Premio no encontrado con id: " + id));
    }
}
