package edu.rivfader.test.rowselector.operations;

import edu.rivfader.rowselector.operations.RequiredColumnsOfRowSelector;
import edu.rivfader.rowselector.operations.RowselectorStubResult;

import edu.rivfader.rowselector.representation.IRowSelector;
import edu.rivfader.rowselector.representation.Always;
import edu.rivfader.rowselector.representation.Comparision;
import edu.rivfader.rowselector.representation.BinaryOperation;
import edu.rivfader.rowselector.representation.BooleanValueCombination;
import edu.rivfader.rowselector.representation.ValueComparer;
import edu.rivfader.rowselector.representation.IValueProvider;

import edu.rivfader.relalg.representation.IQualifiedColumnName;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasItems;
import java.util.Collection;
import java.util.LinkedList;

@RunWith(PowerMockRunner.class)
public class RequiredColumnsOfRowSelectorTest {
    RequiredColumnsOfRowSelector subject;

    @Test public void transformAlways() {
        subject = new RequiredColumnsOfRowSelector();
        IRowSelector input = new Always();

        assertThat(subject.transform(input).size(), is(0));
    }

    @Test public void transformComparision() {
        IValueProvider left = createMock(IValueProvider.class);
        IValueProvider right = createMock(IValueProvider.class);

        Collection<IQualifiedColumnName> leftResult =
            new LinkedList<IQualifiedColumnName>();
        leftResult.add(createMock(IQualifiedColumnName.class));

        Collection<IQualifiedColumnName> rightResult =
            new LinkedList<IQualifiedColumnName>();
        rightResult.add(createMock(IQualifiedColumnName.class));

        expect(left.getRequiredColumns()).andReturn(leftResult);
        expect(right.getRequiredColumns()).andReturn(rightResult);

        ValueComparer compareOperation = ValueComparer.EQUALS;

        replayAll();
        IRowSelector input = new Comparision(left, right, compareOperation);
        subject = new RequiredColumnsOfRowSelector();
        Collection<IQualifiedColumnName> output = subject.transform(input);

        assertTrue(output.containsAll(leftResult));
        assertTrue(output.containsAll(rightResult));
        verifyAll();
    }

    @Test public void transformBinaryOperation() {
        Collection<IQualifiedColumnName> leftResult =
            new LinkedList<IQualifiedColumnName>();
        leftResult.add(createMock(IQualifiedColumnName.class));

        Collection<IQualifiedColumnName> rightResult =
            new LinkedList<IQualifiedColumnName>();
        rightResult.add(createMock(IQualifiedColumnName.class));

        IRowSelector left =
            new RowselectorStubResult<Collection<IQualifiedColumnName>>
                (leftResult);
        IRowSelector right =
            new RowselectorStubResult<Collection<IQualifiedColumnName>>
                (rightResult);

        replayAll();
        IRowSelector input = new BinaryOperation(left,
                                                 BooleanValueCombination.AND,
                                                 right);
        subject = new RequiredColumnsOfRowSelector();
        Collection<IQualifiedColumnName> output = subject.transform(input);

        assertTrue(output.containsAll(leftResult));
        assertTrue(output.containsAll(rightResult));
        verifyAll();
    }
}
