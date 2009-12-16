package edu.rivfader.test.relalg;

import edu.rivfader.data.Database;
import edu.rivfader.data.Row;
import edu.rivfader.relalg.Evaluator;
import edu.rivfader.relalg.StubResult;
import edu.rivfader.relalg.RowSetStubResult;
import edu.rivfader.relalg.IRelAlgExpr;
import edu.rivfader.relalg.Product;
import edu.rivfader.relalg.QualifiedNameRow;
import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.QualifiedColumnName;
import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.relalg.Projection;
import edu.rivfader.relalg.IColumnProjection;
import edu.rivfader.relalg.Selection;
import edu.rivfader.relalg.rowselector.IRowSelector;
import edu.rivfader.relalg.ITable;
import edu.rivfader.relalg.LoadTable;
import edu.rivfader.relalg.RenameTable;
import edu.rivfader.relalg.RowFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;

import org.powermock.modules.junit4.PowerMockRunner;

import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.easymock.EasyMock.expect;

import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

import java.io.IOException;

@RunWith(PowerMockRunner.class)
public class EvaluatorTest {
    @Test
    public void transformProductWorks() {
        Database database = createMock(Database.class);
        List<IQualifiedNameRow> leftResult =
            new LinkedList<IQualifiedNameRow>();
        IQualifiedColumnName ln = new QualifiedColumnName("t", "c");
        RowFactory leftRows = new RowFactory(new String[] {"t", "c"});
        leftRows.addRow("cow");
        leftRows.addRow("more cow");
        leftResult = leftRows.getRows();

        List<IQualifiedNameRow> rightResult =
            new LinkedList<IQualifiedNameRow>();
        RowFactory rightRows = new RowFactory(new String[] {"u", "d"});
        rightRows.addRow("chicken");
        rightRows.addRow("more chicken");
        rightResult = rightRows.getRows();

        RowSetStubResult right = new RowSetStubResult(rightResult);
        RowSetStubResult left = new RowSetStubResult(leftResult);

        replayAll();
        Product input = new Product(left, right);
        Evaluator subject = new Evaluator(database);
        Iterator<IQualifiedNameRow> resultRows = subject.transformProduct(input);

        List<IQualifiedNameRow> gotRows = new LinkedList<IQualifiedNameRow>();
        while(resultRows.hasNext()) {
            gotRows.add(resultRows.next());
        }

        RowFactory expectedRows = new RowFactory();
        expectedRows.addRow(new QualifiedNameRow(leftResult.get(0),
                                                 rightResult.get(0)));
        expectedRows.addRow(new QualifiedNameRow(leftResult.get(0),
                                                 rightResult.get(1)));
        expectedRows.addRow(new QualifiedNameRow(leftResult.get(1),
                                                 rightResult.get(0)));
        expectedRows.addRow(new QualifiedNameRow(leftResult.get(1),
                                                 rightResult.get(1)));

        assertEquals(expectedRows.getRows(), gotRows);
        verifyAll();
    }

    @Test
    public void transformProjection() {
        Database db; // database mock
        IRelAlgExpr se; // subexpression
        IQualifiedColumnName scn; // selected column name
        IQualifiedColumnName ucn; // unselected column name
        List<IQualifiedNameRow> ser; // sub expression result
        IColumnProjection cp; // column projection mock
        IQualifiedNameRow row; // row
        List<IQualifiedColumnName> cs; // row columns
        List<IQualifiedColumnName> scs; // selected columns
        Set<IColumnProjection> ps; // projections
        Iterator<IQualifiedNameRow> res; // result from unit under test
        List<IQualifiedNameRow> rrs; // returned rows

        db = createMock(Database.class);
        se = createMock(IRelAlgExpr.class);
        scn = createMock(IQualifiedColumnName.class);
        ucn = createMock(IQualifiedColumnName.class);
        ser = new LinkedList<IQualifiedNameRow>();
        row = createMock(IQualifiedNameRow.class);
        cp = createMock(IColumnProjection.class);
        scs = new LinkedList<IQualifiedColumnName>();
        ps = new HashSet<IColumnProjection>();
        rrs = new LinkedList<IQualifiedNameRow>();

        ser.add(row);

        scs.add(scn);

        ps.add(cp);

        se = new RowSetStubResult(ser);
        expect(cp.project(row)).andReturn(scs);
        expect(row.getData(scn)).andReturn("some data");

        replayAll();
        Projection input = new Projection(se, ps);
        Evaluator subject = new Evaluator(db);
        res = subject.transform(input);
        while(res.hasNext()) {
            rrs.add(res.next());
        }
        assertThat(rrs.size(), is(equalTo(1)));
        assertThat(rrs.get(0).getData(scn), is(equalTo("some data")));

        verifyAll();
    }

    @Test
    public void transformSelection() {
        Database database = createMock(Database.class);
        IQualifiedNameRow first = createMock(IQualifiedNameRow.class);
        IQualifiedNameRow second = createMock(IQualifiedNameRow.class);
        IQualifiedNameRow third = createMock(IQualifiedNameRow.class);

        IRowSelector predicate = createMock(IRowSelector.class);
        expect(predicate.acceptsRow(first)).andReturn(true);
        expect(predicate.acceptsRow(second)).andReturn(false);
        expect(predicate.acceptsRow(third)).andReturn(true);

        List<IQualifiedNameRow> previousRows =
            new LinkedList<IQualifiedNameRow>();
        previousRows.add(first);
        previousRows.add(second);
        previousRows.add(third);
        IRelAlgExpr subExpression = new RowSetStubResult(previousRows);

        replayAll();
        Selection input = new Selection(predicate, subExpression);
        Evaluator subject = new Evaluator(database);
        Iterator<IQualifiedNameRow> selectedRows = subject.transform(input);
        List<IQualifiedNameRow> gotRows = new LinkedList<IQualifiedNameRow>();
        while(selectedRows.hasNext()) {
            gotRows.add(selectedRows.next());
        }

        List<IQualifiedNameRow> expectedRows =
            new LinkedList<IQualifiedNameRow>();
        expectedRows.add(first);
        expectedRows.add(third);

        assertEquals(expectedRows, gotRows);
        verifyAll();
    }

    @Test public void transformLoadTable() throws IOException {
        String tn; // table name
        List<Row> dbrs; // database rows
        Row fir; // first row returned from database
        Database db; // Database mock
        LoadTable i; // input
        Evaluator s; // subject
        Iterator<IQualifiedNameRow> r; // result
        IQualifiedNameRow frr; // first result row

        tn = "table";
        dbrs = new LinkedList<Row>();
        fir= new Row("foo", "bar");
        fir.setData("foo", "foo");
        fir.setData("bar", "bar");
        dbrs.add(fir);

        db = createMock(Database.class);
        expect(db.loadTable(tn)).andReturn(dbrs.iterator());
        replayAll();
        i = new LoadTable(tn);
        s = new Evaluator(db);
        r = s.transformLoadTable(i);
        frr = r.next();
        assertThat(frr.columns().size(), is(equalTo(2)));
        assertThat(frr.columns(), hasItem((IQualifiedColumnName)new QualifiedColumnName(tn, "foo")));
        assertThat(frr.columns(), hasItem((IQualifiedColumnName)new QualifiedColumnName(tn, "bar")));
        assertThat(frr.getData(new QualifiedColumnName(tn, "foo")), is(equalTo("foo")));
        assertThat(frr.getData(new QualifiedColumnName(tn, "bar")), is(equalTo("bar")));
        assertThat(r.hasNext(), is(false));
        verifyAll();
    }

    @Test public void transformRenameTable() {
        Database db; // database mock
        String on; // old name
        String nn; // new name
        String cn1; // first column name
        String cn2; // second column name
        ITable dt; // decorated table
        RenameTable i; // input
        Iterator<IQualifiedNameRow> r; // result
        Collection<IQualifiedNameRow> rrs; // result rows

        IQualifiedColumnName sn1; // source name 1
        IQualifiedColumnName sn2; // source name 2
        IQualifiedColumnName tn1; // target name 1
        IQualifiedColumnName tn2; // target name 2

        IQualifiedNameRow sr1; // source row 1
        IQualifiedNameRow sr2; // source row 2
        IQualifiedNameRow tr1; // translated row 1
        IQualifiedNameRow tr2; // translated row 2

        List<IQualifiedNameRow> lrs; // loaded rows

        RowFactory srs; // source rows
        RowFactory trs; // transformed rows

        on = "on";
        nn = "nn";
        db = createMock(Database.class);
        dt = createMock(ITable.class);

        srs = new RowFactory(new String[] {"on", "c1"},
                             new String[] {"on", "c2"});
        srs.addRow("d11", "d12");
        srs.addRow("d21", "d22");

        trs = new RowFactory(new String[] {"nn", "c1"},
                             new String[] {"nn", "c2"});
        trs.addRow("d11", "d12");
        trs.addRow("d21", "d22");

        expect(dt.load()).andReturn(srs.getRows().iterator());

        replayAll();
        i = new RenameTable(dt, nn);
        Evaluator s = new Evaluator(db);
        r = s.transformRenameTable(i);
        rrs = new LinkedList<IQualifiedNameRow>();
        while(r.hasNext()) {
            rrs.add(r.next());
        }

        assertThat(new LinkedList<IQualifiedNameRow>(rrs),
                   is(equalTo(trs.getRows())));
        verifyAll();
    }
}
