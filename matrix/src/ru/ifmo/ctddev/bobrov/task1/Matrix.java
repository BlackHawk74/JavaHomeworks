package ru.ifmo.ctddev.bobrov.task1;


import java.io.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.function.ToDoubleBiFunction;
import java.util.function.ToIntFunction;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 20.02.13
 * Time: 0:05
 */
public class Matrix {

    static {
        Locale.setDefault(Locale.US);
    }

    private int rows, cols;
    private double[][] data;

    // <editor-fold desc="Construction from data">

    public Matrix(int rows, int cols) {
        setSizes(rows, cols);
        data = new double[rows][cols];
    }

    public Matrix(double[][] data) {
        if (data == null) {
            throw new NullPointerException("Parameter data must not be null");
        }

        ToIntFunction<double[]> mapper = (row) -> row == null ? 0 : row.length;
        int cols = Arrays.stream(data).map(mapper).reduce((a, b) -> a == b ? a : 0).getAsInt();
        setSizes(data.length, cols);
        this.data = data;
    }

    public Matrix(Matrix other) {
        if (other == null) {
            throw new NullPointerException("Parameter other should not be null");
        }
        data = new double[other.data.length][other.data[0].length];
        setSizes(data.length, data[0].length);
        for (int i = 0; i < rows; i++) {
            data[i] = Arrays.copyOf(other.data[i], cols);
        }
    }
    // </editor-fold>

    // <editor-fold desc="Construction from file">

    private interface IOSupplier<T> {
        public T get() throws IOException;
    }

    private void readMatrixData(final BufferedReader reader) throws IOException {
        IOSupplier<StringTokenizer> getLine = () -> {
            String line = reader.readLine();
            if (line == null) {
                throw new IOException("Incorrect file format");
            }
            return new StringTokenizer(line);
        };

        Function<StringTokenizer, String> nextToken = tokenizer ->
                tokenizer.hasMoreTokens() ? tokenizer.nextToken() : "";
        Function<StringTokenizer, Integer> nextInt = nextToken.compose(Integer::parseInt);
        Function<StringTokenizer, Double> nextDouble = nextToken.compose(Double::parseDouble);

        StringTokenizer tokenizer = getLine.get();
        try {
            int rows = nextInt.apply(tokenizer);
            int cols = nextInt.apply(tokenizer);
            setSizes(rows, cols);
            data = new double[rows][cols];
            for (int i = 0; i < rows; ++i) {
                tokenizer = getLine.get();
                for (int j = 0; j < cols; ++j) {
                    data[i][j] = nextDouble.apply(tokenizer);
                }
                if (tokenizer.hasMoreTokens()) {
                    throw new MatrixInputFormatException("Error in input file format: extra elements in line");
                }
            }
        } catch (NumberFormatException ex) {
            throw new MatrixInputFormatException("Error in input file format", ex);
        }
    }

    public Matrix(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            readMatrixData(reader);
        }
    }

    public Matrix(BufferedReader reader) throws IOException {
        readMatrixData(reader);
    }
    // </editor-fold>

    public void write(File file) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(toString());
        }
    }

    // <editor-fold desc="Validation methods">

    private void setSizes(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new MatrixConstructionException("Incorrect matrix sizes");
        }
        this.rows = rows;
        this.cols = cols;
    }

    private static void checkIndex(int value, int max, String name) {
        if (value <= 0 || value > max) {
            throw new MatrixWrongIndexException(String.format("Wrong index for %s: [1; %d] expected, %d given",
                    name, max, value));
        }
    }

    private void checkIndexes(int row, int col) {
        checkIndex(row, rows, "row");
        checkIndex(col, cols, "col");
    }

    private void checkSizesEqual(Matrix other) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new MatrixArithmeticException(String.format("Wrong operand size: %d x %d expected, %d x %d given",
                    rows, cols, other.rows, other.cols));
        }
    }

    // </editor-fold>

    // <editor-fold desc="Arithmetics">

    public Matrix scale(int value) {
        for (double[] row : data) {
            for (int i = 0; i < cols; ++i) {
                row[i] *= value;
            }
        }
        return this;
    }

    public Matrix add(Matrix rhs) {
        addImpl(rhs, (a, b) -> a + b);
        return this;
    }

    public Matrix subtract(Matrix rhs) {
        addImpl(rhs, (a, b) -> a - b);
        return this;
    }

    private void addImpl(Matrix rhs, ToDoubleBiFunction<Double, Double> function) {
        if (rhs == null) {
            throw new NullPointerException("Right argument is null");
        }
        checkSizesEqual(rhs);
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                data[i][j] = function.applyAsDouble(data[i][j], rhs.data[i][j]);
            }
        }
    }

    public Matrix multiply(Matrix rhs) {
        if (rhs == null) {
            throw new NullPointerException("Right argument is null");
        }
        if (this.cols != rhs.rows) {
            throw new MatrixArithmeticException("Wrong operand size, rhs should have " + cols + " rows");
        }
        double[][] newData = new double[rows][rhs.cols];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < rhs.cols; ++j) {
                for (int k = 0; k < cols; ++k) {
                    newData[i][j] += data[i][k] * rhs.data[k][j];
                }
            }
        }
        cols = rhs.cols;
        data = newData;
        return this;
    }

    public Matrix transpose() {
        double[][] transposedData = new double[cols][rows];
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                transposedData[j][i] = data[i][j];
            }
        }
        setSizes(cols, rows);
        data = transposedData;
        return this;
    }

    // </editor-fold>

    // <editor-fold desc="Getters and setters">

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public double get(int row, int col) {
        checkIndexes(row, col);
        return data[row - 1][col - 1];
    }

    public Matrix set(int row, int col, int value) {
        checkIndexes(row, col);
        data[row - 1][col - 1] = value;
        return this;
    }

    // </editor-fold>

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(rows).append(' ').append(cols).append('\n');
        for (double[] row : data) {
            boolean first = true;
            for (double el : row) {
                builder.append(first ? el + "" : " " + el);
                first = false;
            }
            builder.append('\n');
        }
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Matrix) {
            Matrix m = (Matrix) o;
            if (rows != m.rows || cols != m.cols) {
                return false;
            }
            for (int i = 0; i < rows; ++i) {
                for (int j = 0; j < cols; ++j) {
                    if (data[i][j] != m.data[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
}
