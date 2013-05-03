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
        if (element == null) {
            throw new NullPointerException();
        }
        modCount++;
        size++;
        if (!contains(element)) {
            data.put(element, new ArrayList<E>() {{
                add(0, element);
            }});
            return true;
        }
        List<E> group = data.get(element);
        return group.add(element);
    }

    @Override
    public boolean remove(Object element) {
        if (element == null) {
            throw new NullPointerException();
        }
        return removeAndGet(element) != null;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        boolean modified = false;
        if (collection == this) {
            modified = !isEmpty();
            clear();
            return modified;
        }
        for (Object o: collection) {
            if (data.containsKey(o)) {
                data.remove(o);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        if (collection == this) {
            return false;
        }
        boolean result = false;
        for (Iterator it = data.keySet().iterator(); it.hasNext(); ) {
            Object obj = it.next();
            if (!collection.contains(obj)) {
                result = true;
                size = size - data.get(obj).size();
                it.remove();
            }
        }
        if (result) {
            modCount++;
        }
        return result;
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

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    E removeAndGet(Object o) {
        List<E> group = data.get(o);
        if (group == null) {
            return null;
        }
        size--;
        modCount++;
        E result = group.remove(group.size() - 1);
        if (group.isEmpty()) {
            data.remove(o);
        }
        return result;
    }

    boolean removeExactly(E what) {
        List<E> group = data.get(what);
        if (group != null) {
            for (Iterator<E> it = group.iterator(); it.hasNext(); ) {
                E cur = it.next();
                if (cur == what) {
                    it.remove();
                    if (group.isEmpty()) {
                        data.remove(what);
                    }
                    size--;
                    modCount++;
                    return true;
                }
            }
        }
        return false;
    }

    int getModCount() {
        return modCount;
    }

    private class BagIterator implements Iterator<E> {
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
                data.remove(last);
            }
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
