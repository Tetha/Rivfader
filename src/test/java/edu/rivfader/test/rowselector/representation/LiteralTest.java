package edu.rivfader.test.rowselector.representation;

import edu.rivfader.rowselector.representation.Literal;
import edu.rivfader.relalg.representation.IQualifiedNameRow;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

@RunWith(PowerMockRunner.class)
public class LiteralTest {
    @Test public void literalReturns() {
        String literalValue = "a";
        IQualifiedNameRow parameterRow = createMock(IQualifiedNameRow.class);
        replayAll();
        Literal subject = new Literal(literalValue);
        assertEquals(literalValue, subject.getValue(parameterRow));
        verifyAll();
    }
}
