package edu.rivfader.test.relalg.rowselector;

import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.rowselector.Comparision;
import edu.rivfader.relalg.rowselector.ValueComparer;
import edu.rivfader.relalg.rowselector.IValueProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

import static org.easymock.EasyMock.expect;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.api.easymock.annotation.Mock;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

@RunWith(PowerMockRunner.class)
public class ComparisionTest {
    @Mock private ValueComparer internal;
    @Mock private IQualifiedNameRow parameterRow;

    private void expectGoodValuePair(String firstData, String secondData,
                                     boolean expectedResult) {
        expect(internal.isGoodValuePair(firstData, secondData))
                .andReturn(expectedResult);
    }

    private void expectGetValue(IValueProvider mock, String resultValue) {
        expect(mock.getValue(parameterRow)).andReturn(resultValue);
    }

    @Test
    public void literalLiteralCompare() {
        String firstValue = "a";
        String secondValue = "b";

        IValueProvider firstIValueProvider = createMock(IValueProvider.class);
        IValueProvider secondIValueProvider = createMock(IValueProvider.class);
        expectGetValue(firstIValueProvider, firstValue);
        expectGetValue(secondIValueProvider, secondValue);
        expectGoodValuePair(firstValue, secondValue, true);
        replayAll();
        Comparision subject = new Comparision(firstIValueProvider,
                                              secondIValueProvider,
                                              internal);
        assertTrue(subject.acceptsRow(parameterRow));
        verifyAll();
    }
}
