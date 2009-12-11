package edu.rivfader.test.commands;

import edu.rivfader.commands.Delete;
import edu.rivfader.data.Database;
import edu.rivfader.data.Row;
import edu.rivfader.relalg.rowselector.IRowSelector;
import edu.rivfader.relalg.QualifiedNameRow;

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
        Row rowOne = new Row("c1", "c2");
        Row rowTwo = new Row("c1", "c2");
        rowOne.setData("c1", "d11");
        rowOne.setData("c2", "d12");
        rowTwo.setData("c1", "d21");
        rowTwo.setData("c2", "d22");

        List<Row> tableRows = new LinkedList<Row>();
        tableRows.add(rowOne);
        tableRows.add(rowTwo);
        databaseMock.openTableForWriting(tableName);
        expect(databaseMock.loadTable(tableName))
            .andReturn(tableRows.iterator());
        expect(predicate.acceptsRow(
                    QualifiedNameRow.fromRow(tableName, rowOne)))
            .andReturn(true);
        expect(predicate.acceptsRow(
                    QualifiedNameRow.fromRow(tableName, rowTwo)))
            .andReturn(false);
        databaseMock.storeRow(tableName, rowTwo);
        databaseMock.closeTable(tableName);
        replayAll();
        Delete subject = new Delete(tableName, predicate);
        subject.execute(databaseMock, outputMock);
        verifyAll();
    }
}
