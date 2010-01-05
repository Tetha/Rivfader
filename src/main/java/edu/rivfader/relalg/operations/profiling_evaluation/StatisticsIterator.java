package edu.rivfader.relalg.operations.profiling_evaluation;

import java.util.Iterator;
import edu.rivfader.relalg.representation.IQualifiedNameRow;

public abstract class StatisticsIterator
    implements Iterator<IQualifiedNameRow> {
    protected int columns;
    protected ICountingIterator<IQualifiedNameRow> wrappedIterator;
    protected ICostAccumulator statisticsDestination;

    public StatisticsIterator(Iterator<IQualifiedNameRow> pWrappedIterator,
                              ICostAccumulator pStatisticsDestination) {
        columns = -1;
        wrappedIterator = new CountingIterator<IQualifiedNameRow>(
                                    pWrappedIterator);
        statisticsDestination = pStatisticsDestination;
    }

    abstract void announceStatistics();

    @Override public boolean hasNext() {
        boolean result = wrappedIterator.hasNext();
        if (!result) {
            announceStatistics();
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
