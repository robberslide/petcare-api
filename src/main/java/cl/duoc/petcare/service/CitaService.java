package cl.duoc.petcare.service;

import cl.duoc.petcare.entity.Cita;
import cl.duoc.petcare.entity.Mascota;
import cl.duoc.petcare.exception.RecursoNoEncontradoException;
import cl.duoc.petcare.exception.ReglaNegocioException;
import cl.duoc.petcare.repository.CitaRepository;
import cl.duoc.petcare.repository.MascotaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CAPA SERVICE de Cita.
 * Contiene la validación que pide explícitamente el enunciado:
 * "no crear una cita para una mascota inexistente".
 */
@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final MascotaRepository mascotaRepository;

    public CitaService(CitaRepository citaRepository, MascotaRepository mascotaRepository) {
        this.citaRepository = citaRepository;
        this.mascotaRepository = mascotaRepository;
    }

    public List<Cita> listar() {
        return citaRepository.findAll();
    }

    public Cita buscarPorId(Long id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la cita con id " + id));
    }

    public Cita crear(Cita cita) {
        cita.setId(null);
        // Aquí se aplica la validación principal: si la mascota no existe,
        // obtenerMascota() lanza la excepción y la cita NUNCA se guarda.
        cita.setMascota(obtenerMascota(cita));
        return citaRepository.save(cita);
    }

    public Cita actualizar(Long id, Cita datos) {
        Cita cita = buscarPorId(id);
        cita.setFecha(datos.getFecha());
        cita.setMotivo(datos.getMotivo());
        cita.setDiagnostico(datos.getDiagnostico());
        cita.setCosto(datos.getCosto());
        if (datos.getMascota() != null) {
            cita.setMascota(obtenerMascota(datos));
        }
        return citaRepository.save(cita);
    }

    public void eliminar(Long id) {
        citaRepository.delete(buscarPorId(id));
    }

    /*
     * VALIDACIÓN DEL ENUNCIADO: no crear una cita para una mascota inexistente.
     * Ejemplo en Postman: POST /api/citas con "mascota": {"id": 999}
     *   -> responde 404 "No existe la mascota con id 999".
     */
    private Mascota obtenerMascota(Cita cita) {
        if (cita.getMascota() == null || cita.getMascota().getId() == null) {
            throw new ReglaNegocioException("Debe indicar la mascota: \"mascota\": {\"id\": 1}");
        }
        Long mascotaId = cita.getMascota().getId();
        return mascotaRepository.findById(mascotaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la mascota con id " + mascotaId));
    }
}
