package edu.rivfader.test.evaluation;

import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.Selection;
import edu.rivfader.relalg.rowselector.IRowSelector;
import edu.rivfader.evaluation.SelectionEvaluationIterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.util.List;
import java.util.LinkedList;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Selection.class)
public class SelectionEvaluationIteratorTest {
    @Test
    public void transformSelection() {
        IQualifiedNameRow first = createMock(IQualifiedNameRow.class);
        IQualifiedNameRow second = createMock(IQualifiedNameRow.class);
        IQualifiedNameRow third = createMock(IQualifiedNameRow.class);

        IRowSelector predicate = createMock(IRowSelector.class);
        expect(predicate.acceptsRow(first)).andReturn(true);
        expect(predicate.acceptsRow(second)).andReturn(false);
        expect(predicate.acceptsRow(third)).andReturn(true);
        Selection s = new Selection(predicate, null);

        List<IQualifiedNameRow> previousRows =
            new LinkedList<IQualifiedNameRow>();
        previousRows.add(first);
        previousRows.add(second);
        previousRows.add(third);

        replayAll();
        SelectionEvaluationIterator subject = new SelectionEvaluationIterator(
                                        s, previousRows.iterator());
        List<IQualifiedNameRow> gotRows = new LinkedList<IQualifiedNameRow>();
        while(subject.hasNext()) {
            gotRows.add(subject.next());
        }
        verifyAll();

        List<IQualifiedNameRow> expectedRows =
            new LinkedList<IQualifiedNameRow>();
        expectedRows.add(first);
        expectedRows.add(third);
        assertThat(gotRows, is(equalTo(expectedRows)));
    }
}
