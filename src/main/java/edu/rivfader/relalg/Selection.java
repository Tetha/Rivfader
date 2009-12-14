package edu.rivfader.relalg;

import edu.rivfader.data.Database;
import edu.rivfader.relalg.rowselector.IRowSelector;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This implements the selection of a row set.
 * @author harald
 */
public class Selection implements IRelAlgExpr {
    /**
     * contains the predicate to filter with.
     */
    private IRowSelector predicate;
    /**
     * contains the subexpression to filter.
     */
    private IRelAlgExpr subExpression;

    /**
     * constructs a new selection.
     * @param pPredicate the predicaet to filter on
     * @param pSubExpression the subExpression to filter
     */
    public Selection(final IRowSelector pPredicate,
                     final IRelAlgExpr pSubExpression) {
        predicate = pPredicate;
        subExpression = pSubExpression;
    }

    /**
     * returns the source expression of the selection.
     * @return the subexpression
     */
    public IRelAlgExpr getSubExpression() {
        return subExpression;
    }

    /**
     * returns the predicate selecting rows.
     * @return the row predicate.
     */
    public IRowSelector getPredicate() {
        return predicate;
    }

    @Override
    public Iterator<IQualifiedNameRow> evaluate(final Database context) {
        return new SelectionIterator(predicate,
                                     subExpression.evaluate(context));
    }

    /**
     * This Iterator filters a lazily implemented RowSet.
     * @author harald.
     */
    private static class SelectionIterator
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
        public SelectionIterator(final IRowSelector pPredicate,
                                 final Iterator<IQualifiedNameRow> pSource) {
            predicate = pPredicate;
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
}
