package edu.rivfader.relalg;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

/**
 * This iterator projects the column names in the row set.
 * @author harald.
 */
public class ProjectionEvaluationIterator
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
    public ProjectionEvaluationIterator(
                    final Iterator<IQualifiedNameRow> pSource,
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
