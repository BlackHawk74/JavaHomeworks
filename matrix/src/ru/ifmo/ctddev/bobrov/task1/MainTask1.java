package ru.ifmo.ctddev.bobrov.task1;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 20.02.13
 * Time: 0:05
 */
public class MainTask1 {
    public static void main(String... args) {
        double[][] lData = new double[][]{
                {1, 2},
                {3, 4}
        };

        double[][] rData = new double[][]{
                {5, 6},
                {7, 8}
        };

        try {
            Matrix l = new Matrix(lData), r = new Matrix(rData);
            System.out.print(l.multiply(new Matrix(l).transpose()).subtract(r));
//            System.out.print(l.multiply(r));
        } catch (MatrixException | NullPointerException e) {
            System.out.println("An error occured: " + e.getMessage());
        }
    }
}
