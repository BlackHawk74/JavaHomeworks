import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 15.05.13
 * Time: 23:15
 */
public class MatrixMultiplication {

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
    }

    private static int tryParsePositiveInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

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
