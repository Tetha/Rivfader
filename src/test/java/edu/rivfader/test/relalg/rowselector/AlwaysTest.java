package edu.rivfader.test.relalg.rowselector;

import edu.rivfader.data.Row;
import edu.rivfader.relalg.rowselector.Always;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertTrue;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
    Row.class
})
public class AlwaysTest {
    @Test
    public void acceptsRowIsAlwaysTrue() {
        Row rowMock = createMock(Row.class);
        replayAll();
        Always subject = new Always();
        assertTrue(subject.acceptsRow(rowMock));
        verifyAll();
    }
}
