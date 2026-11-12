package cl.duoc.petcare.controller;

import cl.duoc.petcare.entity.Cita;
import cl.duoc.petcare.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CAPA CONTROLLER de Cita.
 * Recibe HTTP -> llama a CitaService -> devuelve JSON.
 */
@RestController
@RequestMapping("/api/citas")
@CrossOrigin(origins = "*")
public class CitaController {

    private final CitaService service;

    public CitaController(CitaService service) {
        this.service = service;
    }

    // GET /api/citas -> 200 con todas las citas (cada una muestra su mascota y el dueño).
    @GetMapping
    public List<Cita> listar() {
        return service.listar();
    }

    // GET /api/citas/1 -> 200 o 404.
    @GetMapping("/{id}")
    public Cita buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // POST /api/citas -> 201. Si la mascota no existe -> 404 (validación del Service).
    @PostMapping
    public ResponseEntity<Cita> crear(@Valid @RequestBody Cita cita) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(cita));
    }

    // PUT /api/citas/1 -> 200 (ej: agregar el diagnóstico y costo después de la atención).
    @PutMapping("/{id}")
    public Cita actualizar(@PathVariable Long id, @Valid @RequestBody Cita cita) {
        return service.actualizar(id, cita);
    }

    // DELETE /api/citas/1 -> 204.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
