package cl.duoc.petcare.repository;

import cl.duoc.petcare.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * CAPA REPOSITORY de Cita.
 * Hereda el CRUD básico de JpaRepository.
 */
public interface CitaRepository extends JpaRepository<Cita, Long> {

    /*
     * Query method para el endpoint GET /api/mascotas/{id}/citas.
     *   SELECT * FROM citas WHERE mascota_id = ?
     */
    List<Cita> findByMascotaId(Long mascotaId);
}
