package ru.ifmo.ctddev.bobrov.task1;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 20.02.13
 * Time: 23:43
 */
public class Tests {
    @Test
    public void testConstructors() {
        Assert.assertEquals(
                new Matrix(2, 3),
                new Matrix(new double[][]{
                        {0, 0, 0},
                        {0, 0, 0}
                }));
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNull() {
        new Matrix((double[][]) null);
    }

    @Test(expected = MatrixConstructionException.class)
    public void testConstructorInvalidRows() {
        new Matrix(0, 1);
    }

    @Test(expected = MatrixConstructionException.class)
    public void testConstructorInvalidCols() {
        new Matrix(1, 0);
    }

    @Test(expected = MatrixConstructionException.class)
    public void testConstructorInvalidData1() {
        new Matrix(new double[][]{
                {1, 2},
                {3}
        });
    }

    @Test(expected = MatrixConstructionException.class)
    public void testConstructorInvalidData2() {
        new Matrix(new double[][]{
                null,
                {2, 3}
        });
    }

    @Test
    public void testSizes() {
        Matrix m = new Matrix(1, 2);
        Assert.assertEquals(1, m.getRows());
        Assert.assertEquals(2, m.getCols());
    }

    @Test
    public void testGet() {
        Matrix matrix = new Matrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6}});
        Assert.assertEquals(1, matrix.get(1, 1), 1e-10);
    }

    @Test(expected = MatrixWrongIndexException.class)
    public void testGetInvalid() {
        new Matrix(1, 1).get(2, 1);
    }

    @Test
    public void testSet() {
        Matrix matrix = new Matrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6}}
        );
        matrix.set(1, 1, 50);
        Assert.assertEquals(50, matrix.get(1, 1), 1e-10);
    }

    @Test(expected = MatrixWrongIndexException.class)
    public void testSetInvalid() {
        new Matrix(2, 3).set(2, 4, 1);
    }

    @Test
    public void testCopyConstructor() {
        Matrix matrix = new Matrix(new double[][]{{1}});
        Matrix copy = new Matrix(matrix);
        Assert.assertEquals(matrix, copy);
        matrix.set(1, 1, 2);
        Assert.assertEquals(1, copy.get(1, 1), 1e-10);
    }

    @Test(expected = NullPointerException.class)
    public void testCopyConstructorNull() {
        new Matrix((Matrix) null);
    }

    @Test
    public void testAdd() {
        Matrix l = new Matrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6}});
        Matrix r = new Matrix(new double[][]{
                {6, 5, 4},
                {3, 2, 1}});
        l.add(r);
        Assert.assertEquals(new Matrix(new double[][]{
                {7, 7, 7},
                {7, 7, 7}}),
                l);
    }

    @Test(expected = MatrixArithmeticException.class)
    public void testAddInvalid() {
        Matrix l = new Matrix(1, 2);
        Matrix r = new Matrix(2, 1);
        l.add(r);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNull() {
        new Matrix(1, 2).add(null);
    }

    @Test
    public void testSubtract() {
        Matrix l = new Matrix(new double[][]{
                {1, 2, 3, 4},
                {5, 6, 7, 8}});
        Matrix r = new Matrix(new double[][]{
                {1, 4, 9, 16},
                {25, 36, 49, 64}});
        l.subtract(r);
        Assert.assertEquals(new Matrix(new double[][]{
                {0, -2, -6, -12},
                {-20, -30, -42, -56}}), l);
    }

    @Test
    public void testScale() {
        Matrix l = new Matrix(new double[][]{
                {1, 2, 3},
                {1, 2, 3}});
        Assert.assertEquals(new Matrix(new double[][]{
                {2, 4, 6},
                {2, 4, 6}}), l.scale(2));
    }

    @Test
    public void testMultiply() {
        Matrix l = new Matrix(new double[][]{
                {1, 2, 3},
                {4, 5, 6}});
        Matrix r = new Matrix(new double[][]{
                {1, 2},
                {3, 4},
                {5, 6}});
        Matrix res = new Matrix(new double[][]{
                {22, 28},
                {49, 64}});
        Assert.assertEquals(res, l.multiply(r));
    }

    @Test(expected = MatrixArithmeticException.class)
    public void testMultiplyInvalid() {
        new Matrix(2, 4).multiply(new Matrix(3, 4));
    }

    @Test
    public void testTranspose() {
        Matrix matrix = new Matrix(new double[][]{
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 0}
        });
        Matrix result = new Matrix(new double[][]{
                {1, 6},
                {2, 7},
                {3, 8},
                {4, 9},
                {5, 0}
        });
        Assert.assertEquals(result, matrix.transpose());
    }
}
