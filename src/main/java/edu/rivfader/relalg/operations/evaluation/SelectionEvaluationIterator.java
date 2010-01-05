package edu.rivfader.relalg.operation.evaluation;

import edu.rivfader.relalg.representation.IQualifiedNameRow;
import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.rowselector.representation.IRowSelector;
import edu.rivfader.rowselector.operations.AcceptsRowEvaluator;
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
                             final Evaluator e) {
        predicate = evaluatedNode.getPredicate();
        source = e.transform(evaluatedNode.getSubExpression());
    }

    @Override
    public boolean hasNext() {
        if (!nextIsValid) {
            while (source.hasNext()) {
                IQualifiedNameRow pne = source.next();
                AcceptsRowEvaluator eval = new AcceptsRowEvaluator(pne);
                if (eval.evaluate(predicate)) {
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
