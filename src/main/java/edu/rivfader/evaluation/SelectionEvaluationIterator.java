package edu.rivfader.evaluation;

import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.Selection;
import edu.rivfader.relalg.rowselector.IRowSelector;
import java.util.NoSuchElementException;
import java.util.Iterator;
/**
 * This Iterator filters a lazily implemented RowSet.
 * @author harald.
 */
public class SelectionEvaluationIterator
        implements Iterator<IQualifiedNameRow> {
    /**
     * contains the predicate to filter on.
     */
    private IRowSelector predicate;
    /**
     * Contains the iterator to filter.
     */
    private Iterator<IQualifiedNameRow> source;
    /**
     * contains the next row.
     */
    private IQualifiedNameRow nextElement;

    /**
     * contains if next is really the next element.
     */
    private boolean nextIsValid;

    /**
     * Constructs a new SelectionIterator.
     * @param pPredicate the predicate to filter on
     * @param pSource the iterator to filter
     */
    public SelectionEvaluationIterator(
                             final Selection evaluatedNode,
                             final Iterator<IQualifiedNameRow> pSource) {
        predicate = evaluatedNode.getPredicate();
        source = pSource;
    }

    @Override
    public boolean hasNext() {
        if (!nextIsValid) {
            while (source.hasNext()) {
                IQualifiedNameRow pne = source.next();
                if (predicate.acceptsRow(pne)) {
                    nextElement = pne;
                    nextIsValid = true;
                    break;
                }
            }
        }
        return nextIsValid;
    }

    @Override
    public IQualifiedNameRow next() {
        if (hasNext()) {
            nextIsValid = false;
            return nextElement;
        } else {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
