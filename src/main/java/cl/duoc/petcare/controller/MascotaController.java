package cl.duoc.petcare.controller;

import cl.duoc.petcare.entity.Cita;
import cl.duoc.petcare.entity.Mascota;
import cl.duoc.petcare.service.MascotaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CAPA CONTROLLER de Mascota. Misma estructura que DuenoController:
 * recibe HTTP -> llama al Service -> devuelve JSON con el código adecuado.
 */
@RestController
@RequestMapping("/api/mascotas")
@CrossOrigin(origins = "*")
public class MascotaController {

    private final MascotaService service;

    public MascotaController(MascotaService service) {
        this.service = service;
    }

    // GET /api/mascotas -> 200 con la lista (cada mascota incluye su dueño).
    @GetMapping
    public List<Mascota> listar() {
        return service.listar();
    }

    // GET /api/mascotas/1 -> 200 o 404.
    @GetMapping("/{id}")
    public Mascota buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // POST /api/mascotas -> 201. El body debe incluir "dueno": {"id": X}.
    @PostMapping
    public ResponseEntity<Mascota> crear(@Valid @RequestBody Mascota mascota) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(mascota));
    }

    // PUT /api/mascotas/1 -> 200 con la mascota actualizada.
    @PutMapping("/{id}")
    public Mascota actualizar(@PathVariable Long id, @Valid @RequestBody Mascota mascota) {
        return service.actualizar(id, mascota);
    }

    // DELETE /api/mascotas/1 -> 204 (también borra sus citas por el cascade).
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // ENDPOINT DE RELACIÓN: GET /api/mascotas/1/citas -> historial de citas de la mascota 1.
    @GetMapping("/{id}/citas")
    public List<Cita> citas(@PathVariable Long id) {
        return service.citasDeMascota(id);
    }
}
