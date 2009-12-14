package edu.rivfader.relalg;

import edu.rivfader.data.Database;
import edu.rivfader.data.Row;
import edu.rivfader.relalg.rowselector.IRowSelector;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.NoSuchElementException;

/**
 * evaluates a rel alg expression in a database into a lazily computed
 * set of rows.
 * @author harald
 */
public class Evaluator
    extends BaseRelalgTransformation<Iterator<IQualifiedNameRow>>
    implements IRelAlgExprTransformation<Iterator<IQualifiedNameRow>> {

    private Database context;

    public Evaluator(Database pContext) {
        context = pContext;
    }

    @Override
    public Iterator<IQualifiedNameRow> transformProduct(Product p){
        return new ProductResult(p.getLeft(), p.getRight(), context);
    }

    @Override
    public Iterator<IQualifiedNameRow> transformProjection(Projection p){
        return new ProjectionIterator(p.getSubExpression().evaluate(context),
                                      p.getSelectedFields());
    }

    @Override
    public Iterator<IQualifiedNameRow> transformSelection(Selection s){
        return new SelectionIterator(s.getPredicate(),
                                     s.getSubExpression().evaluate(context));
    }

    @Override
    public Iterator<IQualifiedNameRow> transformLoadTable(LoadTable l){
        return null;
    }

    @Override
    public Iterator<IQualifiedNameRow> transformRenameTable(RenameTable r){
        return null;
    }

    /**
     * lazily computes the result of a product.
     */
    private static class ProductResult implements Iterator<IQualifiedNameRow> {
        /**
         * contains the left subexpression.
         */
        private IRelAlgExpr left;

        /**
         * Contains the right subexpression.
         */
        private IRelAlgExpr right;

        /**
         * contains an iterator over the rows in the left subexpression.
         */
        private Iterator<IQualifiedNameRow> leftIterator;

        /**
         * contains an iterator over the rows in the right subexpression.
         */
        private Iterator<IQualifiedNameRow> rightIterator;

        /**
         * contains the current left row.
         */
        private IQualifiedNameRow leftRow;

        /**
         * contains the database to evaluate the query in.
         */
        private Database context;

        /**
         * constructs a new product result iterator.
         * @param pLeft the left subexpression
         * @param pRight the right subexpression
         * @param pContext the database to evaluate the query in
         */
        public ProductResult(final IRelAlgExpr pLeft,
                             final IRelAlgExpr pRight,
                             final Database pContext) {
            left = pLeft;
            right = pRight;
            context = pContext;
            leftIterator = left.evaluate(context);
            rightIterator = right.evaluate(context);
            leftRow = leftIterator.next();
        }

        @Override
        public boolean hasNext() {
            return rightIterator.hasNext() || leftIterator.hasNext();
        }

        @Override
        public IQualifiedNameRow next() {
            if (!rightIterator.hasNext()) {
                if (leftIterator.hasNext()) {
                    leftRow = leftIterator.next();
                    rightIterator = right.evaluate(context);
                } else {
                    throw new NoSuchElementException();
                }
            }
            return new QualifiedNameRow(leftRow, rightIterator.next());
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * This iterator projects the column names in the row set.
     * @author harald.
     */
    private static class ProjectionIterator
            implements Iterator<IQualifiedNameRow> {
        /**
         * contains the lazy row set to project.
         */
        private Iterator<IQualifiedNameRow> source;

        /**
         * contains the set of column projections happening.
         */
        private Collection<IColumnProjection> selectedFields;

        /**
         * cosntructs a new lazy row set from a lazy row set.
         * @param pSource the source row set to project
         */
        public ProjectionIterator(final Iterator<IQualifiedNameRow> pSource,
                        final Collection<IColumnProjection> pSelectedFields) {
            selectedFields = pSelectedFields;
            source = pSource;
        }

        @Override
        public boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public IQualifiedNameRow next() {
            IQualifiedNameRow i; // input
            IQualifiedNameRow o; // output
            Set<IQualifiedColumnName> pcns; // projected column names.

            pcns = new HashSet<IQualifiedColumnName>();

            i = source.next();
            for(IColumnProjection cp : selectedFields) {
                pcns.addAll(cp.project(i));
            }

            o = new QualifiedNameRow(pcns);

            for(IQualifiedColumnName scn : pcns) {
                o.setData(scn, i.getData(scn));
            }
            return o;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
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
