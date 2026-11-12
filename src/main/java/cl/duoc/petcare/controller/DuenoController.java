package cl.duoc.petcare.controller;

import cl.duoc.petcare.entity.Dueno;
import cl.duoc.petcare.entity.Mascota;
import cl.duoc.petcare.service.DuenoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CAPA CONTROLLER
 * Es la "puerta de entrada" de la API: recibe las peticiones HTTP (desde
 * Postman o React), llama al Service y devuelve la respuesta en JSON.
 * NO tiene lógica de negocio: solo recibe, delega y responde.
 *
 * @RestController: combina @Controller + @ResponseBody. Todo lo que devuelven
 *   los métodos se convierte automáticamente a JSON (lo hace la librería Jackson).
 * @RequestMapping("/api/duenos"): ruta base de todos los endpoints de esta clase.
 * @CrossOrigin: habilita CORS, para que un frontend en otro puerto
 *   (ej: React en localhost:5173) pueda consumir la API sin que el navegador la bloquee.
 */
@RestController
@RequestMapping("/api/duenos")
@CrossOrigin(origins = "*")
public class DuenoController {

    private final DuenoService service;

    // Inyección de dependencias: el Controller usa el Service, nunca el Repository directo.
    public DuenoController(DuenoService service) {
        this.service = service;
    }

    // GET /api/duenos -> lista todos los dueños. Responde 200 OK.
    @GetMapping
    public List<Dueno> listar() {
        return service.listar();
    }

    // GET /api/duenos/1 -> @PathVariable toma el {id} de la URL.
    // 200 si existe, 404 si no (lo maneja el GlobalExceptionHandler).
    @GetMapping("/{id}")
    public Dueno buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    /*
     * POST /api/duenos -> crea un dueño.
     * @RequestBody: convierte el JSON del body en un objeto Dueno.
     * @Valid: activa las validaciones de la entidad (@NotBlank, @Email...).
     *   Si fallan, responde 400 sin entrar al método.
     * ResponseEntity permite elegir el código HTTP: aquí 201 CREATED,
     * que es el correcto cuando se crea un recurso.
     */
    @PostMapping
    public ResponseEntity<Dueno> crear(@Valid @RequestBody Dueno dueno) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dueno));
    }

    // PUT /api/duenos/1 -> actualiza el dueño completo. Responde 200 con el dueño actualizado.
    @PutMapping("/{id}")
    public Dueno actualizar(@PathVariable Long id, @Valid @RequestBody Dueno dueno) {
        return service.actualizar(id, dueno);
    }

    // DELETE /api/duenos/1 -> responde 204 NO CONTENT (se borró, no hay nada que devolver).
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ENDPOINT DE RELACIÓN: GET /api/duenos/1/mascotas -> mascotas del dueño 1.
    @GetMapping("/{id}/mascotas")
    public List<Mascota> mascotas(@PathVariable Long id) {
        return service.mascotasDelDueno(id);
    }
}
