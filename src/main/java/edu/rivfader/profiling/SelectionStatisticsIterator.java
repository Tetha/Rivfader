package edu.rivfader.profiling;

import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.Selection;
import java.util.Iterator;

public class SelectionStatisticsIterator extends StatisticsIterator {
    private Selection activeNode;
    private ICountingIterator<IQualifiedNameRow> ingoingRowCounter;

    public SelectionStatisticsIterator(ICostAccumulator pStatisticsDestination,
                                Selection pActiveNode,
                                Iterator<IQualifiedNameRow> wrappedIterator) {
        super(wrappedIterator, pStatisticsDestination);
        activeNode = pActiveNode;
    }

    protected void announceStatistics() {
        statisticsDestination.handleSelectionStatistics(activeNode,
                wrappedIterator.getNumberOfElements(),
                columns);
    }
}
