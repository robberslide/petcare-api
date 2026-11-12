package cl.duoc.petcare.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

/**
 * CAPA ENTITY
 * Una entidad es una clase Java que representa una TABLA de la base de datos.
 * Cada atributo = una columna, cada objeto = una fila.
 *
 * @Entity -> le dice a JPA/Hibernate que esta clase se guarda en la BD.
 * @Table  -> nombre de la tabla (si no se pone, usa el nombre de la clase).
 */
@Entity
@Table(name = "duenos")
public class Dueno {

    // @Id: clave primaria.
    // @GeneratedValue(IDENTITY): la BD genera el id solo (autoincremental),
    // por eso en el POST no se manda el id.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @NotBlank: validación (Bean Validation). Si viene vacío, el Controller
    // rechaza la petición con 400 antes de llegar al Service.
    // unique = true: la BD no permite dos dueños con el mismo rut.
    @NotBlank(message = "El rut es obligatorio")
    @Column(nullable = false, unique = true)
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String telefono;

    // @Email: valida que tenga formato de correo.
    @Email(message = "Email no válido")
    private String email;

    /*
     * RELACIÓN 1 a N: un dueño tiene MUCHAS mascotas.
     *
     * @OneToMany(mappedBy = "dueno"): la relación la "maneja" el atributo
     *   'dueno' de la clase Mascota. Es decir, la llave foránea (dueno_id)
     *   está en la tabla mascotas, no aquí.
     * cascade = ALL: si se borra el dueño, se borran también sus mascotas.
     * orphanRemoval = true: si una mascota se saca de la lista, se elimina.
     *
     * @JsonIgnore: al convertir a JSON NO se incluye esta lista. Si no lo
     * pusiéramos, se generaría un bucle infinito:
     *   dueño -> mascotas -> dueño -> mascotas -> ...
     * Para ver las mascotas de un dueño usamos GET /api/duenos/{id}/mascotas.
     */
    @OneToMany(mappedBy = "dueno", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Mascota> mascotas = new ArrayList<>();

    // JPA necesita un constructor vacío para crear los objetos al leer la BD.
    public Dueno() {}

    // Getters y setters: JPA y Jackson (JSON) los usan para leer/escribir los atributos.
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<Mascota> getMascotas() { return mascotas; }
    public void setMascotas(List<Mascota> mascotas) { this.mascotas = mascotas; }
}
