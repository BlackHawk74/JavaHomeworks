package ru.ifmo.ctddev.bobrov.task3.bag;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.function.Predicate;

public class LinkedBag extends AbstractBag {
    private Bag data;
    private DataHolder head;

    public LinkedBag() {
        head = new DataHolder(null).makeSelfReference();
        data = new Bag();
    }

    public LinkedBag(Collection collection) {
        this();
        addAll(collection);
    }

    @Override
    public boolean contains(Object o) {
        return data.contains(new DataHolder(o));
    }

    @Override
    public Iterator iterator() {
        return new LinkedBagIterator();
    }

    @Override
    public boolean add(Object o) {
        return data.add(new DataHolder(o, head, head.getPrev()));
    }

    @Override
    public boolean remove(Object o) {
        DataHolder holder = (DataHolder) data.removeAndGet(new DataHolder(o));
        if (holder != null) {
            holder.removeReferences();
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection collection) {
        if (collection == this) {
            boolean result = !isEmpty();
            clear();
            return result;
        }
        return bulkRemoveImpl(collection::contains);
    }

    @Override
    public boolean retainAll(Collection collection) {
        if (collection == this) {
            return false;
        }
        return bulkRemoveImpl(o -> !collection.contains(o));
    }

    private boolean bulkRemoveImpl(Predicate predicate) {
        boolean result = false;
        for (Iterator it = iterator(); it.hasNext(); ) {
            if (predicate.test(it.next())) {
                it.remove();
                result = true;
            }
        }
        return result;
    }

    @Override
    public void clear() {
        data.clear();
        head.makeSelfReference();
    }

    @Override
    public int size() {
        return data.size();
    }

    private int getModCount() {
        return data.getModCount();
    }

    private static class DataHolder {
        private Object value;
        private DataHolder next;
        private DataHolder prev;

        public DataHolder(Object value) {
            this(value, null, null);
        }

        public DataHolder(Object value, DataHolder next, DataHolder prev) {
            this.value = value;
            this.next = next;
            this.prev = prev;

            if (next != null) {
                next.prev = this;
            }
            if (prev != null) {
                prev.next = this;
            }
        }

        private Object getValue() {
            return value;
        }

        private DataHolder getNext() {
            return next;
        }

        private DataHolder getPrev() {
            return prev;
        }

        public void removeReferences() {
            if (next != null) {
                next.prev = prev;
            }
            if (prev != null) {
                prev.next = next;
            }
        }

        public DataHolder makeSelfReference() {
            next = this;
            prev = this;
            return this;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof DataHolder && value.equals(((DataHolder) o).value);
        }
    }

    private class LinkedBagIterator implements Iterator {
        private DataHolder data = head;
        private int expectedModCount = getModCount();
        private boolean canRemove = false;

        @Override
        public boolean hasNext() {
            checkModCount();
            return data.getNext() != head;
        }

        @Override
        public Object next() {
            checkModCount();
            if (!hasNext()) {
                throw new IllegalStateException();
            }
            data = data.getNext();
            canRemove = true;
            return data.getValue();
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException("Remove called before next");
            }
            checkModCount();
            if (LinkedBag.this.data.removeExactly(data)) {
                data.removeReferences();
                expectedModCount = getModCount();
                canRemove = false;
            } else {
                assert false;
            }
        }

        private void checkModCount() {
            if (getModCount() != expectedModCount) {
                throw new ConcurrentModificationException("Iterator is not valid because collection is modified");
            }
        }
    }
}
