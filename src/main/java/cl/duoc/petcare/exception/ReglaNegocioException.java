package cl.duoc.petcare.exception;

/**
 * Excepción propia para cuando se rompe una regla de negocio
 * (rut repetido, fecha de nacimiento futura, falta el dueño, etc.).
 * El GlobalExceptionHandler la convierte en un 400.
 */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
