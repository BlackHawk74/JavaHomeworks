package ru.ifmo.ctddev.bobrov.task8;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 24.05.13
 * Time: 14:34
 */
public interface Task<X, Y> {
    X run(Y value);
}
