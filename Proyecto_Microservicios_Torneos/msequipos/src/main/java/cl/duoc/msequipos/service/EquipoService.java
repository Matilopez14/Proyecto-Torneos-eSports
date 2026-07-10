package cl.duoc.msequipos.service;

import cl.duoc.msequipos.client.JugadorClient;
import cl.duoc.msequipos.dto.EquipoRequestDTO;
import cl.duoc.msequipos.entity.Equipo;
import cl.duoc.msequipos.exception.BusinessRuleException;
import cl.duoc.msequipos.repository.EquipoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipoService {

    private static final Logger log = LoggerFactory.getLogger(EquipoService.class);

    private final EquipoRepository equipoRepository;
    private final JugadorClient jugadorClient;

    public EquipoService(EquipoRepository equipoRepository, JugadorClient jugadorClient) {
        this.equipoRepository = equipoRepository;
        this.jugadorClient = jugadorClient;
    }

    public Equipo crearEquipo(EquipoRequestDTO dto) {
        log.info("Iniciando creación de equipo con nombre: {}", dto.getNombre());

        try {
            jugadorClient.obtenerJugadorPorId(dto.getIdCapitan());
        } catch (Exception ex) {
            log.warn("No se encontró el capitán con ID: {}", dto.getIdCapitan());
            throw new BusinessRuleException("No se puede crear el equipo porque el capitán no existe");
        }

        Equipo equipo = new Equipo();
        equipo.setNombre(dto.getNombre());
        equipo.setTagAcronimo(dto.getTagAcronimo().toUpperCase());
        equipo.setRegion(dto.getRegion());
        equipo.setIdCapitan(dto.getIdCapitan());

        Equipo equipoGuardado = equipoRepository.save(equipo);
        log.info("Equipo creado correctamente con ID: {}", equipoGuardado.getId());

        return equipoGuardado;
    }

    public List<Equipo> listarTodos() {
        log.info("Listando todos los equipos");
        return equipoRepository.findAll();
    }

    public Equipo obtenerPorId(Long id) {
        log.info("Buscando equipo con ID: {}", id);

        return equipoRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Equipo no encontrado con ID: " + id));
    }
}