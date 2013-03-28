package ru.ifmo.ctddev.bobrov.task3.bag;

import java.util.*;

public class Bag extends AbstractBag {
    private Map<Object, List<Object>> data;
    private int modCount = 0;
    private int size = 0;


    public Bag() {
        data = new HashMap<>();
    }

    public Bag(Collection collection) {
        this();
        addAll(collection);
    }

    @Override
    public boolean contains(Object o) {
        return data.containsKey(o);
    }

    @Override
    public Iterator iterator() {
        return new BagIterator();
    }

    @Override
    public boolean add(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        modCount++;
        if (size != Integer.MAX_VALUE) {
            size++;
        }
        if (!contains(o)) {
            data.put(o, new ArrayList<Object>() {{
                add(0, o);
            }});
            return true;
        }
        List<Object> group = data.get(o);
        return group.add(o);
    }

    @Override
    public boolean remove(Object o) {
        if (o == null) {
            throw new NullPointerException();
        }
        return removeAndGet(o) != null;
    }

    @Override
    public boolean removeAll(Collection collection) {
        boolean result = false;
        if (collection == this) {
            result = !isEmpty();
            clear();
            return result;
        }

        for (Object obj : collection) {
            List group = data.get(obj);
            if (group != null) {
                result = true;
                size -= group.size();
                data.remove(obj);
            }
        }
        if (result) {
            modCount++;
        }
        return result;
    }

    @Override
    public boolean retainAll(Collection collection) {
        if (collection == this) {
            return false;
        }
        boolean result = false;
        for (Iterator it = data.keySet().iterator(); it.hasNext(); ) {
            Object obj = it.next();
            if (!collection.contains(obj)) {
                result = true;
                size -= data.get(obj).size();
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
        return size;
    }

    Object removeAndGet(Object o) {
        List<Object> group = data.get(o);
        if (group == null) {
            return null;
        }
        size--;
        modCount++;
        Object result = group.remove(group.size() - 1);
        if (group.isEmpty()) {
            data.remove(o);
        }
        return result;
    }

    boolean removeExactly(Object o) {
        List group = data.get(o);
        if (group == null) {
            return false;
        }
        for (Iterator it = group.iterator(); it.hasNext(); ) {
            Object cur = it.next();
            if (cur == o) {
                it.remove();
                if (group.isEmpty()) {
                    data.remove(o);
                }
                size--;
                modCount++;
                return true;
            }
        }
        return false;
    }

    int getModCount() {
        return modCount;
    }

    private class BagIterator implements Iterator {
        private int expectedModCount = modCount;

        private Iterator mapIterator;
        private Iterator groupIterator;
        private boolean canRemove = false;
        private Object last;

        public BagIterator() {
            mapIterator = data.keySet().iterator();
        }

        @Override
        public boolean hasNext() {
            checkModCount();
            return mapIterator.hasNext() || (groupIterator != null && groupIterator.hasNext());
        }

        @Override
        public Object next() {
            checkModCount();
            if (groupIterator == null || !groupIterator.hasNext()) {
                last = mapIterator.next();
                groupIterator = data.get(last).iterator();
            }
            Object result = groupIterator.next();
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
            List group = data.get(last);
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
