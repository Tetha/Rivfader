package edu.rivfader.test.relalg;

import edu.rivfader.data.Row;
import edu.rivfader.relalg.RelAlgExpr;
import edu.rivfader.relalg.Product;

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
@PrepareForTest(Row.class)
public class ProductTest {
    private interface RowIterator extends Iterator<Row> {};

    @Test
    public void evaluateWorks() {
        List<Row> leftResult = new LinkedList<Row>();
        List<String> leftColumnNames = new LinkedList<String>();
        leftColumnNames.add("cow");
        Row leftFirst = new Row(leftColumnNames);
        leftFirst.setData("cow", "milk");
        leftResult.add(leftFirst);
        Row leftSecond = new Row(leftColumnNames);
        leftSecond.setData("cow", "cows");
        leftResult.add(leftSecond);

        List<Row> rightResult = new LinkedList<Row>();
        List<String> rightColumnNames = new LinkedList<String>();
        rightColumnNames.add("chicken");
        Row rightFirst = new Row(rightColumnNames);
        rightFirst.setData("chicken", "eggs");
        rightResult.add(rightFirst);
        Row rightSecond = new Row(rightColumnNames);
        rightSecond.setData("chicken", "chickens");
        rightResult.add(rightSecond);

        RelAlgExpr left = createMock(RelAlgExpr.class);
        expect(left.evaluate()).andReturn(leftResult.iterator());
        expect(left.evaluate()).andReturn(leftResult.iterator()).times(0,1);
        RelAlgExpr right = createMock(RelAlgExpr.class);
        expect(right.evaluate()).andReturn(rightResult.iterator());
        expect(right.evaluate()).andReturn(rightResult.iterator()).times(0,1);

        replayAll();
        Product subject = new Product(left, right);
        Iterator<Row> resultRows = subject.evaluate();

        List<Row> gotRows = new LinkedList<Row>();
        while(resultRows.hasNext()) {
            gotRows.add(resultRows.next());
        }

        try {
            resultRows.next();
            fail();
        } catch(NoSuchElementException e) {
        }

        List<Row> expectedRows = new LinkedList<Row>();
        List<String> expectedColumnNames = new LinkedList<String>();
        expectedColumnNames.add("cow");
        expectedColumnNames.add("chicken");

        Row firstExpected = new Row(expectedColumnNames);
        firstExpected.setData("cow", "milk");
        firstExpected.setData("chicken", "eggs");
        expectedRows.add(firstExpected);

        Row secondExpected = new Row(expectedColumnNames);
        secondExpected.setData("cow", "milk");
        secondExpected.setData("chicken", "chickens");
        expectedRows.add(secondExpected);

        Row thirdExpected = new Row(expectedColumnNames);
        thirdExpected.setData("cow", "cows");
        thirdExpected.setData("chicken", "eggs");
        expectedRows.add(thirdExpected);

        Row fourthExpected = new Row(expectedColumnNames);
        fourthExpected.setData("cow", "cows");
        fourthExpected.setData("chicken", "chickens");
        expectedRows.add(fourthExpected);

        assertEquals(expectedRows, gotRows);
        verifyAll();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeIsUnsupported() {
        RelAlgExpr left = createMock(RelAlgExpr.class);
        RelAlgExpr right = createMock(RelAlgExpr.class);
        RowIterator leftIterator = createMock(RowIterator.class);
        RowIterator rightIterator = createMock(RowIterator.class);
        Row leftResult = createMock(Row.class);

        expect(left.evaluate()).andReturn(leftIterator);
        expect(right.evaluate()).andReturn(rightIterator);
        expect(leftIterator.next()).andReturn(leftResult);

        replayAll();
        new Product(left, right).evaluate().remove();
        verifyAll();
    }
}
