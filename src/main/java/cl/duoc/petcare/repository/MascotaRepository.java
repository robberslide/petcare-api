package cl.duoc.petcare.repository;

import cl.duoc.petcare.entity.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * CAPA REPOSITORY de Mascota.
 * Hereda el CRUD básico de JpaRepository.
 */
public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    /*
     * Query method para el endpoint de relación GET /api/duenos/{id}/mascotas.
     * findByDuenoId -> Spring entiende "buscar por el atributo dueno, campo id":
     *   SELECT * FROM mascotas WHERE dueno_id = ?
     */
    List<Mascota> findByDuenoId(Long duenoId);
}
