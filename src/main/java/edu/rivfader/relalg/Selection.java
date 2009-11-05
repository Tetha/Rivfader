package edu.rivfader.relalg;

import edu.rivfader.data.Row;
import edu.rivfader.relalg.rowselector.RowSelector;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This implements the selection of a row set.
 * @author harald
 */
public class Selection implements RelAlgExpr {
    /**
     * contains the predicate to filter with.
     */
    private RowSelector predicate;
    /**
     * contains the subexpression to filter.
     */
    private RelAlgExpr subExpression;

    /**
     * constructs a new selection.
     * @param pPredicate the predicaet to filter on
     * @param pSubExpression the subExpression to filter
     */
    public Selection(final RowSelector pPredicate,
                     final RelAlgExpr pSubExpression) {
        predicate = pPredicate;
        subExpression = pSubExpression;
    }

    @Override
    public Iterator<Row> evaluate() {
        return new SelectionIterator(predicate, subExpression.evaluate());
    }

    private static class SelectionIterator implements Iterator<Row> {
        /**
         * contains the predicate to filter on.
         */
        private RowSelector predicate;
        /**
         * Contains the iterator to filter.
         */
        private Iterator<Row> source;
        /**
         * contains the next row.
         */
        private Row next;

        /**
         * contains if next is really the next element
         */
        private boolean nextIsValid;

        /**
         * Constructs a new SelectionIterator.
         * @param pPredicate the predicate to filter on
         * @param pSource the iterator to filter
         */
        public SelectionIterator(final RowSelector pPredicate,
                                 final Iterator<Row> pSource) {
            predicate = pPredicate;
            source = pSource;
        }

        @Override
        public boolean hasNext() {
            if (!nextIsValid) {
                while (source.hasNext()) {
                    next = source.next();
                    nextIsValid = true;
                    if (predicate.acceptsRow(next)) {
                        break;
                    }
                }
            }
            return nextIsValid;
        }

        @Override
        public Row next() {
            if (hasNext()) {
                nextIsValid = false;
                return next;
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
