package ru.ifmo.ctddev.bobrov.task8;

/**
 * An interface for running tasks.
 */
public interface TaskRunner {
    /**
     * Executes task with given argument.
     *
     * @param task  a task to execute.
     * @param value a parameter that will be passed to a task.
     * @param <X>   task return type.
     * @param <Y>   task argument type.
     * @return task execution result.
     */
    <X, Y> X run(Task<X, Y> task, Y value);
}
