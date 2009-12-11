package edu.rivfader.test.relalg;

import edu.rivfader.data.Database;
import edu.rivfader.data.Row;
import edu.rivfader.relalg.IRelAlgExpr;
import edu.rivfader.relalg.Product;
import edu.rivfader.relalg.QualifiedNameRow;
import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.QualifiedColumnName;
import edu.rivfader.relalg.IQualifiedColumnName;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.powermock.modules.junit4.PowerMockRunner;

import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.easymock.EasyMock.expect;

import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.NoSuchElementException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Row.class,Database.class})
public class ProductTest {
    private interface RowIterator extends Iterator<IQualifiedNameRow> {};

    @Test
    public void evaluateWorks() {
        Database database = createMock(Database.class);
        List<IQualifiedNameRow> leftResult =
            new LinkedList<IQualifiedNameRow>();
        IQualifiedColumnName ln = new QualifiedColumnName("t", "c");
        IQualifiedNameRow leftFirst = new QualifiedNameRow(ln);
        leftFirst.setData(ln, "cow");
        leftResult.add(leftFirst);
        QualifiedNameRow leftSecond = new QualifiedNameRow(ln);
        leftSecond.setData(ln, "more cow");
        leftResult.add(leftSecond);

        List<IQualifiedNameRow> rightResult =
            new LinkedList<IQualifiedNameRow>();
        IQualifiedColumnName rn = new QualifiedColumnName("u", "d");
        IQualifiedNameRow rightFirst = new QualifiedNameRow(rn);
        rightFirst.setData(rn, "chicken");
        rightResult.add(rightFirst);
        IQualifiedNameRow rightSecond = new QualifiedNameRow(rn);
        rightSecond.setData(rn, "more chicken");
        rightResult.add(rightSecond);

        IRelAlgExpr left = createMock(IRelAlgExpr.class);
        expect(left.evaluate(database)).andReturn(leftResult.iterator());
        expect(left.evaluate(database))
            .andReturn(leftResult.iterator())
            .times(0,1);

        IRelAlgExpr right = createMock(IRelAlgExpr.class);
        expect(right.evaluate(database)).andReturn(rightResult.iterator());
        expect(right.evaluate(database))
            .andReturn(rightResult.iterator())
            .times(0,1);

        replayAll();
        Product subject = new Product(left, right);
        Iterator<IQualifiedNameRow> resultRows = subject.evaluate(database);

        List<IQualifiedNameRow> gotRows = new LinkedList<IQualifiedNameRow>();
        while(resultRows.hasNext()) {
            gotRows.add(resultRows.next());
        }

        try {
            resultRows.next();
            fail();
        } catch(NoSuchElementException e) {
        }

        List<IQualifiedNameRow> expectedRows =
            new LinkedList<IQualifiedNameRow>();

        expectedRows.add(new QualifiedNameRow(leftFirst, rightFirst));
        expectedRows.add(new QualifiedNameRow(leftFirst, rightSecond));
        expectedRows.add(new QualifiedNameRow(leftSecond, rightFirst));
        expectedRows.add(new QualifiedNameRow(leftSecond, rightSecond));

        assertEquals(expectedRows, gotRows);
        verifyAll();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeIsUnsupported() {
        Database database = createMock(Database.class);
        IRelAlgExpr left = createMock(IRelAlgExpr.class);
        IRelAlgExpr right = createMock(IRelAlgExpr.class);
        RowIterator leftIterator = createMock(RowIterator.class);
        RowIterator rightIterator = createMock(RowIterator.class);
        IQualifiedNameRow leftResult = createMock(IQualifiedNameRow.class);

        expect(left.evaluate(database)).andReturn(leftIterator);
        expect(right.evaluate(database)).andReturn(rightIterator);
        expect(leftIterator.next()).andReturn(leftResult);

        replayAll();
        new Product(left, right).evaluate(database).remove();
        verifyAll();
    }
}
