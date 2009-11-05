package edu.rivfader.test.relalg;

import edu.rivfader.data.Row;
import edu.rivfader.relalg.IRelAlgExpr;
import edu.rivfader.relalg.Selection;
import edu.rivfader.relalg.rowselector.IRowSelector;

import org.junit.runner.RunWith;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import static org.powermock.api.easymock.PowerMock.replayAll;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.NoSuchElementException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Row.class)
public class SelectionTest {
    @Test
    public void onlyGoodRowsPass() {
        Row first = createMock(Row.class);
        Row second = createMock(Row.class);
        Row third = createMock(Row.class);

        IRowSelector predicate = createMock(IRowSelector.class);
        expect(predicate.acceptsRow(first)).andReturn(true);
        expect(predicate.acceptsRow(second)).andReturn(false);
        expect(predicate.acceptsRow(third)).andReturn(true);

        List<Row> previousRows = new LinkedList<Row>();
        previousRows.add(first);
        previousRows.add(second);
        previousRows.add(third);
        IRelAlgExpr subExpression = createMock(IRelAlgExpr.class);
        expect(subExpression.evaluate())
            .andReturn(previousRows.iterator())
            .anyTimes();

        replayAll();
        Selection subject = new Selection(predicate, subExpression);
        Iterator<Row> selectedRows = subject.evaluate();
        List<Row> gotRows = new LinkedList<Row>();
        while(selectedRows.hasNext()) {
            gotRows.add(selectedRows.next());
        }

        try {
            assertFalse(selectedRows.hasNext());
            selectedRows.next();
            fail();
        } catch (NoSuchElementException e) {
            // all good
        }
        List<Row> expectedRows = new LinkedList<Row>();
        expectedRows.add(first);
        expectedRows.add(third);

        assertEquals(expectedRows, gotRows);
        verifyAll();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeIsNotSupported() {
        IRelAlgExpr subExpression = createMock(IRelAlgExpr.class);
        expect(subExpression.evaluate()).andReturn(null);
        IRowSelector predicate = createMock(IRowSelector.class);
        replayAll();
        Selection subject = new Selection(predicate, subExpression);
        try {
            subject.evaluate().remove();
        } finally {
            verifyAll();
        }
    }
}
