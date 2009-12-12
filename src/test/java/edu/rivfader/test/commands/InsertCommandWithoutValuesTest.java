package edu.rivfader.test.commands;

import edu.rivfader.commands.InsertCommandWithoutValues;
import edu.rivfader.data.Database;
import edu.rivfader.data.Row;
import edu.rivfader.relalg.ITable;
import edu.rivfader.relalg.QualifiedNameRow;
import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.QualifiedColumnName;
import edu.rivfader.relalg.IQualifiedColumnName;

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
        ITable t; // table to operate on
        Database db; // database the table is in
        Writer w; // writer for output
        List<String> v; // list of values to insert
        List<IQualifiedColumnName> cns; // column names of table
        IQualifiedColumnName c1; // example column name
        IQualifiedColumnName c2; // example column name
        IQualifiedNameRow vr; // row to expect to be appended
        InsertCommandWithoutValues s; // subject to examine

        t = createMock(ITable.class);
        db = createMock(Database.class);
        w = createMock(Writer.class);
        v = new LinkedList<String>();
        v.add("milk");
        v.add("eggs");

        c1 = createMock(IQualifiedColumnName.class);
        c2 = createMock(IQualifiedColumnName.class);
        cns = new LinkedList<IQualifiedColumnName>();
        cns.add(c1);
        cns.add(c2);

        vr = new QualifiedNameRow(cns);
        vr.setData(c1, "milk");
        vr.setData(c2, "eggs");

        t.setDatabase(db);
        expect(t.getColumnNames()).andReturn(cns);
        t.appendRow(vr);

        replayAll();
        s = new InsertCommandWithoutValues(t, v);
        s.execute(db, w);
        verifyAll();
    }
}
