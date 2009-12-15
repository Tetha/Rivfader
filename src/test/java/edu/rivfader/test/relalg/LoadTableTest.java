package edu.rivfader.test.relalg;

import edu.rivfader.data.Database;
import edu.rivfader.data.Row;
import edu.rivfader.relalg.IRelAlgExpr;
import edu.rivfader.relalg.LoadTable;
import edu.rivfader.relalg.QualifiedColumnName;
import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.relalg.QualifiedNameRow;
import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.errors.ColumnWriteError;

import static org.easymock.EasyMock.expect;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.equalTo;


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
    @Test(expected = IllegalStateException.class)
    public void getColumnNamesBeforeSetDatabaseFails() {
        LoadTable s; // subject
        s = new LoadTable("tablename");
        s.getColumnNames();
    }

    @Test(expected = IllegalStateException.class)
    public void openForWritingBeforeSetDatabaseFails() {
        LoadTable s; // subject
        s = new LoadTable("tablename");
        s.openForWriting();
    }

    @Test(expected = IllegalStateException.class)
    public void openForWritingTwiceFails() {
        LoadTable s; // subject
        Database db; // database

        db = createMock(Database.class);
        s = new LoadTable("tablename");
        s.setDatabase(db);
        s.openForWriting();
        s.openForWriting();
    }

    @Test(expected = IllegalStateException.class)
    public void loadBeforeSetDatabaseFails() {
        LoadTable s; // subject
        s = new LoadTable("tablename");
        s.load();
    }

    @Test(expected = IllegalStateException.class)
    public void storeRowBeforeSetDatabaseFails() {
        LoadTable s; // subject
        s = new LoadTable("tablename");
        s.storeRow(null);
    }

    @Test(expected = IllegalStateException.class)
    public void storeRowBeforeOpenForWritingFails() {
        LoadTable s; // subject
        Database db; // database mock
        s = new LoadTable("tablename");
        db = createMock(Database.class);
        s.setDatabase(db);
        s.storeRow(null);
    }

    @Test(expected = IllegalStateException.class)
    public void appendRowBeforeSetDatabaseFails() {
        LoadTable s; // subject
        s = new LoadTable("tablename");
        s.appendRow(null);
    }

    @Test(expected = IllegalStateException.class)
    public void appendRowWhenOpenFails() {
        LoadTable s; // subject;
        s = new LoadTable("tablename");
        s.openForWriting();
        s.appendRow(null);
    }

    @Test(expected = IllegalStateException.class)
    public void closeTableWithoutOpenFails() {
        LoadTable s; // subject
        String tn; // tablename

        tn = "t";
        s = new LoadTable(tn);
        s.close();
    }

    @Test public void getColumnNamesIsDelegated() throws IOException {
        LoadTable s; // subject
        String t; // tablename
        Database db; // database mock
        List<String> cns; // column names
        List<IQualifiedColumnName> r; // result
        t = "t";
        db = createMock(Database.class);
        s = new LoadTable(t);
        s.setDatabase(db);
        cns = new LinkedList<String>();
        cns.add("foo");
        cns.add("bar");

        expect(db.getColumnNames(t)).andReturn(cns);
        replayAll();
        r = s.getColumnNames();
        verifyAll();
        assertThat(r.size(), is(equalTo(2)));
        assertThat(r, hasItems((IQualifiedColumnName) new QualifiedColumnName(t, "foo"),
                               new QualifiedColumnName(t, "bar")));
    }

    @Test public void openForWritingIsDelegated() throws IOException {
        LoadTable s; // subject
        String t; // tablename
        Database db; // database mock

        t = "table";
        s = new LoadTable(t);
        db = createMock(Database.class);
        s.setDatabase(db);
        db.openTableForWriting(t);
        replayAll();
        s.openForWriting();
        verifyAll();
    }

    @Test public void loadWorks() throws IOException {
        String tn; // table name
        List<Row> dbrs; // database rows
        Row fir; // first row returned from database
        Database db; // Database mock
        LoadTable s; // subject
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
        s = new LoadTable(tn);
        s.setDatabase(db);
        r = s.load();
        frr = r.next();
        assertThat(frr.columns().size(), is(equalTo(2)));
        assertThat(frr.columns(), hasItem((IQualifiedColumnName)new QualifiedColumnName(tn, "foo")));
        assertThat(frr.columns(), hasItem((IQualifiedColumnName)new QualifiedColumnName(tn, "bar")));
        assertThat(frr.getData(new QualifiedColumnName(tn, "foo")), is(equalTo("foo")));
        assertThat(frr.getData(new QualifiedColumnName(tn, "bar")), is(equalTo("bar")));
        assertThat(r.hasNext(), is(false));
        verifyAll();
    }


    @Test(expected = ColumnWriteError.class)
    public void storeRowWithdisagreeingTableNamesFails()
        throws IOException {
        String tn; // table name
        String wtn; // wrong table name.
        IQualifiedNameRow ir; // input row
        LoadTable s; // subject
        Database db; // database mock

        tn = "t";
        wtn = tn + "1";
        assertThat(wtn, is(not(equalTo(tn))));

        db = createMock(Database.class);
        db.openTableForWriting(tn);

        ir = new QualifiedNameRow(new QualifiedColumnName(tn, "foo"),
                                  new QualifiedColumnName(wtn, "bar"));
        replayAll();
        s = new LoadTable(tn);
        s.setDatabase(db);
        s.openForWriting();
        s.storeRow(ir);
        verifyAll();
    }

    @Test(expected = ColumnWriteError.class)
    public void storeRowWithWrongTablenameFails() throws IOException {
        String tn; // table name
        String wtn; // wrong table name.
        IQualifiedNameRow ir; // input row
        LoadTable s; // subject
        Database db; // database mock

        tn = "t";
        wtn = tn + "1";
        assertThat(wtn, is(not(equalTo(tn))));

        db = createMock(Database.class);
        db.openTableForWriting(tn);

        ir = new QualifiedNameRow(new QualifiedColumnName(wtn, "foo"),
                                  new QualifiedColumnName(wtn, "bar"));
        replayAll();
        s = new LoadTable(tn);
        s.setDatabase(db);
        s.openForWriting();
        s.storeRow(ir);
        verifyAll();
    }

    @Test public void storeRowIsDelegated() throws IOException {
        String tn; // table name
        LoadTable s; // subject
        Database db; // database
        IQualifiedNameRow ir; // input row
        Row dbr; // database row

        tn = "t";
        db = createMock(Database.class);
        ir = new QualifiedNameRow(new QualifiedColumnName(tn, "foo"),
                                  new QualifiedColumnName(tn, "bar"));
        ir.setData(new QualifiedColumnName(tn, "foo"), "foodata");
        ir.setData(new QualifiedColumnName(tn, "bar"), "bardata");

        s = new LoadTable(tn);
        db.openTableForWriting(tn);

        dbr = new Row("foo", "bar");
        dbr.setData("foo", "foodata");
        dbr.setData("bar", "bardata");
        db.storeRow(tn, dbr);
        replayAll();
        s.setDatabase(db);
        s.openForWriting();
        s.storeRow(ir);
        verifyAll();
    }

    @Test(expected = IllegalArgumentException.class)
    public void storeNullRowFails() throws IOException {
        String tn; // table name
        LoadTable s; // subject
        Database db; // database mock

        tn = "t";
        db = createMock(Database.class);
        db.openTableForWriting(tn);

        replayAll();
        s = new LoadTable(tn);
        s.setDatabase(db);
        s.openForWriting();
        s.storeRow(null);
        verifyAll();

    }

    @Test public void appendRowIsDelegated()
        throws IOException {
        String tn; // table name
        LoadTable s; // subject
        Database db; // database mock
        IQualifiedNameRow qr; // appended input row
        Row dbr; // database row

        tn = "t";
        qr = new QualifiedNameRow(new QualifiedColumnName(tn, "foo"),
                                  new QualifiedColumnName(tn, "bar"));
        qr.setData(new QualifiedColumnName(tn, "foo"), "foodata");
        qr.setData(new QualifiedColumnName(tn, "bar"), "bardata");

        dbr = new Row("foo", "bar");
        dbr.setData("foo", "foodata");
        dbr.setData("bar", "bardata");

        db = createMock(Database.class);
        db.appendRow(tn, dbr);

        replayAll();
        s = new LoadTable(tn);
        s.setDatabase(db);
        s.appendRow(qr);
        verifyAll();
    }

    @Test public void closeIsDelegated() throws IOException {
        String tn; // tablename
        Database db; // database mock
        LoadTable s; // subject

        tn = "t";
        db = createMock(Database.class);
        db.openTableForWriting(tn);
        db.closeTable(tn);
        replayAll();
        s = new LoadTable(tn);
        s.setDatabase(db);
        s.openForWriting();
        s.close();
        verifyAll();
    }
}
