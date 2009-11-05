package edu.rivfader.relalg;

import edu.rivfader.data.Row;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class implements the cartesian product of two relational algebra
 * expressions.
 * @author harald
 */
public class Product implements IRelAlgExpr {
    /**
     * contains the lhs.
     */
    private IRelAlgExpr left;
    /**
     * contains the rhs.
     */
    private IRelAlgExpr right;

    /**
     * Constructs a new product node.
     * @param pLeft the left subexpression
     * @param pRight the right subexpression
     */
    public Product(final IRelAlgExpr pLeft, final IRelAlgExpr pRight) {
        left = pLeft;
        right = pRight;
    }

    @Override
    public Iterator<Row> evaluate() {
        return new ProductResult(left, right);
    }

    /**
     * iterates over all rows in the result.
     */
    private static class ProductResult implements Iterator<Row> {
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
        private Iterator<Row> leftIterator;

        /**
         * contains an iterator over the rows in the right subexpression.
         */
        private Iterator<Row> rightIterator;

        /**
         * contains the current left row.
         */
        private Row leftRow;

        /**
         * constructs a new product result iterator.
         * @param pLeft the left subexpression
         * @param pRight the right subexpression
         */
        public ProductResult(final IRelAlgExpr pLeft,
                             final IRelAlgExpr pRight) {
            left = pLeft;
            right = pRight;
            this.leftIterator = left.evaluate();
            this.rightIterator = right.evaluate();
            leftRow = leftIterator.next();
        }

        @Override
        public boolean hasNext() {
            return rightIterator.hasNext() || leftIterator.hasNext();
        }

        @Override
        public Row next() {
            if (!rightIterator.hasNext()) {
                if (leftIterator.hasNext()) {
                    leftRow = leftIterator.next();
                    rightIterator = right.evaluate();
                } else {
                    throw new NoSuchElementException();
                }
            }
            return Row.combineRows(leftRow, rightIterator.next());
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
