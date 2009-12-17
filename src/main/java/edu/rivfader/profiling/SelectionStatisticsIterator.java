package edu.rivfader.profiling;

import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.Selection;
import java.util.Iterator;

public class SelectionStatisticsIterator extends StatisticsIterator {
    private Selection activeNode;
    private ICountingIterator<IQualifiedNameRow> ingoingRowCounter;

    public SelectionStatisticsIterator(Selection pActiveNode,
            Iterator<IQualifiedNameRow> wrappedIterator,
            ICostAccumulator statisticsDestination,
            ICountingIterator<IQualifiedNameRow> inputCounter) {
        super(wrappedIterator, statisticsDestination);
        ingoingRowCounter = inputCounter;
        activeNode = pActiveNode;
    }

    protected void announceStatistics() {
        statisticsDestination.handleSelectionStatistics(activeNode,
                ingoingRowCounter.getNumberOfElements(),
                wrappedIterator.getNumberOfElements(),
                columns);
    }
}
