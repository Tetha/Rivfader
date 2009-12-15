package edu.rivfader.relalg;

import edu.rivfader.data.Database;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Collection;

/**
 * This class implements the projection in relational algebra.
 *
 * There is a set of fields which is just selected (as in:
 * they just appear after the SELECT without an AS after them),
 * and there is a mapping for all fields which are mapped
 * (as in: they are followed by an AS bar).
 * @author harald
 */
public class Projection implements IRelAlgExpr {
    /**
     * contains the fields which are also renamed.
     */
    private Collection<IColumnProjection> selectedFields;

    /**
     * ontains the projected subExpression.
     */
    private IRelAlgExpr subExpression;

    /**
     * constructs a new projection.
     * @param pSubExpression the subExpression to project
     * @param pSelectedFields fields to just pass through
     * @param pRenamedFields fields to rename
     */
    public Projection(final IRelAlgExpr pSubExpression,
                      final Collection<IColumnProjection> pSelectedFields) {
        subExpression = pSubExpression;
        selectedFields = pSelectedFields;
    }

    /**
     * returns the projected sub expression.
     * @return the projected sub expression
     */
    public IRelAlgExpr getSubExpression() {
        return subExpression;
    }

    /**
     * gets the column projection set.
     */
    public Collection<IColumnProjection> getSelectedFields() {
        return selectedFields;
    }
}
