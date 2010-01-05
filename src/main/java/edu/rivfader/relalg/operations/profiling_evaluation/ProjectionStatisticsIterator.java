package edu.rivfader.relalg.operations.profiling_evaluation;

import java.util.Iterator;
import edu.rivfader.relalg.representation.IQualifiedNameRow;
import edu.rivfader.relalg.representation.Projection;
import edu.rivfader.relalg.operations.profiling_evaluation.ICostAccumulator;

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
