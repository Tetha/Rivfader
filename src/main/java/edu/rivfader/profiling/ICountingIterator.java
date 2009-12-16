package edu.rivfader.profiling;

import java.util.Iterator;

/**
 * Iterators implementing this interface count the number of elements they
 * yield.
 * @author harald
 */
public interface ICountingIterator<E> extends Iterator<E> {
    /**
     * returns the number of elements returned by next() so far.
     * @return the element count
     */
    public int getNumberOfElements();
}
