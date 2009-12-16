package edu.rivfader.profiling;

import java.util.Iterator;
import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.Projection;
import edu.rivfader.profiling.ICostAccumulator;

public class ProjectionStatisticsIterator
    implements Iterator<IQualifiedNameRow> {

    private CountingIterator<IQualifiedNameRow> wrappedIterator;
    private ICostAccumulator costDestination;
    private Projection activeNode;
    private int columns;

    public ProjectionStatisticsIterator(ICostAccumulator pCostDestination,
            Iterator<IQualifiedNameRow> pWrappedIterator,
            Projection pActiveNode) {
        costDestination = pCostDestination;
        wrappedIterator = new CountingIterator<IQualifiedNameRow>(pWrappedIterator);
        activeNode = pActiveNode;
        columns = -1;
    }

    @Override public boolean hasNext() {
        boolean result = wrappedIterator.hasNext();
        if (!result) {
            costDestination.handleProjectionStatistics(activeNode,
                            wrappedIterator.getNumberOfElements(), columns);
        }
        return result;
    }

    @Override public IQualifiedNameRow next() {
        IQualifiedNameRow result = wrappedIterator.next();
        columns = result.columns().size();
        return result;
    }

    @Override public void remove() {
        wrappedIterator.remove();
    }
}
