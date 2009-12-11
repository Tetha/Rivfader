package edu.rivfader.test.commands;

import edu.rivfader.commands.UpdateTable;
import edu.rivfader.data.Database;
import edu.rivfader.data.Row;
import edu.rivfader.relalg.rowselector.IRowSelector;
import edu.rivfader.relalg.QualifiedNameRow;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

import java.io.Writer;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Database.class)
public class UpdateTableTest {
    @Test
    public void updateExecution() throws IOException {
        String tablename = "table";
        Writer writer = createMock(Writer.class);
        List<String> columnNames = new LinkedList<String>();
        columnNames.add("chicken");
        columnNames.add("cow");
        IRowSelector selectedRows = createMock(IRowSelector.class);
        Database database = createMock(Database.class);
        Map<String, String> assignments = new HashMap<String, String>();
        assignments.put("chicken", "chickens");

        List<Row> rows = new LinkedList<Row>();
        Row modifiedRow = new Row(columnNames);
        modifiedRow.setData("chicken", "egg");
        modifiedRow.setData("cow", "milk");
        rows.add(modifiedRow);
        Row unmodifiedRow = new Row(columnNames);
        unmodifiedRow.setData("chicken", "mud");
        unmodifiedRow.setData("cow", "cows");
        rows.add(unmodifiedRow);

        database.openTableForWriting(tablename);
        expect(database.loadTable(tablename)).andReturn(rows.iterator());
        expect(selectedRows
                .acceptsRow(QualifiedNameRow.fromRow(tablename, modifiedRow)))
            .andReturn(true);
        expect(selectedRows
                .acceptsRow(QualifiedNameRow.fromRow(tablename, unmodifiedRow)))
            .andReturn(false);
        Row modifiedRowResult = new Row(columnNames);
        modifiedRowResult.setData("chicken", "chickens");
        modifiedRowResult.setData("cow", "milk");
        database.storeRow(tablename, modifiedRowResult);

        Row unmodifiedRowResult = new Row(columnNames);
        unmodifiedRowResult.setData("chicken", "mud");
        unmodifiedRowResult.setData("cow", "cows");
        database.storeRow(tablename, unmodifiedRowResult);
        database.closeTable(tablename);

        replayAll();
        UpdateTable subject = new UpdateTable(tablename,
                                              assignments,
                                              selectedRows);
        subject.execute(database, writer);
        verifyAll();
    }
}
