package edu.rivfader.test.rowselector.operations;

import edu.rivfader.rowselector.representation.IRowSelector;
import edu.rivfader.rowselector.representation.BinaryOperation;
import edu.rivfader.rowselector.representation.BooleanValueCombination;
import edu.rivfader.rowselector.representation.IValueProvider;
import edu.rivfader.rowselector.representation.ValueComparer;
import edu.rivfader.rowselector.representation.Comparision;
import edu.rivfader.rowselector.representation.Always;
import edu.rivfader.rowselector.operations.ConjunctionSplitter;
import edu.rivfader.rowselector.operations.RowselectorStubResult;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import org.powermock.modules.junit4.PowerMockRunner;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

@RunWith(PowerMockRunner.class)
public class ConjunctionSplitterTest {
    @Test public void binaryOperationAndSplit() {
        Collection<IRowSelector> leftResult = new LinkedList<IRowSelector>();
        Collection<IRowSelector> rightResult = new LinkedList<IRowSelector>();
        leftResult.add(createMock(IRowSelector.class));
        rightResult.add(createMock(IRowSelector.class));
        IRowSelector left =
            new RowselectorStubResult<Collection<IRowSelector>>(leftResult);
        IRowSelector right =
            new RowselectorStubResult<Collection<IRowSelector>>(rightResult);
        IRowSelector input =
            new BinaryOperation(left, BooleanValueCombination.AND, right);
        replayAll();
        ConjunctionSplitter subject = new ConjunctionSplitter();
        Collection<IRowSelector> output = subject.transform(input);
        verifyAll();
        assertThat(output.containsAll(leftResult), is(true));
        assertThat(output.containsAll(rightResult), is(true));
    }

    @Test public void binaryOperationOrCollected() {
        IRowSelector left = createMock(IRowSelector.class);
        IRowSelector right = createMock(IRowSelector.class);
        IRowSelector input = new BinaryOperation(
                        left, BooleanValueCombination.OR, right);
        replayAll();
        ConjunctionSplitter subject = new ConjunctionSplitter();
        Collection<IRowSelector> output = subject.transform(input);
        verifyAll();
        assertThat(output, hasItem(input));
        assertThat(output.size(), is(1));
    }

    @Test public void comparisionCollected() {
        IValueProvider leftValueProvider = createMock(IValueProvider.class);
        IValueProvider rightValueProvider = createMock(IValueProvider.class);

        IRowSelector input = new Comparision(leftValueProvider,
                                             rightValueProvider,
                                             ValueComparer.EQUALS);
        replayAll();
        ConjunctionSplitter subject = new ConjunctionSplitter();
        Collection<IRowSelector> output = subject.transform(input);
        verifyAll();
        assertThat(output, hasItem(input));
        assertThat(output.size(), is(1));
    }

    @Test public void alwaysCollected() {
        IRowSelector input = new Always();

        replayAll();
        ConjunctionSplitter subject = new ConjunctionSplitter();
        Collection<IRowSelector> output = subject.transform(input);
        verifyAll();
        assertThat(output, hasItem(input));
        assertThat(output.size(), is(1));
    }
}
