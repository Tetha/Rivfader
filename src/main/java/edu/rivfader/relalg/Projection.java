package edu.rivfader.relalg;

import edu.rivfader.data.Database;
import edu.rivfader.data.Row;

import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Iterator;

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
    private Set<IColumnProjection> selectedFields;

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
                      final Set<IColumnProjection> pSelectedFields) {
        subExpression = pSubExpression;
        selectedFields = pSelectedFields;
    }

    @Override
    public Iterator<IQualifiedNameRow> evaluate(final Database context) {
        return new ProjectionIterator(subExpression.evaluate(context));
    }

    /**
     * This iterator projects the column names in the row set.
     * @author harald.
     */
    private class ProjectionIterator implements Iterator<IQualifiedNameRow> {
        /**
         * contains the lazy row set to project.
         */
        private Iterator<IQualifiedNameRow> source;

        /**
         * cosntructs a new lazy row set from a lazy row set.
         * @param pSource the source row set to project
         */
        public ProjectionIterator(final Iterator<IQualifiedNameRow> pSource) {
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
