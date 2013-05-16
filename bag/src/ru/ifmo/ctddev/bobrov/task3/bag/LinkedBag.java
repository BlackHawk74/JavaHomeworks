package ru.ifmo.ctddev.bobrov.task3.bag;

import java.util.*;
import java.util.function.Predicate;

public class LinkedBag<E> extends AbstractCollection<E> {
    private final Map<E, Set<Wrapper<E>>> data;
    private final Wrapper<E> head;
    private int modCount = 0;
    private long size = 0;

    public LinkedBag() {
        head = new Wrapper<E>(null).makeSelfReference();
        data = new HashMap<>();
    }

    public LinkedBag(Collection<? extends E> collection) {
        this();
        addAll(collection);
    }

    @Override
    public boolean contains(Object el) {
        return data.containsKey(el);
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedBagIterator();
    }

    @Override
    public boolean add(E el) {
        if (el == null) {
            throw new NullPointerException();
        }
        modCount++;
        size++;
        Set<Wrapper<E>> group;
        if (!contains(el)) {
            group = new HashSet<>();
            data.put(el, group);
        } else {
            group = data.get(el);
        }
        Wrapper<E> wrapper = new Wrapper<>(el, head, head.prev);
        return group.add(wrapper);
    }

    @Override
    public boolean remove(Object o) {
        Set<Wrapper<E>> group = data.get(o);
        if (group == null) {
            return false;
        }
        Iterator<Wrapper<E>> it = group.iterator();
        Wrapper<E> removed = it.next();
        it.remove();
        removed.removeReferences();
        if (group.isEmpty()) {
            data.remove(o);
        }
        size--;
        modCount++;
        return true;
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
    public void clear() {
        data.clear();
        size = 0;
        head.makeSelfReference();
    }

    @Override
    public int size() {
        return size > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) size;
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

    private final class LinkedBagIterator implements Iterator<E> {
        private Wrapper<E> position = head;
        private int expectedModCount = modCount;
        private boolean canRemove = false;

        @Override
        public boolean hasNext() {
            checkModCount();
            return position.next != head;
        }

        @Override
        public E next() {
            checkModCount();
            if (!hasNext()) {
                throw new IllegalStateException();
            }
            position = position.next;
            canRemove = true;
            return position.getValue();
        }

        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException("Remove called before next");
            }
            checkModCount();
            E value = position.value;
            Set<Wrapper<E>> group = data.get(value);
            assert group != null;

            group.remove(position);
            Wrapper<E> newPosition = position.prev;
            position.removeReferences();
            position = newPosition;
            if (group.isEmpty()) {
                data.remove(value);
            }
            size--;
            expectedModCount = ++modCount;
            canRemove = false;
        }

        private void checkModCount() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException("Iterator is not valid because collection is modified");
            }
        }
    }

    private static final class Wrapper<T> {
        private final T value;
        Wrapper<T> next;
        Wrapper<T> prev;

        public Wrapper(T value) {
            this(value, null, null);
        }

        public Wrapper(T value, Wrapper<T> next, Wrapper<T> prev) {
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

        public void removeReferences() {
            if (next != null) {
                next.prev = prev;
            }
            if (prev != null) {
                prev.next = next;
            }
        }

        public Wrapper<T> makeSelfReference() {
            next = this;
            prev = this;
            return this;
        }
    }
}
