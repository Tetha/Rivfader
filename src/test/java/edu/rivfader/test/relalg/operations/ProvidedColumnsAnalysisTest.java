package edu.rivfader.test.relalg.operations;

import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;
import edu.rivfader.data.Database;
import java.util.List;
import edu.rivfader.relalg.representation.IRelAlgExpr;
import edu.rivfader.relalg.representation.LoadTable;
import edu.rivfader.relalg.operations.ProvidedColumnsAnalysis;
import java.util.Collection;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.easymock.EasyMock.expect;
import edu.rivfader.relalg.representation.QualifiedColumnName;
import edu.rivfader.relalg.representation.IQualifiedColumnName;
import edu.rivfader.relalg.representation.RenameTable;
import edu.rivfader.relalg.representation.ITable;
import java.util.HashSet;
import edu.rivfader.relalg.operations.StubResult;
import edu.rivfader.relalg.representation.Product;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.relalg.representation.IColumnProjection;
import edu.rivfader.relalg.representation.Projection;
import edu.rivfader.relalg.operations.ProvidedColumnsAnalysis;
import org.junit.Test;
import static org.easymock.EasyMock.anyObject;
import java.util.LinkedList;
import edu.rivfader.rowselector.representation.IRowSelector;
import edu.rivfader.relalg.representation.IQualifiedNameRow;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import org.powermock.api.easymock.annotation.Mock;
import org.junit.Before;


@PrepareForTest(Database.class)
@RunWith(PowerMockRunner.class)
public class ProvidedColumnsAnalysisTest {
    @Mock Database database;
    ProvidedColumnsAnalysis subject;

    @Before public void createSubject() {
        subject = new ProvidedColumnsAnalysis(database);
    }

    @Test public void testTransformLoadTable() throws IOException {
        String tableName = "table";
        List<String> columnNames = new LinkedList<String>();
        columnNames.add("foo");
        columnNames.add("bar");
        expect(database.getColumnNames(tableName)).andReturn(columnNames);

        IRelAlgExpr input = new LoadTable(tableName);

        replayAll();
        List<IQualifiedColumnName> output =
            new LinkedList<IQualifiedColumnName>(subject.transform(input));
        verifyAll();
        assertThat(output.get(0).getTable(), is(tableName));
        assertThat(output.get(1).getTable(), is(tableName));
        assertThat(output.get(0).getColumn(), is(columnNames.get(0)));
        assertThat(output.get(1).getColumn(), is(columnNames.get(1)));
    }

    @Test public void testTransformRenameTable() {
        ITable source = createMock(ITable.class);
        List<IQualifiedColumnName> cols =
            new LinkedList<IQualifiedColumnName>();
        cols.add(new QualifiedColumnName("oldname", "foo"));
        cols.add(new QualifiedColumnName("oldname", "bar"));

        source.setDatabase(database);
        expect(source.getColumnNames()).andReturn(cols);

        IRelAlgExpr input = new RenameTable(source, "newname");
        replayAll();
        List<IQualifiedColumnName> output =
            new LinkedList<IQualifiedColumnName>(subject.transform(input));
        verifyAll();

        assertThat(output.get(0).getTable(), is("newname"));
        assertThat(output.get(1).getTable(), is("newname"));
        assertThat(output.get(0).getColumn(), is(cols.get(0).getColumn()));
        assertThat(output.get(1).getColumn(), is(cols.get(1).getColumn()));
    }

    @Test public void testTransformProduct() {
        Collection<IQualifiedColumnName> leftResult =
            new LinkedList<IQualifiedColumnName>();
        leftResult.add(createMock(IQualifiedColumnName.class));
        Collection<IQualifiedColumnName> rightResult =
            new LinkedList<IQualifiedColumnName>();
        rightResult.add(createMock(IQualifiedColumnName.class));

        IRelAlgExpr left =
            new StubResult<Collection<IQualifiedColumnName>>(leftResult);
        IRelAlgExpr right =
            new StubResult<Collection<IQualifiedColumnName>>(rightResult);

        IRelAlgExpr input = new Product(left, right);

        replayAll();
        Collection<IQualifiedColumnName> output = subject.transform(input);
        verifyAll();
        assertTrue(output.containsAll(leftResult));
        assertTrue(output.containsAll(rightResult));
    }

    @Test public void testTransformSelection() {
        Collection<IQualifiedColumnName> subResult =
            new HashSet<IQualifiedColumnName>();
        IRelAlgExpr subExpression =
            new StubResult<Collection<IQualifiedColumnName>>(subResult);
        IRowSelector predicate = createMock(IRowSelector.class);

        IRelAlgExpr input = new Selection(predicate, subExpression);

        replayAll();
        Collection<IQualifiedColumnName> output = subject.transform(input);
        assertThat(output, is(subResult));
        verifyAll();
    }

    @Test public void testTransformProjection() {
        IQualifiedColumnName selectedColumn =
            createMock(IQualifiedColumnName.class);
        IQualifiedColumnName removedColumn =
            createMock(IQualifiedColumnName.class);

        Collection<IQualifiedColumnName> subResult =
            new HashSet<IQualifiedColumnName>();
        subResult.add(selectedColumn);
        subResult.add(removedColumn);

        IRelAlgExpr subExpression =
            new StubResult<Collection<IQualifiedColumnName>>(subResult);
        IColumnProjection projection = createMock(IColumnProjection.class);
        Collection<IColumnProjection> projections =
            new HashSet<IColumnProjection>();
        projections.add(projection);

        Collection<IQualifiedColumnName> projectedColumns =
            new HashSet<IQualifiedColumnName>();
        projectedColumns.add(selectedColumn);
        expect(projection.project((IQualifiedNameRow) anyObject())).andReturn(projectedColumns);

        IRelAlgExpr input = new Projection(subExpression, projections);
        replayAll();
        Collection<IQualifiedColumnName> output = subject.transform(input);
        assertThat(output, is(projectedColumns));
        verifyAll();
    }
}
