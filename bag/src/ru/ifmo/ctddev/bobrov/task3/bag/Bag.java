package ru.ifmo.ctddev.bobrov.task3.bag;

import java.util.*;

public class Bag<E> extends AbstractCollection<E> {
    private final Map<E, List<E>> data;
    private int modCount = 0;
    private long size = 0;


    public Bag() {
        data = new HashMap<>();
    }

    public Bag(Collection<? extends E> collection) {
        this();
        addAll(collection);
    }

    @Override
    public boolean contains(Object o) {
        return data.containsKey(o);
    }

    @Override
    public Iterator<E> iterator() {
        return new BagIterator();
    }

    @Override
    public boolean add(E element) {
        modCount++;
        size++;
        List<E> group = data.get(element);
        if (group == null) {
            group = new ArrayList<>();
            data.put(element, group);
        }
        return group.add(element);
    }

    @Override
    public boolean remove(Object element) {
        List<E> group = data.get(element);
        if (group == null) {
            return false;
        }
        size--;
        modCount++;
        group.remove(group.size() - 1);
        if (group.isEmpty()) {
            data.remove(element);
        }
        return true;
    }

    @Override
    public void clear() {
        data.clear();
        size = 0;
        modCount++;
    }

    @Override
    public int size() {
        return size > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) size;
    }

    private final class BagIterator implements Iterator<E> {
        private int expectedModCount = modCount;

        private final Iterator<E> mapIterator;
        private Iterator<E> groupIterator;
        private boolean canRemove = false;
        private E last;

        public BagIterator() {
            mapIterator = data.keySet().iterator();
        }

        @Override
        public boolean hasNext() {
            checkModCount();
            return mapIterator.hasNext() || (groupIterator != null && groupIterator.hasNext());
        }

        @Override
        public E next() {
            checkModCount();
            if (groupIterator == null || !groupIterator.hasNext()) {
                last = mapIterator.next();
                groupIterator = data.get(last).iterator();
            }
            E result = groupIterator.next();
            canRemove = true;
            return result;
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException("Remove called before next");
            }
            checkModCount();
            groupIterator.remove();
            List<E> group = data.get(last);
            if (group.isEmpty()) {
                mapIterator.remove();
            }
            size--;
            canRemove = false;
            expectedModCount = ++modCount;
        }

        private void checkModCount() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException("Iterator is not valid because collection is modified");
            }
        }
    }
}
