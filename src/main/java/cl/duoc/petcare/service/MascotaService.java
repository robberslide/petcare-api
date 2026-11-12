package cl.duoc.petcare.service;

import cl.duoc.petcare.entity.Cita;
import cl.duoc.petcare.entity.Dueno;
import cl.duoc.petcare.entity.Mascota;
import cl.duoc.petcare.exception.RecursoNoEncontradoException;
import cl.duoc.petcare.exception.ReglaNegocioException;
import cl.duoc.petcare.repository.CitaRepository;
import cl.duoc.petcare.repository.DuenoRepository;
import cl.duoc.petcare.repository.MascotaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * CAPA SERVICE de Mascota.
 * Usa 3 repositorios porque una mascota se relaciona con dueños y con citas.
 */
@Service
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final DuenoRepository duenoRepository;
    private final CitaRepository citaRepository;

    // Inyección de dependencias por constructor (Spring entrega los repositorios).
    public MascotaService(MascotaRepository mascotaRepository,
                          DuenoRepository duenoRepository,
                          CitaRepository citaRepository) {
        this.mascotaRepository = mascotaRepository;
        this.duenoRepository = duenoRepository;
        this.citaRepository = citaRepository;
    }

    public List<Mascota> listar() {
        return mascotaRepository.findAll();
    }

    public Mascota buscarPorId(Long id) {
        return mascotaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la mascota con id " + id));
    }

    public Mascota crear(Mascota mascota) {
        mascota.setId(null);
        // El JSON solo trae "dueno": {"id": 1}. Aquí lo cambiamos por el
        // dueño REAL de la BD (y si no existe, se lanza 404).
        mascota.setDueno(obtenerDueno(mascota));
        validarFecha(mascota);
        return mascotaRepository.save(mascota);
    }

    public Mascota actualizar(Long id, Mascota datos) {
        Mascota mascota = buscarPorId(id);
        mascota.setNombre(datos.getNombre());
        mascota.setEspecie(datos.getEspecie());
        mascota.setRaza(datos.getRaza());
        mascota.setFechaNacimiento(datos.getFechaNacimiento());
        // Solo si mandan un dueño nuevo se cambia (permite "traspasar" la mascota).
        if (datos.getDueno() != null) {
            mascota.setDueno(obtenerDueno(datos));
        }
        validarFecha(mascota);
        return mascotaRepository.save(mascota);
    }

    public void eliminar(Long id) {
        // cascade = ALL: al borrar la mascota se borran sus citas.
        mascotaRepository.delete(buscarPorId(id));
    }

    /*
     * ENDPOINT DE RELACIÓN: GET /api/mascotas/{id}/citas
     * Valida que la mascota exista y devuelve su historial de citas.
     */
    public List<Cita> citasDeMascota(Long id) {
        buscarPorId(id);
        return citaRepository.findByMascotaId(id);
    }

    /*
     * VALIDACIÓN: no se puede crear una mascota para un dueño inexistente.
     * - Si no mandan dueño -> 400 (Bad Request).
     * - Si mandan un id que no existe en la BD -> 404 (Not Found).
     * Es "private" porque es un método de apoyo interno del Service.
     */
    private Dueno obtenerDueno(Mascota mascota) {
        if (mascota.getDueno() == null || mascota.getDueno().getId() == null) {
            throw new ReglaNegocioException("Debe indicar el dueño: \"dueno\": {\"id\": 1}");
        }
        Long duenoId = mascota.getDueno().getId();
        return duenoRepository.findById(duenoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el dueño con id " + duenoId));
    }

    // VALIDACIÓN: una mascota no puede haber nacido en el futuro.
    private void validarFecha(Mascota mascota) {
        if (mascota.getFechaNacimiento() != null && mascota.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new ReglaNegocioException("La fecha de nacimiento no puede ser futura");
        }
    }
}
