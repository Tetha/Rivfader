package edu.rivfader.profiling;

import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.Selection;
import java.util.Iterator;

public class SelectionStatisticsIterator implements Iterator<IQualifiedNameRow> {
    private int columns;
    private Selection activeNode;
    private ICostAccumulator statisticsDestination;
    private ICountingIterator<IQualifiedNameRow> ingoingRowCounter;
    private ICountingIterator<IQualifiedNameRow> outcomingRowCounter;

    public SelectionStatisticsIterator(Selection pActiveNode,
            Iterator<IQualifiedNameRow> selectedRows,
            ICostAccumulator pStatisticsDestination,
            ICountingIterator<IQualifiedNameRow> inputCounter) {
        ingoingRowCounter = inputCounter;
        outcomingRowCounter = new CountingIterator<IQualifiedNameRow>(selectedRows);
        statisticsDestination = pStatisticsDestination;
        activeNode = pActiveNode;
    }

    @Override public boolean hasNext() {
        boolean o = outcomingRowCounter.hasNext();
        if (!o) {
            statisticsDestination.handleSelectionStatistics(activeNode,
                        ingoingRowCounter.getNumberOfElements(),
                        outcomingRowCounter.getNumberOfElements(),
                        columns);
        }
        return o;
    }
    @Override public IQualifiedNameRow next() {
        IQualifiedNameRow o = outcomingRowCounter.next();
        columns = o.columns().size();
        return o;
    }

    @Override public void remove() {
        outcomingRowCounter.remove();
    }
}
