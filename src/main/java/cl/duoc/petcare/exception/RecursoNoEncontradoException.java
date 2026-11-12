package cl.duoc.petcare.exception;

/**
 * Excepción propia para cuando se busca un id que no existe.
 * Extiende RuntimeException (excepción "no chequeada"), así no hay que
 * declararla con "throws" en cada método.
 * El GlobalExceptionHandler la convierte en un 404.
 */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
