package ru.ifmo.ctddev.bobrov.task8;

import java.util.Random;

/**
 * Generates tasks in infinite loop and executes them using {@link TaskRunner}
 */
public class Client {
    /**
     * {@link TaskRunner} to execute tasks.
     */
    private final TaskRunner taskRunner;

    /**
     * Thread to generate tasks and receive results.
     */
    private final Thread taskGenerationThread;


    /**
     * Starts generating tasks for specified TaskRunner.
     *
     * @param taskRunner an executor for generated tasks.
     * @throws NullPointerException if parameter is null.
     */
    public Client(TaskRunner taskRunner) {
        if (taskRunner == null) {
            throw new NullPointerException("taskRunner must not be null");
        }
        this.taskRunner = taskRunner;
        taskGenerationThread = new Thread(this::generateTasks);
        taskGenerationThread.start();
    }

    /**
     * Generates tasks in infinite loop.
     */
    private void generateTasks() {
        Random rand = new Random();
        while (!Thread.interrupted()) {
            Integer x = rand.nextInt(42);
            String res = taskRunner.run((Integer v) -> v.toString(), x);
            System.out.println(Thread.currentThread() + " " + res);
        }
    }
}
