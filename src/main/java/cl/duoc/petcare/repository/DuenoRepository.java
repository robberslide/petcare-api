package cl.duoc.petcare.repository;

import cl.duoc.petcare.entity.Dueno;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CAPA REPOSITORY
 * Es la capa que habla con la base de datos.
 *
 * Es solo una INTERFAZ: no escribimos la implementación. Spring Data JPA la
 * crea automáticamente al iniciar la app.
 *
 * JpaRepository<Dueno, Long> = <Entidad que maneja, tipo de su id>.
 * Al heredar de JpaRepository ya tenemos gratis:
 *   findAll(), findById(id), save(obj), delete(obj), existsById(id), count()...
 * save() sirve para INSERT (si el id es null) y para UPDATE (si ya tiene id).
 */
public interface DuenoRepository extends JpaRepository<Dueno, Long> {

    /*
     * "Query method": Spring lee el NOMBRE del método y genera el SQL solo.
     * existsByRut -> SELECT COUNT(*) > 0 FROM duenos WHERE rut = ?
     * Lo usamos para no permitir RUTs repetidos.
     */
    boolean existsByRut(String rut);
}
