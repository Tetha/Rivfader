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

@RunWith(PowerMockRunner.class)
@PrepareForTest(Database.class)
public class DropTableTest {
    @Test
    public void checkExecute() {
        String tablename = "table";
        Database database = createMock(Database.class);
        database.dropTable("table");
        replayAll();
        DropTable subject = new DropTable(tablename);
        subject.execute(database);
        verifyAll();
    }
}
