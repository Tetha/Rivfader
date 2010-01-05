package edu.rivfader.test.commands;

import edu.rivfader.commands.UpdateTable;
import edu.rivfader.data.Database;
import edu.rivfader.data.Row;
import edu.rivfader.rowselector.representation.IRowSelector;
import edu.rivfader.relalg.representation.IQualifiedNameRow;
import edu.rivfader.relalg.representation.QualifiedNameRow;
import edu.rivfader.relalg.representation.QualifiedColumnName;
import edu.rivfader.relalg.representation.IQualifiedColumnName;
import edu.rivfader.relalg.representation.ITable;
import edu.rivfader.rowselector.representation.StubResult;
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
        ITable t; // table mock;
        Writer w; // writer mock
        IQualifiedColumnName cow; // example column name
        IQualifiedColumnName chicken; // another example column name
        List<IQualifiedColumnName> cns; // column names
        IRowSelector p; // predicate selecting the rows to change
        Database db; // database mock
        Map<String, String> as; //assignements
        LinkedList<IQualifiedNameRow> rs; // loaded rows
        IQualifiedNameRow mr; // row to change
        IQualifiedNameRow umr; // row not to change

        t = createMock(ITable.class);
        w = createMock(Writer.class);
        cow = createMock(IQualifiedColumnName.class);
        chicken = createMock(IQualifiedColumnName.class);
        cns = new LinkedList<IQualifiedColumnName>();
        cns.add(chicken);
        cns.add(cow);

        p = new StubResult(true, false);
        db = createMock(Database.class);
        as = new HashMap<String, String>();
        as.put("chicken", "chickens");

        rs = new LinkedList<IQualifiedNameRow>();
        mr = createMock(IQualifiedNameRow.class);
        rs.add(mr);

        umr = createMock(IQualifiedNameRow.class);
        rs.add(umr);

        t.setDatabase(db);
        t.openForWriting();
        expect(t.load()).andReturn(rs.iterator());
        expect(mr.resolveUnqualifiedName("chicken")).andReturn(chicken);
        mr.setData(chicken, "chickens");

        t.storeRow(mr);
        t.storeRow(umr);
        t.close();

        replayAll();
        UpdateTable subject = new UpdateTable(t, as, p);
        subject.execute(db, w);
        verifyAll();
    }
}
