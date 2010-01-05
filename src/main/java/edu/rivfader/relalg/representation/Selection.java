package edu.rivfader.relalg.representation;

import edu.rivfader.data.Database;
import edu.rivfader.rowselector.representation.IRowSelector;

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
}
