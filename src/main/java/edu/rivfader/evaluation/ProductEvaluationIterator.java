package edu.rivfader.evaluation;

import edu.rivfader.relalg.Product;
import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.QualifiedNameRow;
import edu.rivfader.relalg.IRelAlgExpr;
import edu.rivfader.relalg.IRelAlgExprTransformation;

import java.util.NoSuchElementException;
import java.util.Iterator;
/**
 * lazily computes the result of a product.
 */
public class ProductEvaluationIterator implements Iterator<IQualifiedNameRow> {
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
     * Contains an Evaluator to re-evaluate subexpressions.
     */
    private IRelAlgExprTransformation<Iterator<IQualifiedNameRow>>
        evaluator;
    /**
     * constructs a new product result iterator.
     * @param pLeft the left subexpression
     * @param pRight the right subexpression
     * @param pContext the database to evaluate the query in
     */
    public ProductEvaluationIterator(
            final Product p,
            IRelAlgExprTransformation<Iterator<IQualifiedNameRow>>
                pEvaluator) {
        left = p.getLeft();
        right = p.getRight();
        evaluator = pEvaluator;
        leftIterator = evaluator.transform(left);
        rightIterator = evaluator.transform(right);
        leftRow = leftIterator.next();
    }

    @Override
    public boolean hasNext() {
        return leftIterator.hasNext() || rightIterator.hasNext();
    }

    @Override
    public IQualifiedNameRow next() {
        if (!rightIterator.hasNext()) {
            if (leftIterator.hasNext()) {
                leftRow = leftIterator.next();
                rightIterator = evaluator.transform(right);
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
