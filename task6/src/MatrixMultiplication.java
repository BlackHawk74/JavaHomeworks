import java.util.Random;

public class MatrixMultiplication {

    /**
     * Launches random matrix multiplication.
     *
     * @param args first element — positive number representing matrices size, second — positive number representing thread count, third — if set and equals "graphgen", makes program generate output for easy parsing.
     */
    public static void main(String... args) {
        if (args.length < 2) {
            System.err.println("Usage: MatrixMultiplication matrixSize threadCount");
            return;
        }

        int matrixSize = tryParsePositiveInt(args[0]);
        if (matrixSize < 1) {
            System.err.println("Matrix size must be a positive number");
            return;
        }

        int threadCount = tryParsePositiveInt(args[1]);
        if (threadCount < 1) {
            System.err.println("Thread count must be a positive number");
            return;
        }

        final int[][] a = generateRandomMatrix(matrixSize);
        final int[][] b = generateRandomMatrix(matrixSize);
        final int[][] result = new int[matrixSize][matrixSize];

        MatrixMultiplicationProfiler multiplier = new MatrixMultiplicationProfiler(a, b, result, threadCount);
        long ns = multiplier.multiply();

        int sum = 0;
        for (int[] row : result) {
            for (int i : row) {
                sum += i;
            }
        }

        if (args.length > 2 && args[2].equals("graphgen")) {
            System.out.println(threadCount + " " + ns / 1000);
            System.err.println(sum);
        } else {
            System.out.println("Multiplication time: " + ns / 1000 + " µs");
            System.out.println("Matrix elements sum: " + sum);
        }
    }


    /**
     * Parses the string argument as positive decimal number.
     *
     * @param s a string containing number to be parsed.
     * @return positive number represented by the argument or negative number, if argument does not contain one.
     */
    private static int tryParsePositiveInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    /**
     * Generates random square matrix
     *
     * @param size size of matrix to be generated.
     * @return random square matrix with size represented by the argument.
     * @throws java.lang.NegativeArraySizeException if the argument is negative.
     */
    private static int[][] generateRandomMatrix(int size) {
        int[][] result = new int[size][size];
        Random random = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result[i][j] = random.nextInt();
            }
        }
        return result;
    }
}
