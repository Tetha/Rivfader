package edu.rivfader.relalg;

import edu.rivfader.data.Database;
import edu.rivfader.data.Row;

import java.util.Iterator;
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
        return null;
    }

    @Override
    public Iterator<IQualifiedNameRow> transformSelection(Selection s){
        return null;
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
}
