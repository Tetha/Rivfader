package edu.rivfader.profiling;

import java.util.Iterator;
import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.Projection;
import edu.rivfader.profiling.ICostAccumulator;

public class ProjectionStatisticsIterator
        extends StatisticsIterator {
    private Projection activeNode;

    public ProjectionStatisticsIterator(ICostAccumulator statisticsDestination,
            Projection pActiveNode,
            Iterator<IQualifiedNameRow> wrappedIterator) {
        super(wrappedIterator, statisticsDestination);
        activeNode = pActiveNode;
    }

    protected void announceStatistics() {
        statisticsDestination.handleProjectionStatistics(activeNode,
                        wrappedIterator.getNumberOfElements(), columns);
    }
}
