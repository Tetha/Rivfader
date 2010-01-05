package edu.rivfader.test.relalg.operations.profiling_evaluation;

import edu.rivfader.relalg.operations.profiling_evaluation.ICountingIterator;
import edu.rivfader.relalg.operations.profiling_evaluation.ICostAccumulator;
import edu.rivfader.relalg.operations.profiling_evaluation.SelectionStatisticsIterator;
import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.relalg.representation.IQualifiedNameRow;
import edu.rivfader.relalg.operations.RowFactory;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.hasItem;

import org.powermock.modules.junit4.PowerMockRunner;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import org.powermock.core.classloader.annotations.PrepareForTest;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Selection.class)
public class SelectionStatisticsIteratorTest {
    private interface RowCounter extends ICountingIterator<IQualifiedNameRow> {
    }

    @Test public void evaluationWorks() {
        ICostAccumulator statisticsDestination;
        Iterator<IQualifiedNameRow> output;
        SelectionStatisticsIterator subject;
        Selection activeNode;

        RowFactory selectedRows = new RowFactory(new String[] {"t", "c"});
        selectedRows.addRow("a");
        selectedRows.addRow("b");

        activeNode = createMock(Selection.class);
        statisticsDestination = createMock(ICostAccumulator.class);
        statisticsDestination.handleSelectionStatistics(activeNode,
                                                        selectedRows.getRows().size(),
                                                        1);
        output = selectedRows.getRows().iterator();
        replayAll();
        subject = new SelectionStatisticsIterator(statisticsDestination,
                                                  activeNode, output);
        List<IQualifiedNameRow> result = new LinkedList<IQualifiedNameRow>();
        while(subject.hasNext()) {
            result.add(subject.next());
        }
        for(IQualifiedNameRow outputRow : selectedRows.getRows()) {
            assertThat(result, hasItem(outputRow));
        }
        verifyAll();
    }
}
