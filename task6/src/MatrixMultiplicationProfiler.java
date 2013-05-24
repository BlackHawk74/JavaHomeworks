import java.util.concurrent.CountDownLatch;

/**
 * Calculates matrix product in  multiple threads.
 *
 * @author Dmitry Bobrov
 */
public class MatrixMultiplicationProfiler {
    /**
     * multiplicand
     */
    private final int[][] a;

    /**
     * multiplier
     */
    private final int[][] b;

    /**
     * result
     */
    private final int[][] c;

    /**
     * count of worker threads.
     */
    private final int threadCount;

    /**
     * Constructs a new profiler with given parameters.
     *
     * @param a           multiplicand.
     * @param b           multiplier.
     * @param c           result.
     * @param threadCount count of worker threads.
     * @throws NullPointerException if one of the matrices is null.
     */
    public MatrixMultiplicationProfiler(int[][] a, int[][] b, int[][] c, int threadCount) {
        if (a == null || b == null || c == null) {
            throw new NullPointerException();
        }

        this.a = a;
        this.b = b;
        this.c = c;

        this.threadCount = threadCount;
    }

    /**
     * Calculates matrix product.
     *
     * @return the time of calculation.
     */
    public long multiply() {
        final CountDownLatch startSignal = new CountDownLatch(1);
        final CountDownLatch doneSignal = new CountDownLatch(threadCount);
        final int matrixSize = a.length;

        int elementsPerThread = matrixSize * matrixSize / threadCount;
        int currentElement = 0;

        for (int i = 0; i < threadCount; i++) {
            int currentThreadElements = i == threadCount - 1 ?
                    matrixSize * matrixSize - currentElement : elementsPerThread;
            new Thread(new Worker(currentElement / matrixSize,
                    currentElement % matrixSize, currentThreadElements, startSignal, doneSignal))
                    .start();
            currentElement += currentThreadElements;
        }

        long begin = System.nanoTime();
        startSignal.countDown();
        try {
            doneSignal.await();
        } catch (InterruptedException e) {
            System.err.println("Waiting for result interrupted");
            return -1;
        }
        long end = System.nanoTime();
        return end - begin;
    }

    /**
     * Calculates part of matrix product.
     */
    private class Worker implements Runnable {
        private final CountDownLatch startSignal, doneSignal;
        private int row;
        private int col;
        private int count;

        /**
         * Assigns a part of matrix to calculate a product.
         *
         * @param row         a row of the result matrix where to start calculation.
         * @param col         a column of the result matrix where to start calculation.
         * @param count       count of elements to be calculated.
         * @param startSignal a barrier to start calculation.
         * @param doneSignal  a barrier to tell main thread that work is done.
         */
        public Worker(int row, int col, int count, CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.col = col;
            this.row = row;
            this.count = count;
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        @Override
        public void run() {
            try {
                startSignal.await();
                calculateResult();
            } catch (InterruptedException e) {
                System.err.println("InterruptedException, aborting");
            } finally {
                doneSignal.countDown();
            }
        }

        /**
         * Performs a calculation.
         *
         * @throws ArrayIndexOutOfBoundsException if matrices are not square or have different sizes.
         */
        private void calculateResult() {
            while (count > 0) {
                c[row][col] = 0;
                for (int i = 0; i < a.length; i++) {
                    c[row][col] += a[row][i] * b[i][col];
                }
                count--;
                if (col == a.length - 1) {
                    row++;
                    col = 0;
                } else {
                    col++;
                }
            }
        }
    }

}
