package edu.rivfader.test.commands;

import edu.rivfader.commands.DropTable;
import edu.rivfader.data.Database;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import static org.powermock.api.easymock.PowerMock.replayAll;

import java.io.Writer;
import java.io.IOException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Database.class)
public class DropTableTest {
    @Test
    public void checkExecute() throws IOException {
        Writer writer = createMock(Writer.class);
        String tablename = "table";
        Writer output = createMock(Writer.class);
        Database database = createMock(Database.class);
        database.dropTable("table");
        replayAll();
        DropTable subject = new DropTable(tablename);
        subject.execute(database, writer);
        verifyAll();
    }
}
