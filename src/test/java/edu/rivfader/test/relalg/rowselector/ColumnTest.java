package edu.rivfader.test.relalg.rowselector;

import edu.rivfader.data.Row;
import edu.rivfader.relalg.rowselector.Column;
import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.relalg.QualifiedColumnName;
import edu.rivfader.relalg.IQualifiedNameRow;

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
        IQualifiedColumnName rn = new QualifiedColumnName("t", "c");
        String columnValue = "v";

        IQualifiedNameRow row = createMock(IQualifiedNameRow.class);
        expect(row.resolveUnqualifiedName("c")).andReturn(rn);
        expect(row.getData(rn)).andReturn(columnValue);
        replayAll();
        Column subject = new Column(columnName);
        assertEquals(columnValue, subject.getValue(row));
        verifyAll();
    }
}
