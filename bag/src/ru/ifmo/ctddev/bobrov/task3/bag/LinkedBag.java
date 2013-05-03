package ru.ifmo.ctddev.bobrov.task3.bag;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.function.Predicate;

public class LinkedBag<E> extends AbstractCollection<E> {
    private final Bag<DataHolder<E>> data;
    private final DataHolder<E> head;

    public LinkedBag() {
        head = new DataHolder<E>(null).makeSelfReference();
        data = new Bag<>();
    }

    public LinkedBag(Collection<? extends E> collection) {
        this();
        addAll(collection);
    }

    @Override
    public boolean contains(Object el) {
        return data.contains(new DataHolder<>(el));
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedBagIterator();
    }

    @Override
    public boolean add(E el) {
        return data.add(new DataHolder<>(el, head, head.getPrev()));
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        if (collection == this) {
            boolean modified = !isEmpty();
            clear();
            return modified;
        }
        return filter(collection::contains);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return collection != this && filter(o -> !collection.contains(o));
    }

    @Override
    public boolean remove(Object o) {
        DataHolder<E> holder = data.removeAndGet(new DataHolder<>(o));
        if (holder != null) {
            holder.removeReferences();
            return true;
        }
        return false;
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

    private boolean filter(Predicate<Object> predicate) {
        boolean modified = false;
        for (Iterator <E> it = iterator(); it.hasNext();) {
            if (predicate.test(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    private int getModCount() {
        return data.getModCount();
    }

    private class LinkedBagIterator implements Iterator<E> {
        private DataHolder<E> position = head;
        private int expectedModCount = getModCount();
        private boolean canRemove = false;

        @Override
        public boolean hasNext() {
            checkModCount();
            return position.getNext() != head;
        }

        @Override
        public E next() {
            checkModCount();
            if (!hasNext()) {
                throw new IllegalStateException();
            }
            position = position.getNext();
            canRemove = true;
            return position.getValue();
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException("Remove called before next");
            }
            checkModCount();
            if (data.removeExactly(position)) {
                position.removeReferences();
                position = position.prev;
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

    private static final class DataHolder<T> {
        private final T value;
        private DataHolder<T> next;
        private DataHolder<T> prev;

        public DataHolder(T value) {
            this(value, null, null);
        }

        public DataHolder(T value, DataHolder<T> next, DataHolder<T> prev) {
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

        public T getValue() {
            return value;
        }

        public DataHolder<T> getNext() {
            return next;
        }

        public DataHolder<T> getPrev() {
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

        public DataHolder<T> makeSelfReference() {
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
            if (!(o instanceof DataHolder)) {
                return false;
            }
            DataHolder t = DataHolder.class.cast(o);
            return value.equals(t.value);
        }
    }
}
