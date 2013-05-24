package ru.ifmo.ctddev.bobrov.task7;

import java.util.concurrent.*;

/**
 * Multithreaded implementation of {@link TaskRunner}
 */
public class TaskRunnerImpl implements TaskRunner {
    /**
     * A task queue.
     */
    private final LinkedBlockingQueue<Runnable> tasks;

    /**
     * Constructs the task runner. Starts worker threads.
     * @param threadCount count of worker threads.
     * @throws IllegalArgumentException if threadCount is non-positive.
     */
    public TaskRunnerImpl(int threadCount) {
        if (threadCount <= 0) {
            throw new IllegalArgumentException("Thread count must be positive");
        }
        Thread[] threadPool = new Thread[threadCount];
        tasks = new LinkedBlockingQueue<>(threadCount * 2);
        for (int i = 0; i < threadCount; i++) {
            threadPool[i] = new Thread(() -> {
                while (!Thread.interrupted()) {
                    try {
                        Runnable task = tasks.take();
                        task.run();
                    } catch (InterruptedException e) {
                        System.err.println("Thread interrupted");
                        Thread.currentThread().interrupt();
                    }
                }
            });
            threadPool[i].start();
        }
    }

    /**
     * {@inheritDoc}
     * @throws NullPointerException if task is null.
     */
    @Override
    public <X, Y> X run(Task<X, Y> task, Y value) {
        if (task == null) {
            throw new NullPointerException("task parameter is null");
        }
        final Exchanger<X> exchanger = new Exchanger<>();
        final Thread mainThread = Thread.currentThread();
        try {
            tasks.put(() -> {
                try {
                    exchanger.exchange(task.run(value));
                } catch (InterruptedException e) {
                    System.err.println("Worker thread interrupted while running task");
                    Thread.currentThread().interrupt();
                    mainThread.interrupt();
                }
            });
        } catch (InterruptedException e) {
            showErrorMessageAndExit();
        }
        try {
            return exchanger.exchange(null);
        } catch (InterruptedException e) {
            showErrorMessageAndExit();
            return null;
        }
    }

    /**
     * Displays error message and terminates program execution.
     */
    private void showErrorMessageAndExit() {
        System.err.println("Main thread interrupted, exiting");
        System.exit(1);
    }
}
