package cl.duoc.msequipos.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "equipos")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String tagAcronimo;

    @Column(nullable = false)
    private String region;

    @Column(nullable = false)
    private Long idCapitan;

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTagAcronimo() {
        return tagAcronimo;
    }

    public String getRegion() {
        return region;
    }

    public Long getIdCapitan() {
        return idCapitan;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTagAcronimo(String tagAcronimo) {
        this.tagAcronimo = tagAcronimo;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setIdCapitan(Long idCapitan) {
        this.idCapitan = idCapitan;
    }
}