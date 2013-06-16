package ru.ifmo.ctddev.bobrov.task8;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Multithreaded implementation of {@link TaskRunner}
 */
public class TaskRunnerImpl implements TaskRunner {
    /**
     * {@link ExecutorService} where tasks are executed.
     */
    private final ExecutorService executorService;

    /**
     * Constructs the task runner.
     *
     * @param threadCount count of worker threads.
     * @throws IllegalArgumentException if threadCount is non-positive.
     */
    public TaskRunnerImpl(int threadCount) {
        if (threadCount <= 0) {
            throw new IllegalArgumentException("Thread count must be positive");
        }
        executorService = Executors.newFixedThreadPool(threadCount);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NullPointerException if task is null.
     * @throws java.util.concurrent.RejectedExecutionException if the task cannot be scheduled for execution.
     */
    @Override
    public <X, Y> X run(Task<X, Y> task, Y value) {
        if (task == null) {
            throw new NullPointerException("task parameter is null");
        }
        Future<X> future = executorService.submit(() -> {
            return task.run(value);
        });

        try {
            return future.get();
        } catch (InterruptedException e) {
            System.err.println("Result waiting was interrupted");
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.err.println("Task caused an error, cause" + e.getCause());
        }
        return null;
    }
}
