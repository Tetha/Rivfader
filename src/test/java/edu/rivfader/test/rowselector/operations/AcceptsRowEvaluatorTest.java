package edu.rivfader.test.rowselector.operations;

import edu.rivfader.relalg.representation.IQualifiedNameRow;
import edu.rivfader.rowselector.representation.Always;
import edu.rivfader.rowselector.operations.AcceptsRowEvaluator;
import edu.rivfader.rowselector.operations.RowselectorStubResult;
import edu.rivfader.rowselector.representation.IRowSelector;
import edu.rivfader.rowselector.representation.BooleanValueCombination;
import edu.rivfader.rowselector.representation.BinaryOperation;
import edu.rivfader.rowselector.representation.Comparision;
import edu.rivfader.rowselector.representation.IValueProvider;
import edu.rivfader.rowselector.representation.ValueComparer;
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
        assertThat(subject.transform(input), is(true));
        verifyAll();
    }

    @Test public void binaryOperationEvaluation() {
        IRowSelector left = new RowselectorStubResult<Boolean>(true);
        IRowSelector right = new RowselectorStubResult<Boolean>(false);
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
        assertThat(subject.transform(input), is(combinationResult));
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
        assertThat(subject.transform(input), is(true));
        verifyAll();
    }
}
