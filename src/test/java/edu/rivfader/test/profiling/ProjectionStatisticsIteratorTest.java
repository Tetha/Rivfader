package edu.rivfader.test.profiling;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.Projection;
import edu.rivfader.relalg.RowFactory;
import edu.rivfader.profiling.ICostAccumulator;
import edu.rivfader.profiling.ProjectionStatisticsIterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Projection.class)
public class ProjectionStatisticsIteratorTest {
    @Test
    public void testTriggering() {
        ICostAccumulator costDestination;
        List<IQualifiedNameRow> input;
        List<IQualifiedNameRow> output;
        Iterator<IQualifiedNameRow> subject;
        Projection activeNode;

        RowFactory inputRows = new RowFactory(new String[] {"t", "c"},
                                              new String[] {"t", "d"});
        inputRows.addRow("a", "b");
        inputRows.addRow("c", "d");
        input = inputRows.getRows();

        activeNode = createMock(Projection.class);
        costDestination = createMock(ICostAccumulator.class);
        costDestination.handleProjectionStatistics(activeNode, input.size(), 2);

        replayAll();
        subject = new ProjectionStatisticsIterator(costDestination,
                                                    activeNode,
                                                   input.iterator());
        output = new LinkedList<IQualifiedNameRow>();
        while(subject.hasNext()) {
            output.add(subject.next());
        }

        assertThat(output, is(equalTo(input)));
        verifyAll();
    }
}
