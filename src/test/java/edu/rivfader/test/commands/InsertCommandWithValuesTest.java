package edu.rivfader.test.commands;

import edu.rivfader.commands.InsertCommandWithValues;
import edu.rivfader.data.Database;
import edu.rivfader.data.Row;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.util.Map;
import java.util.HashMap;

import java.io.Writer;
import java.io.IOException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Database.class)
public class InsertCommandWithValuesTest {
    @Test
    public void valuesAreInserted() throws IOException {
        String tableName = "table";
        Writer writer = createMock(Writer.class);
        Database database = createMock(Database.class);

        Map<String, String> values = new HashMap<String, String>();
        values.put("cow", "milk");
        values.put("chicken", "egg");

        Row valueRow = new Row("chicken", "cow");
        valueRow.setData("cow", "milk");
        valueRow.setData("chicken", "egg");

        database.openTableForWriting(tableName);
        database.appendRow(tableName, valueRow);
        database.closeTable(tableName);
        replayAll();
        InsertCommandWithValues subject =
            new InsertCommandWithValues(tableName, values);
        subject.execute(database, writer);
        verifyAll();
    }
}
