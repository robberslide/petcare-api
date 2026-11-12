package cl.duoc.petcare.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * CAPA ENTITY - tabla "mascotas".
 * Es la entidad del medio: pertenece a un Dueño y tiene muchas Citas.
 */
@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "La especie es obligatoria")
    private String especie;

    private String raza;

    // LocalDate = solo fecha (sin hora). En JSON se manda como "2021-05-10".
    private LocalDate fechaNacimiento;

    /*
     * RELACIÓN N a 1: muchas mascotas pertenecen a UN dueño.
     *
     * @ManyToOne: este es el lado "dueño" de la relación (el que tiene la FK).
     * @JoinColumn(name = "dueno_id"): crea la columna dueno_id en la tabla
     *   mascotas, que es la llave foránea hacia duenos.id.
     * nullable = false: una mascota NO puede existir sin dueño.
     *
     * Este atributo SÍ aparece en el JSON, así al consultar una mascota
     * se ve quién es su dueño.
     */
    @ManyToOne
    @JoinColumn(name = "dueno_id", nullable = false)
    private Dueno dueno;

    /*
     * RELACIÓN 1 a N: una mascota tiene MUCHAS citas.
     * mappedBy = "mascota": la FK (mascota_id) está en la tabla citas.
     * cascade = ALL: si se borra la mascota, se borran sus citas.
     * @JsonIgnore: evita el bucle infinito en el JSON. Las citas se consultan
     * con GET /api/mascotas/{id}/citas.
     */
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Cita> citas = new ArrayList<>();

    public Mascota() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public Dueno getDueno() { return dueno; }
    public void setDueno(Dueno dueno) { this.dueno = dueno; }
    public List<Cita> getCitas() { return citas; }
    public void setCitas(List<Cita> citas) { this.citas = citas; }
}
