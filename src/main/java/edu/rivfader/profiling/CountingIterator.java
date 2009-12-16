package edu.rivfader.profiling;

import java.util.Iterator;

/**
 * implements a generic wrapper for iterators to be able to count
 * the number of elements they returned.
 * @author harald
 */
public class CountingIterator<E> implements ICountingIterator<E> {
    private Iterator<E> wrappedIterator;
    private int elementsReturned;
    /**
     * Constructs a new counting iterator.
     * @param wrappedIterator the iterator to count elements from
     */
    public CountingIterator(Iterator<E> pWrappedIterator) {
        wrappedIterator = pWrappedIterator;
        elementsReturned = 0;
    }

    @Override
    public boolean hasNext() {
        return wrappedIterator.hasNext();
    }

    @Override
    public E next() {
        elementsReturned++;
        return wrappedIterator.next();
    }

    @Override
    public void remove() {
        wrappedIterator.remove();
    }

    @Override
    public int getNumberOfElements() {
        return elementsReturned;
    }
}
