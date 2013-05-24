package ru.ifmo.ctddev.bobrov.task8;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 24.05.13
 * Time: 14:36
 */
public class TaskRunnerImpl implements TaskRunner {
    private final ExecutorService executorService;

    public TaskRunnerImpl(int threadCount) {
        if (threadCount <= 0) {
            throw new IllegalArgumentException("Thread count must be positive");
        }
        executorService = Executors.newFixedThreadPool(threadCount);
    }

    @Override
    public <X, Y> X run(Task<X, Y> task, Y value) {
        Future<X> future = executorService.submit(() -> {
            return task.run(value);
        });

        try {
            return future.get();
        } catch (InterruptedException e) {
            System.err.println("Result waiting was interrupted");
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            System.err.println("ru.ifmo.ctddev.bobrov.task8.Task caused an error, cause" + e.getCause());
        }
        return null;
    }
}
