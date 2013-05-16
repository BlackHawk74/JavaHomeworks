package ru.ifmo.ctddev.bobrov.task1;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 14.03.13
 * Time: 13:21
 */
public class MatrixInputFormatException extends IOException {
    public MatrixInputFormatException(String message) {
        super(message);
    }

    public MatrixInputFormatException(Throwable cause) {
        super(cause);
    }

    public MatrixInputFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
