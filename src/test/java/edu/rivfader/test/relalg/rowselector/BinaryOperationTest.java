package edu.rivfader.test.relalg.rowselector;

import edu.rivfader.data.Row;
import edu.rivfader.relalg.rowselector.BinaryOperation;
import edu.rivfader.relalg.rowselector.IRowSelector;
import edu.rivfader.relalg.rowselector.BooleanValueCombination;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

import static org.easymock.EasyMock.expect;

import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Row.class,BooleanValueCombination.class})
public class BinaryOperationTest {
    @Test public void testValueCombination() {
        IRowSelector left = createMock(IRowSelector.class);
        IRowSelector right = createMock(IRowSelector.class);
        BooleanValueCombination combination =
            createMock(BooleanValueCombination.class);
        boolean leftResult = true;
        boolean rightResult = false;
        boolean combinationResult = true;
        Row parameterRow = createMock(Row.class);

        expect(left.acceptsRow(parameterRow)).andReturn(leftResult);
        expect(right.acceptsRow(parameterRow)).andReturn(rightResult);
        expect(combination.combineValues(leftResult, rightResult))
            .andReturn(combinationResult);

        replayAll();
        BinaryOperation subject = new BinaryOperation(left, combination, right);
        assertEquals(combinationResult, subject.acceptsRow(parameterRow));
        verifyAll();
    }
}
