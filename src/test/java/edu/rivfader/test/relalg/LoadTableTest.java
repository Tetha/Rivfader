package edu.rivfader.test.relalg;

import edu.rivfader.data.Database;
import edu.rivfader.data.Row;
import edu.rivfader.relalg.LoadTable;
import edu.rivfader.relalg.QualifiedColumnName;
import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.relalg.QualifiedNameRow;
import edu.rivfader.relalg.IQualifiedNameRow;

import static org.easymock.EasyMock.expect;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.io.IOException;
@RunWith(PowerMockRunner.class)
@PrepareForTest(Database.class)
public class LoadTableTest {

    @Test
    public void checkLoading() throws IOException {
        String tablename=  "table";
        List<Row> databaseRows = new LinkedList<Row>();
        Row firstInputRow = new Row("foo", "bar");
        firstInputRow.setData("foo", "foo");
        firstInputRow.setData("bar", "bar");
        databaseRows.add(firstInputRow);

        Database database = createMock(Database.class);
        expect(database.loadTable(tablename))
            .andReturn(databaseRows.iterator());
        replayAll();
        LoadTable subject = new LoadTable(tablename);
        Iterator<IQualifiedNameRow> result = subject.evaluate(database);
        IQualifiedNameRow firstRow = result.next();
        assertEquals(2, firstRow.columns().size());
        assertTrue(firstRow
                        .columns()
                        .contains(new QualifiedColumnName(tablename, "foo")));
        assertTrue(firstRow
                        .columns()
                        .contains(new QualifiedColumnName(tablename, "bar")));
        assertEquals("foo", firstRow.getData(new QualifiedColumnName(tablename,
                                                                     "foo")));
        assertEquals("bar", firstRow.getData(new QualifiedColumnName(tablename,
                                                                     "bar")));
        assertFalse(result.hasNext());
        verifyAll();
    }
}
