package ru.ifmo.ctddev.bobrov.task1;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 20.02.13
 * Time: 0:41
 */
public class MatrixWrongIndexException extends MatrixException {
    public MatrixWrongIndexException(String message) {
        super(message);
    }

    public MatrixWrongIndexException(Throwable cause) {
        super(cause);
    }

    public MatrixWrongIndexException(String message, Throwable cause) {
        super(message, cause);
    }
}
