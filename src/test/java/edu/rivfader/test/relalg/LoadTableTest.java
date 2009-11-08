package edu.rivfader.test.relalg;

import edu.rivfader.data.Database;
import edu.rivfader.data.Row;
import edu.rivfader.relalg.LoadTable;

import static org.easymock.EasyMock.expect;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.util.Iterator;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Database.class)
public class LoadTableTest {
    private interface RowIterator extends Iterator<Row> {};

    @Test
    public void checkEquals() {
        String tablename=  "table";
        Database database = createMock(Database.class);
        Iterator<Row> iteratorMock = createMock(RowIterator.class);
        expect(database.loadTable(tablename)).andReturn(iteratorMock);
        replayAll();
        LoadTable subject = new LoadTable(tablename);
        assertEquals(iteratorMock, subject.evaluate(database));
        verifyAll();
    }
}
