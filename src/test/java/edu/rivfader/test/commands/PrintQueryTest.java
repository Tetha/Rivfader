package edu.rivfader.test.commands;

import edu.rivfader.commands.PrintQuery;
import edu.rivfader.data.Database;
import edu.rivfader.data.Row;
import edu.rivfader.relalg.IRelAlgExpr;

import java.io.Writer;
import java.io.IOException;

import java.util.List;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.easymock.EasyMock.expect;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Row.class, Database.class})
public class PrintQueryTest {
    @Test
    public void checkPrintQueryExecution() throws IOException {
        Database database = createMock(Database.class);
        IRelAlgExpr query = createMock(IRelAlgExpr.class);
        List<String> columns = new LinkedList<String>();
        Writer writer = createMock(Writer.class);
        columns.add("cow");
        columns.add("chicken");
        Row row1 = new Row(columns);
        row1.setData("cow", "milk");
        row1.setData("chicken", "eggs");
        Row row2 = new Row(columns);
        row2.setData("cow", "cows");
        row2.setData("chicken", "chickens");
        List<Row> resultRows = new LinkedList<Row>();
        resultRows.add(row1);
        resultRows.add(row2);
        expect(query.evaluate(database)).andReturn(resultRows.iterator());
        writer.write("cow chicken\n");
        writer.write("milk eggs\n");
        writer.write("cows chickens\n");
        replayAll();
        PrintQuery subject = new PrintQuery(query);
        subject.execute(database, writer);
        verifyAll();
    }
}
