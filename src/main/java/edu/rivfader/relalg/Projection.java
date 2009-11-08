package edu.rivfader.relalg;

import edu.rivfader.data.Database;
import edu.rivfader.data.Row;

import java.util.List;
import java.util.LinkedList;
import java.util.Set;
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
    private Map<String, String> renamedFields;

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
                      final Set<String> pSelectedFields,
                      final Map<String, String> pRenamedFields) {
        renamedFields = pRenamedFields;
        subExpression = pSubExpression;
        for (String sf : pSelectedFields) {
            renamedFields.put(sf, sf);
        }
    }

    @Override
    public Iterator<Row> evaluate(final Database context) {
        return new ProjectionIterator(subExpression.evaluate(context));
    }

    /**
     * This iterator projects the column names in the row set.
     * @author harald.
     */
    private class ProjectionIterator implements Iterator<Row> {
        /**
         * contains the lazy row set to project.
         */
        private Iterator<Row> source;

        /**
         * cosntructs a new lazy row set from a lazy row set.
         * @param pSource the source row set to project
         */
        public ProjectionIterator(final Iterator<Row> pSource) {
            source = pSource;
        }

        @Override
        public boolean hasNext() {
            return this.source.hasNext();
        }

        @Override
        public Row next() {
            Row input = source.next();
            List<String> resultColumns = new LinkedList<String>();
            for (String column : input.columns()) {
                if (renamedFields.containsKey(column)) {
                    resultColumns.add(renamedFields.get(column));
                }
            }
            Row result = new Row(resultColumns);
            for (String column : input.columns()) {
                if (renamedFields.containsKey(column)) {
                    result.setData(renamedFields.get(column),
                            input.getData(column));
                }
            }
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
