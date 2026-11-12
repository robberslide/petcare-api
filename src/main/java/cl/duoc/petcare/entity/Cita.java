package cl.duoc.petcare.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

/**
 * CAPA ENTITY - tabla "citas".
 * Cada cita pertenece a una mascota.
 */
@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // LocalDateTime = fecha + hora. En JSON: "2026-11-20T10:30:00".
    // @NotNull (no @NotBlank) porque no es texto.
    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fecha;

    @NotBlank(message = "El motivo es obligatorio")
    private String motivo;

    // Opcional: puede quedar vacío si la cita aún no se realiza.
    private String diagnostico;

    // @PositiveOrZero: no se permiten costos negativos.
    // Integer porque en pesos chilenos no hay decimales.
    @PositiveOrZero(message = "El costo no puede ser negativo")
    private Integer costo;

    /*
     * RELACIÓN N a 1: muchas citas pertenecen a UNA mascota.
     * @JoinColumn(name = "mascota_id"): llave foránea hacia mascotas.id.
     * En el POST se manda solo el id: "mascota": { "id": 1 }
     * y el Service se encarga de buscar la mascota completa.
     */
    @ManyToOne
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    public Cita() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public Integer getCosto() { return costo; }
    public void setCosto(Integer costo) { this.costo = costo; }
    public Mascota getMascota() { return mascota; }
    public void setMascota(Mascota mascota) { this.mascota = mascota; }
}
