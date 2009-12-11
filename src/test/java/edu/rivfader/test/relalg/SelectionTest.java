package edu.rivfader.test.relalg;

import edu.rivfader.data.Database;
import edu.rivfader.relalg.QualifiedNameRow;
import edu.rivfader.relalg.IQualifiedNameRow;
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
@PrepareForTest(Database.class)
public class SelectionTest {
    @Test
    public void onlyGoodRowsPass() {
        Database database = createMock(Database.class);
        IQualifiedNameRow first = createMock(IQualifiedNameRow.class);
        IQualifiedNameRow second = createMock(IQualifiedNameRow.class);
        IQualifiedNameRow third = createMock(IQualifiedNameRow.class);

        IRowSelector predicate = createMock(IRowSelector.class);
        expect(predicate.acceptsRow(first)).andReturn(true);
        expect(predicate.acceptsRow(second)).andReturn(false);
        expect(predicate.acceptsRow(third)).andReturn(true);

        List<IQualifiedNameRow> previousRows =
            new LinkedList<IQualifiedNameRow>();
        previousRows.add(first);
        previousRows.add(second);
        previousRows.add(third);
        IRelAlgExpr subExpression = createMock(IRelAlgExpr.class);
        expect(subExpression.evaluate(database))
            .andReturn(previousRows.iterator())
            .anyTimes();

        replayAll();
        Selection subject = new Selection(predicate, subExpression);
        Iterator<IQualifiedNameRow> selectedRows = subject.evaluate(database);
        List<IQualifiedNameRow> gotRows = new LinkedList<IQualifiedNameRow>();
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
        List<IQualifiedNameRow> expectedRows =
            new LinkedList<IQualifiedNameRow>();
        expectedRows.add(first);
        expectedRows.add(third);

        assertEquals(expectedRows, gotRows);
        verifyAll();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeIsNotSupported() {
        Database database = createMock(Database.class);
        IRelAlgExpr subExpression = createMock(IRelAlgExpr.class);
        expect(subExpression.evaluate(database)).andReturn(null);
        IRowSelector predicate = createMock(IRowSelector.class);
        replayAll();
        Selection subject = new Selection(predicate, subExpression);
        try {
            subject.evaluate(database).remove();
        } finally {
            verifyAll();
        }
    }
}
