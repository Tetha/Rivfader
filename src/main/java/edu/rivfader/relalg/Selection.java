package edu.rivfader.relalg;

import edu.rivfader.data.Row;
import edu.rivfader.relalg.rowselector.RowSelector;

import java.util.Iterator;
import java.util.NoSuchElementException;

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
            if (next == null) {
                while (source.hasNext()) {
                    next = source.next();
                    if (predicate.acceptsRow(next)) {
                        break;
                    }
                }
            }
            return next != null;
        }

        @Override
        public Row next() {
            if (hasNext()) {
                try {
                    return next;
                } finally {
                    next = null;
                }
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
