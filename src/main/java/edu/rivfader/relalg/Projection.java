package edu.rivfader.relalg;

import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.Map;
import java.util.Iterator;

import edu.rivfader.data.Row;

/**
 * This class implements the projection in relational algebra.
 *
 * There is a set of fields which is just selected (as in:
 * they just appear after the SELECT without an AS after them),
 * and there is a mapping for all fields which are mapped
 * (as in: they are followed by an AS bar).
 * @author harald
 */
public class Projection implements RelAlgExpr {
    /**
     * contains the fields which are also renamed.
     */
    private Map<String, String> renamedFields;

    /**
     * ontains the projected subExpression.
     */
    private RelAlgExpr subExpression;

    /**
     * constructs a new projection.
     * @param pSubExpression the subExpression to project
     * @param pSelectedFields fields to just pass through
     * @param pRenamedFields fields to rename
     */
    public Projection(final RelAlgExpr pSubExpression,
                      final Set<String> pSelectedFields,
                      final Map<String, String> pRenamedFields) {
        renamedFields = pRenamedFields;
        subExpression = pSubExpression;
        for (String sf : pSelectedFields) {
            renamedFields.put(sf, sf);
        }
    }

    @Override
    public Iterator<Row> evaluate() {
        return new ProjectionIterator(subExpression.evaluate());
    }

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
            Row n = source.next();
            List<String> rcn = new LinkedList<String>();
            for (String c : n.columns()) {
                if (renamedFields.containsKey(c)) {
                    rcn.add(renamedFields.get(c));
                }
            }
            Row r = new Row(rcn);
            for (String c : n.columns()) {
                if (renamedFields.containsKey(c)) {
                    r.setData(renamedFields.get(c), n.getData(c));
                }
            }
            return r;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
