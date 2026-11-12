package cl.duoc.petcare.service;

import cl.duoc.petcare.entity.Dueno;
import cl.duoc.petcare.entity.Mascota;
import cl.duoc.petcare.exception.RecursoNoEncontradoException;
import cl.duoc.petcare.exception.ReglaNegocioException;
import cl.duoc.petcare.repository.DuenoRepository;
import cl.duoc.petcare.repository.MascotaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CAPA SERVICE
 * Aquí va la LÓGICA DE NEGOCIO y las VALIDACIONES (las reglas de la clínica).
 *
 * ¿Por qué no poner esto directo en el Controller?
 *  - Separación de responsabilidades: el Controller solo recibe y responde HTTP,
 *    el Repository solo habla con la BD, y el Service decide qué se puede hacer.
 *  - Si mañana se agrega otra forma de entrada (ej: una app móvil), las reglas
 *    ya están en un solo lugar y se reutilizan.
 *
 * @Service: marca la clase como componente de Spring (un "bean"), para que
 * Spring la cree y la pueda inyectar en el Controller.
 */
@Service
public class DuenoService {

    private final DuenoRepository duenoRepository;
    private final MascotaRepository mascotaRepository;

    /*
     * INYECCIÓN DE DEPENDENCIAS por constructor.
     * No hacemos "new DuenoRepository()": Spring ve que el constructor necesita
     * estos repositorios y nos entrega las instancias que ya creó.
     * Es la forma recomendada (mejor que @Autowired en el atributo).
     */
    public DuenoService(DuenoRepository duenoRepository, MascotaRepository mascotaRepository) {
        this.duenoRepository = duenoRepository;
        this.mascotaRepository = mascotaRepository;
    }

    public List<Dueno> listar() {
        return duenoRepository.findAll();
    }

    /*
     * findById devuelve un Optional (puede venir vacío si no existe el id).
     * orElseThrow: si está vacío, lanza nuestra excepción, que el
     * GlobalExceptionHandler convierte en una respuesta 404.
     * Este método se reutiliza en actualizar, eliminar, etc.
     */
    public Dueno buscarPorId(Long id) {
        return duenoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el dueño con id " + id));
    }

    public Dueno crear(Dueno dueno) {
        // Regla de negocio: el RUT no se puede repetir.
        if (duenoRepository.existsByRut(dueno.getRut())) {
            throw new ReglaNegocioException("Ya existe un dueño con rut " + dueno.getRut());
        }
        // Forzamos id null para que siempre sea un INSERT, aunque manden un id en el JSON.
        dueno.setId(null);
        return duenoRepository.save(dueno);
    }

    public Dueno actualizar(Long id, Dueno datos) {
        // 1) Verificamos que exista (si no, 404).
        Dueno dueno = buscarPorId(id);
        // 2) Si cambió el RUT, revisamos que el nuevo no lo tenga otro dueño.
        if (!dueno.getRut().equals(datos.getRut()) && duenoRepository.existsByRut(datos.getRut())) {
            throw new ReglaNegocioException("Ya existe un dueño con rut " + datos.getRut());
        }
        // 3) Copiamos los datos nuevos sobre el objeto existente y guardamos.
        //    Como ya tiene id, save() hace UPDATE.
        dueno.setRut(datos.getRut());
        dueno.setNombre(datos.getNombre());
        dueno.setTelefono(datos.getTelefono());
        dueno.setEmail(datos.getEmail());
        return duenoRepository.save(dueno);
    }

    public void eliminar(Long id) {
        Dueno dueno = buscarPorId(id);
        // Gracias a cascade = ALL en la entidad, se borran también
        // sus mascotas y las citas de esas mascotas.
        duenoRepository.delete(dueno);
    }

    /*
     * ENDPOINT DE RELACIÓN: GET /api/duenos/{id}/mascotas
     * Primero valida que el dueño exista (para devolver 404 y no una lista
     * vacía engañosa) y luego trae sus mascotas.
     */
    public List<Mascota> mascotasDelDueno(Long id) {
        buscarPorId(id);
        return mascotaRepository.findByDuenoId(id);
    }
}
