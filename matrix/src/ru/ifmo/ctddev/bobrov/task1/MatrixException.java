package ru.ifmo.ctddev.bobrov.task1;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 20.02.13
 * Time: 0:15
 */
public class MatrixException extends RuntimeException {
    public MatrixException(String message) {
        super(message);
    }

    public MatrixException(Throwable cause) {
        super(cause);
    }

    public MatrixException(String message, Throwable cause) {
        super(message, cause);
    }
}
