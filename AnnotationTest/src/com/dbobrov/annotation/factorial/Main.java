package com.dbobrov.annotation.factorial;

/**
 * Created with IntelliJ IDEA.
 * User: blackhawk
 * Date: 01.05.13
 * Time: 0:35
 */
public class Main {

    @Factorial(3)
    private static final int fact() {
        return -1;
    }

    @Factorial(4)
    private static final int ff = -1;

    public static void main(String... args) {
        System.out.println(fact());
        System.out.println(ff);
    }
}
