package ru.ifmo.ctddev.bobrov.task3;

import ru.ifmo.ctddev.bobrov.task3.bag.LinkedBag;

public class Main {
    public static void main(String... args) {
        LinkedBag bag = new LinkedBag();
        bag.add("1");
        bag.add("3");
        bag.add("1");
        bag.add("2");
        bag.add("1");
        bag.add("2");
        bag.remove("2");
        for (Object o : bag) {
            System.out.println(o);
        }
        System.out.println(bag.size());
    }
}
