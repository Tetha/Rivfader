package edu.rivfader.test.commands;

import edu.rivfader.commands.Delete;
import edu.rivfader.data.Database;
import edu.rivfader.data.Row;
import edu.rivfader.relalg.rowselector.IRowSelector;
import edu.rivfader.relalg.rowselector.StubResult;
import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.QualifiedNameRow;
import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.relalg.QualifiedColumnName;
import edu.rivfader.relalg.ITable;
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
        IQualifiedColumnName c1; // one column name
        IQualifiedColumnName c2; // another column name
        IQualifiedNameRow r1; // an example row
        IQualifiedNameRow r2; // another example row
        List<IQualifiedNameRow> trs; // loaded rows in the table
        IRowSelector p; // predicate to select rows to delete
        ITable t; // table mock
        Database db; // database mock
        Writer w; // writer mock
        t = createMock(ITable.class);

        p = new StubResult(true, false);
        db = createMock(Database.class);
        w = createMock(Writer.class);

        c1 = new QualifiedColumnName("t", "c1");
        c2 = new QualifiedColumnName("t", "c2");

        r1 = new QualifiedNameRow(c1, c2);
        r2 = new QualifiedNameRow(c1, c2);
        r1.setData(c1, "d11");
        r1.setData(c2, "d12");
        r2.setData(c1, "d21");
        r2.setData(c2, "d22");

        trs = new LinkedList<IQualifiedNameRow>();
        trs.add(r1);
        trs.add(r2);

        t.setDatabase(db);
        t.openForWriting();
        expect(t.load()).andReturn(trs.iterator());
        t.storeRow(r2);
        t.close();
        replayAll();
        Delete subject = new Delete(t, p);
        subject.execute(db, w);
        verifyAll();
    }
}
