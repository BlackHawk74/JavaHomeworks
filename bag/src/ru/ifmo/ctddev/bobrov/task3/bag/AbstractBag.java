package ru.ifmo.ctddev.bobrov.task3.bag;

import java.util.Arrays;
import java.util.Collection;

public abstract class AbstractBag implements Collection {
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public Object[] toArray() {
        return toArray(new Object[size()]);
    }

    @Override
    public Object[] toArray(Object[] objects) {
        if (objects.length < size()) {
            objects = new Object[size()];
        }
        int i = 0;
        for (Object obj : this) {
            objects[i++] = obj;
        }
        return objects;
    }

    @Override
    public boolean containsAll(Collection collection) {
        return collection.stream().allMatch(obj -> contains(obj));
    }

    @Override
    public boolean addAll(Collection collection) {
        if (collection == this) {
            return addAll(Arrays.asList(collection.toArray()));
        }
        return collection.stream().allMatch(obj -> add(obj));
    }
}
