package edu.rivfader.test.relalg.rowselector;

import edu.rivfader.data.Row;
import edu.rivfader.relalg.rowselector.Comparision;
import edu.rivfader.relalg.rowselector.ValueComparer;
import edu.rivfader.relalg.rowselector.ValueProvider;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

import static org.easymock.EasyMock.expect;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Row.class,ValueProvider.class})
public class ComparisionTest {
    private ValueComparer internal;
    private Row parameterRow;

    @Before
    public void createValueComparer() {
        internal = createMock(ValueComparer.class);
    }

    @Before
    public void createRow() {
        parameterRow = createMock(Row.class);
    }

    private void expectGoodValuePair(String firstData, String secondData,
                                     boolean expectedResult) {
        expect(internal.isGoodValuePair(firstData, secondData))
                .andReturn(expectedResult);
    }

    private void expectGetValue(ValueProvider mock, String resultValue) {
        expect(mock.getValue(parameterRow)).andReturn(resultValue);
    }

    @After
    public void verify() {
        verifyAll();
    }

    @Test
    public void literalLiteralCompare() {
        String firstValue = "a";
        String secondValue = "b";

        ValueProvider firstValueProvider = createMock(ValueProvider.class);
        ValueProvider secondValueProvider = createMock(ValueProvider.class);
        expectGetValue(firstValueProvider, firstValue);
        expectGetValue(secondValueProvider, secondValue);
        expectGoodValuePair(firstValue, secondValue, true);
        replayAll();
        Comparision subject = new Comparision(firstValueProvider,
                                              secondValueProvider,
                                              internal);
        assertTrue(subject.acceptsRow(parameterRow));
    }
}
