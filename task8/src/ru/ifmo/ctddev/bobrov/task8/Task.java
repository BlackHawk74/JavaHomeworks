package ru.ifmo.ctddev.bobrov.task8;

/**
 * A functional interface to store tasks.
 *
 * @param <X> function result type.
 * @param <Y> function argument type.
 */
public interface Task<X, Y> {
    /**
     * Performs some calculation.
     *
     * @param value a single argument of a function.
     * @return result of calculation.
     */
    X run(Y value);
}
