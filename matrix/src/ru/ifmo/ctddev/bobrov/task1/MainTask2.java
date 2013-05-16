package ru.ifmo.ctddev.bobrov.task1;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 26.02.13
 * Time: 23:07
 */
public class MainTask2 {
    public static void main(String... args) {
        if (args.length != 2) {
            System.out.println("Usage: java MainTask2 inputFile outputFile");
            return;
        }
        File inputFile = new File(args[0]);
        File outputFile = new File(args[1]);
        if (!inputFile.canRead()) {
            System.err.println("Error: cannot open input file for reading");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            Matrix first = new Matrix(reader);
            Matrix second = new Matrix(reader);
            Matrix third = new Matrix(reader);
            first
                    .multiply(new Matrix(first))
                    .add(second.multiply(third))
                    .write(outputFile);
        } catch (IOException ex) {
            System.err.println("IO error: " + ex.getMessage());
        } catch (MatrixException ex) {
            System.err.println("Matrix error: " + ex.getMessage());
        }
    }
}
