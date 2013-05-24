package ru.ifmo.ctddev.bobrov.task8;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 24.05.13
 * Time: 14:35
 */
public interface TaskRunner {
    <X, Y> X run(Task<X, Y> task, Y value);
}
