package edu.rivfader.test.relalg.rowselector;

import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.rowselector.Always;
import edu.rivfader.relalg.rowselector.AcceptsRowEvaluator;
import edu.rivfader.relalg.rowselector.IRowSelector;
import edu.rivfader.relalg.rowselector.BooleanValueCombination;
import edu.rivfader.relalg.rowselector.BinaryOperation;
import edu.rivfader.relalg.rowselector.Comparision;
import edu.rivfader.relalg.rowselector.IValueProvider;
import edu.rivfader.relalg.rowselector.ValueComparer;
import edu.rivfader.relalg.rowselector.StubResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.easymock.EasyMock.expect;
import org.powermock.api.easymock.annotation.Mock;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import org.powermock.core.classloader.annotations.PrepareForTest;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ValueComparer.class)
public class AcceptsRowEvaluatorTest {
    @Mock private IQualifiedNameRow parameterRow;

    @Test
    public void alwaysEvaluation() {
        Always input = new Always();
        replayAll();
        AcceptsRowEvaluator subject = new AcceptsRowEvaluator(parameterRow);
        assertThat(subject.evaluate(input), is(true));
        verifyAll();
    }

    @Test public void binaryOperationEvaluation() {
        IRowSelector left = new StubResult(true);
        IRowSelector right = new StubResult(false);
        BooleanValueCombination combination =
            createMock(BooleanValueCombination.class);
        boolean leftResult = true;
        boolean rightResult = false;
        boolean combinationResult = true;

        expect(combination.combineValues(leftResult, rightResult))
            .andReturn(combinationResult);

        BinaryOperation input = new BinaryOperation(left, combination, right);
        replayAll();
        AcceptsRowEvaluator subject = new AcceptsRowEvaluator(parameterRow);
        assertThat(subject.evaluate(input), is(combinationResult));
        verifyAll();
    }

    @Test
    public void comparisionEvaluation() {
        ValueComparer internal = createMock(ValueComparer.class);
        String firstValue = "a";
        String secondValue = "b";

        IValueProvider firstIValueProvider = createMock(IValueProvider.class);
        IValueProvider secondIValueProvider = createMock(IValueProvider.class);
        expect(firstIValueProvider.getValue(parameterRow)).andReturn(firstValue);
        expect(secondIValueProvider.getValue(parameterRow)).andReturn(secondValue);
        expect(internal.isGoodValuePair(firstValue, secondValue)).andReturn(true);
        Comparision input = new Comparision(firstIValueProvider,
                                            secondIValueProvider,
                                            internal);
        replayAll();
        AcceptsRowEvaluator subject = new AcceptsRowEvaluator(parameterRow);
        assertThat(subject.evaluate(input), is(true));
        verifyAll();
    }
}
