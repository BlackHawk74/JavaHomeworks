package ru.ifmo.ctddev.bobrov.task1;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 20.02.13
 * Time: 0:20
 */
public class MatrixConstructionException extends MatrixException {
    public MatrixConstructionException(String message) {
        super(message);
    }

    public MatrixConstructionException(Throwable cause) {
        super(cause);
    }

    public MatrixConstructionException(String message, Throwable cause) {
        super(message, cause);
    }
}
