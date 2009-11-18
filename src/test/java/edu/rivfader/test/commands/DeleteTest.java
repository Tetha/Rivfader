package edu.rivfader.test.commands;

import edu.rivfader.commands.Delete;
import edu.rivfader.data.Database;
import edu.rivfader.data.Row;
import edu.rivfader.relalg.rowselector.IRowSelector;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.util.List;
import java.util.LinkedList;

import java.io.IOException;
import java.io.Writer;
@RunWith(PowerMockRunner.class)
@PrepareForTest({
    Database.class,
    Row.class
})
public class DeleteTest {
    @Test
    public void deleteTest() throws IOException {
        String tableName = "t";
        IRowSelector predicate = createMock(IRowSelector.class);
        Database databaseMock = createMock(Database.class);
        Writer outputMock = createMock(Writer.class);
        Row rowMockOne = createMock(Row.class);
        Row rowMockTwo = createMock(Row.class);

        List<Row> tableRows = new LinkedList<Row>();
        tableRows.add(rowMockOne);
        tableRows.add(rowMockTwo);
        databaseMock.openTableForWriting(tableName);
        expect(databaseMock.loadTable(tableName))
            .andReturn(tableRows.iterator());
        expect(predicate.acceptsRow(rowMockOne))
            .andReturn(true);
        expect(predicate.acceptsRow(rowMockTwo))
            .andReturn(false);
        databaseMock.storeRow(tableName, rowMockTwo);
        databaseMock.closeTable(tableName);
        replayAll();
        Delete subject = new Delete(tableName, predicate);
        subject.execute(databaseMock, outputMock);
        verifyAll();
    }
}
