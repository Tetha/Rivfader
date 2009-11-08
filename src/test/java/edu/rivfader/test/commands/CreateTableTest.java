package edu.rivfader.test.commands;

import edu.rivfader.data.Database;
import edu.rivfader.commands.CreateTable;

import java.io.Writer;
import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.LinkedList;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Database.class)
public class CreateTableTest {
    @Test
    public void testCreationWorks() throws IOException {
        String tableName = "table";
        List<String> columns = new LinkedList<String>();
        columns.add("c1");
        columns.add("c2");
        columns.add("c3");
        Database database = createMock(Database.class);
        Writer writer = createMock(Writer.class);
        database.createTable(tableName, columns);
        replayAll();
        CreateTable subject = new CreateTable(tableName, columns);
        subject.execute(database, writer);
        verifyAll();
    }
}
