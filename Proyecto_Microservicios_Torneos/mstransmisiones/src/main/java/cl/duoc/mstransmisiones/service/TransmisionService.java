package cl.duoc.mstransmisiones.service;

import cl.duoc.mstransmisiones.client.PartidaClient;
import cl.duoc.mstransmisiones.dto.TransmisionRequestDTO;
import cl.duoc.mstransmisiones.entity.Transmision;
import cl.duoc.mstransmisiones.exception.BusinessRuleException;
import cl.duoc.mstransmisiones.repository.TransmisionRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransmisionService {

    private final TransmisionRepository transmisionRepository;
    private final PartidaClient partidaClient;

    public Transmision crearTransmision(TransmisionRequestDTO dto) {
        validarPartida(dto.getIdPartida());

        Transmision transmision = new Transmision();
        transmision.setIdPartida(dto.getIdPartida());
        transmision.setPlataforma(dto.getPlataforma());
        transmision.setUrl(dto.getUrl());
        transmision.setEstado(dto.getEstado());
        transmision.setFechaInicio(dto.getFechaInicio());

        return transmisionRepository.save(transmision);
    }

    public List<Transmision> obtenerTodas() {
        return transmisionRepository.findAll();
    }

    public Transmision obtenerPorId(Long id) {
        return transmisionRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Transmisión no encontrada con ID: " + id));
    }

    public List<Transmision> obtenerPorPartida(Long idPartida) {
        return transmisionRepository.findByIdPartida(idPartida);
    }

    public void eliminarTransmision(Long id) {
        if (!transmisionRepository.existsById(id)) {
            throw new BusinessRuleException("No se puede eliminar. Transmisión no encontrada con ID: " + id);
        }
        transmisionRepository.deleteById(id);
    }

    private void validarPartida(Long idPartida) {
        try {
            partidaClient.obtenerPartidaPorId(idPartida);
        } catch (FeignException.NotFound e) {
            throw new BusinessRuleException("La partida con ID " + idPartida + " no existe.");
        } catch (Exception e) {
            throw new BusinessRuleException("Error al validar la partida: " + e.getMessage());
        }
    }
}