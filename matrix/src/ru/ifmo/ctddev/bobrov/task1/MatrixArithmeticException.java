package ru.ifmo.ctddev.bobrov.task1;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 20.02.13
 * Time: 0:17
 */
public class MatrixArithmeticException extends MatrixException {

    public MatrixArithmeticException(String message) {
        super(message);
    }

    public MatrixArithmeticException(Throwable cause) {
        super(cause);
    }

    public MatrixArithmeticException(String message, Throwable cause) {
        super(message, cause);
    }
}
