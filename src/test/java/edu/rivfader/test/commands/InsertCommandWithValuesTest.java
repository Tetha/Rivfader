package edu.rivfader.test.commands;

import edu.rivfader.commands.InsertCommandWithValues;
import edu.rivfader.data.Database;
import edu.rivfader.data.Row;
import edu.rivfader.relalg.representation.IQualifiedColumnName;
import edu.rivfader.relalg.representation.QualifiedColumnName;
import edu.rivfader.relalg.representation.IQualifiedNameRow;
import edu.rivfader.relalg.representation.QualifiedNameRow;
import edu.rivfader.relalg.representation.ITable;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

import java.io.Writer;
import java.io.IOException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Database.class)
public class InsertCommandWithValuesTest {
    @Test
    public void valuesAreInserted() throws IOException {
        ITable t; // table to operate on
        Writer w; // writer mock
        Database db; // database mock
        Map<String, String> v; // the value-mapping for the insertion
        IQualifiedNameRow vr; // the new row to expect
        List<IQualifiedColumnName> cns; // list of columns of the table
        IQualifiedColumnName c1; // example column name
        IQualifiedColumnName c2; // another example column name
        InsertCommandWithValues s; // subject to examine

        t = createMock(ITable.class);
        w = createMock(Writer.class);
        db = createMock(Database.class);
        c1 = new QualifiedColumnName("t", "cow");
        c2 = new QualifiedColumnName("t", "chicken");

        v = new HashMap<String, String>();
        v.put("cow", "milk");
        v.put("chicken", "egg");

        cns = new LinkedList<IQualifiedColumnName>();
        cns.add(c1);
        cns.add(c2);

        vr = new QualifiedNameRow(cns);
        vr.setData(c1, "milk");
        vr.setData(c2, "egg");
        t.setDatabase(db);
        expect(t.getColumnNames()).andReturn(cns);
        t.appendRow(vr);
        replayAll();
        s = new InsertCommandWithValues(t, v);
        s.execute(db, w);
        verifyAll();
    }
}
