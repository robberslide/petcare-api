package cl.duoc.petcare.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MANEJO GLOBAL DE ERRORES
 *
 * @RestControllerAdvice: "escucha" las excepciones que se lanzan en CUALQUIER
 * Controller o Service y las transforma en una respuesta JSON ordenada.
 *
 * Sin esta clase, un error devolvería un 500 genérico con un stacktrace feo.
 * Con ella, Postman muestra algo claro, por ejemplo:
 *   { "status": 404, "mensaje": "No existe la mascota con id 999" }
 *
 * Así evitamos poner try/catch en cada método del Controller.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Recurso no encontrado (id que no existe) -> 404 NOT FOUND
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> noEncontrado(RecursoNoEncontradoException ex) {
        return respuesta(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Se rompió una regla de negocio (rut repetido, fecha futura...) -> 400 BAD REQUEST
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<Map<String, Object>> reglaNegocio(ReglaNegocioException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    /*
     * Fallaron las validaciones de @Valid (@NotBlank, @Email, etc.) -> 400.
     * Junta todos los errores por campo, ej:
     *   "errores": { "nombre": "El nombre es obligatorio", "email": "Email no válido" }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errores.put(e.getField(), e.getDefaultMessage()));
        Map<String, Object> body = base(HttpStatus.BAD_REQUEST, "Datos inválidos");
        body.put("errores", errores);
        return ResponseEntity.badRequest().body(body);
    }

    // Métodos de apoyo para armar siempre el mismo formato de respuesta.
    private ResponseEntity<Map<String, Object>> respuesta(HttpStatus status, String mensaje) {
        return ResponseEntity.status(status).body(base(status, mensaje));
    }

    private Map<String, Object> base(HttpStatus status, String mensaje) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("mensaje", mensaje);
        return body;
    }
}
