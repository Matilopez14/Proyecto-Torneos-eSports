package cl.duoc.msjugadores.dto;

import jakarta.validation.constraints.*;

public class JugadorRequestDTO {

    @NotBlank(message = "El Riot ID no puede estar vacío")
    @Size(min = 3, max = 50, message = "El Riot ID debe tener entre 3 y 50 caracteres")
    private String riotId;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ingresar un formato de email válido")
    private String email;

    @NotBlank(message = "El rango es obligatorio")
    private String rangoActual;

    @NotNull(message = "El nivel no puede ser nulo")
    @Min(value = 1, message = "El nivel mínimo es 1")
    private Integer nivel;

    public String getRiotId() {
        return riotId;
    }

    public String getEmail() {
        return email;
    }

    public String getRangoActual() {
        return rangoActual;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setRiotId(String riotId) {
        this.riotId = riotId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRangoActual(String rangoActual) {
        this.rangoActual = rangoActual;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }
}
