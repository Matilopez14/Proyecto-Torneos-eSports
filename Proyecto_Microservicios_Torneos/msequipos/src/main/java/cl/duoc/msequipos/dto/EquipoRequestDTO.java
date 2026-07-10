package cl.duoc.msequipos.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para registrar un equipo")
public class EquipoRequestDTO {

    @NotBlank(message = "El nombre del equipo es obligatorio")
    @Schema(description = "Nombre del equipo", example = "Team Liquid")
    private String nombre;

    @NotBlank(message = "El tag o acrónimo del equipo es obligatorio")
    @Size(min = 2, max = 5, message = "El tag debe tener entre 2 y 5 caracteres")
    @Schema(description = "Tag o acrónimo del equipo", example = "TL")
    private String tagAcronimo;

    @NotBlank(message = "La región del equipo es obligatoria")
    @Schema(description = "Región del equipo", example = "NA")
    private String region;

    @NotNull(message = "Debe asignar el ID de un jugador para que sea el capitán")
    @Schema(description = "ID del jugador capitán", example = "1")
    private Long idCapitan;

    public String getNombre() { return nombre; }
    public String getTagAcronimo() { return tagAcronimo; }
    public String getRegion() { return region; }
    public Long getIdCapitan() { return idCapitan; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTagAcronimo(String tagAcronimo) { this.tagAcronimo = tagAcronimo; }
    public void setRegion(String region) { this.region = region; }
    public void setIdCapitan(Long idCapitan) { this.idCapitan = idCapitan; }
}
