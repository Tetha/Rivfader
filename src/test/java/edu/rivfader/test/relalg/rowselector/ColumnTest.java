package edu.rivfader.test.relalg.rowselector;

import edu.rivfader.data.Row;
import edu.rivfader.relalg.rowselector.Column;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.easymock.EasyMock.expect;

import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Row.class)
public class ColumnTest {
    @Test public void columnValue () {
        String columnName = "c";
        String columnValue = "v";

        Row row = createMock(Row.class);
        expect(row.getData(columnName)).andReturn(columnValue);
        replayAll();
        Column subject = new Column(columnName);
        assertEquals(columnValue, subject.getValue(row));
        verifyAll();
    }
}
