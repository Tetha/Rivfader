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

    @Override
    public Iterator<IQualifiedNameRow> evaluate(final Database context) {
        return new ProjectionIterator(subExpression.evaluate(context),
                                      selectedFields);
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
}
