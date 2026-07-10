package cl.duoc.msrankings.dto;

import lombok.Data;

@Data
public class JugadorDTO {
    private Long id;
    private String riotId;
    private String email;
    private String rangoActual;
    private Integer nivel;
}
