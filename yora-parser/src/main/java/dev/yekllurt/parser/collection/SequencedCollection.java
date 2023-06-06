package dev.yekllurt.parser.collection;

import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class SequencedCollection<E> implements Iterable<E> {

    private static final int INITIAL_CAPACITY = 10;

    private Object[] array;
    private int size;

    public SequencedCollection() {
        this.array = new Object[INITIAL_CAPACITY];
    }

    public void add(E e) {
        if (Objects.isNull(e)) {
            throw new IllegalArgumentException("Only non null elements can be added");
        }
        if (this.size == this.array.length) {
            resize();
        }
        array[size++] = e;
    }

    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return (E) array[index];
    }

    public int size() {
        return this.size;
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    public Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    private void resize() {
        Object[] newArray = new Object[array.length * 2];
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }

    class Itr implements Iterator<E> {

        private int cursorPosition;

        @Override
        public boolean hasNext() {
            return cursorPosition < size;
        }

        @Override
        public E next() {
            return (E) array[cursorPosition++];
        }

    }

}
