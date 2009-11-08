package edu.rivfader.test.commands;

import edu.rivfader.commands.InsertCommandWithoutValues;
import edu.rivfader.data.Database;
import edu.rivfader.data.Row;

import java.io.Writer;
import java.io.IOException;

import java.util.List;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Database.class)
public class InsertCommandWithoutValuesTest {
    @Test
    public void InsertCommandWithoutValuesExecution() throws IOException {
        Database database = createMock(Database.class);
        Writer writer = createMock(Writer.class);
        List<String> values = new LinkedList<String>();
        values.add("milk");
        values.add("eggs");

        String tableName = "table";

        List<String> columnList = new LinkedList<String>();
        columnList.add("cow");
        columnList.add("chicken");
        Row valueRow = new Row("cow", "chicken");
        valueRow.setData("cow", "milk");
        valueRow.setData("chicken", "eggs");
        database.openTableForWriting(tableName);
        expect(database.getColumnNames(tableName)).andReturn(columnList);
        database.storeRow(tableName, valueRow);
        database.closeTable(tableName);
        replayAll();
        InsertCommandWithoutValues subject =
            new InsertCommandWithoutValues(tableName, values);
        subject.execute(database, writer);
        verifyAll();
    }
}
