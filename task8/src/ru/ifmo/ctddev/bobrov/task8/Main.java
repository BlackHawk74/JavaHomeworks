package ru.ifmo.ctddev.bobrov.task8;

/**
 * Starts generating and executing tasks in multiple threads.
 */
public class Main {
    /**
     * Runs on program startup.
     * @param args if first argument is set and is positive number, sets number of worker threads to run tasks. If second argument is set and is positive number, sets number of generation threads.
     */
    public static void main(String ... args) {
        int producers = 2;
        int consumers = 2;

        if (args.length > 0) {
            consumers = tryParsePositiveInt(args[0], consumers);
        }

        if (args.length > 1) {
            producers = tryParsePositiveInt(args[1], producers);
        }

        TaskRunner taskRunner = new TaskRunnerImpl(consumers);
        for (int i = 0; i < producers; i++) {
            new Client(taskRunner);
        }
    }

    /**
     * Parses a positive integer represented by parameter.
     * @param str a string containing number.
     * @param fallback a value to return if parsing failed.
     * @return a positive number represented by argument string.
     */
    private static int tryParsePositiveInt(String str, int fallback) {
        try {
            int result = Integer.parseInt(str);
            return result > 0 ? result : fallback;
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }
}
