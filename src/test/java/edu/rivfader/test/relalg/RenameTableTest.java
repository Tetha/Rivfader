package edu.rivfader.test.relalg;

import edu.rivfader.data.Database;
import edu.rivfader.relalg.RenameTable;
import edu.rivfader.relalg.ITable;
import edu.rivfader.relalg.QualifiedNameRow;
import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.relalg.QualifiedColumnName;
import edu.rivfader.relalg.IRelAlgExpr;

import java.util.List;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.expectNew;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.createMockAndExpectNew;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

@RunWith(PowerMockRunner.class)
public class RenameTableTest {
    @Test(expected = IllegalArgumentException.class)
    public void decoratedMustNotBeNull() {
        RenameTable s; // subject
        s = new RenameTable(null, "r");
    }

    @Test(expected = IllegalArgumentException.class)
    public void resultNameMustNotBeNull() {
        RenameTable s; // subject
        ITable dt; // decorated table

        dt = createMock(ITable.class);
        s = new RenameTable(dt, null);
    }

    @Test public void columnNamesTranslated() {
        String nn; // new table name
        String cn; // column name
        RenameTable s; // subject
        ITable dt; // decorated table
        List<IQualifiedColumnName> cns; // column names
        IQualifiedColumnName ocn; // old column name
        IQualifiedColumnName ncn; // new column name
        List<IQualifiedColumnName> r; // result

        nn = "nn";
        cn = "cn";
        dt = createMock(ITable.class);
        ocn = createMock(IQualifiedColumnName.class);
        cns = new LinkedList<IQualifiedColumnName>();
        cns.add(ocn);
        expect(dt.getColumnNames()).andReturn(cns);

        expect(ocn.getColumn()).andReturn(cn);
        ncn = new QualifiedColumnName(nn, cn);

        replayAll();
        s = new RenameTable(dt, nn);
        r = s.getColumnNames();
        assertThat(r, hasItem((IQualifiedColumnName)ncn));
        assertThat(r.size(), is(equalTo(1)));
        verifyAll();
    }

    @Test public void setDatabaseDelegated() {
        String nn; // new name
        ITable dt; // decorated table
        RenameTable s; // subject
        Database db; // database mock;

        nn = "nn";
        dt = createMock(ITable.class);
        db = createMock(Database.class);

        dt.setDatabase(db);
        replayAll();
        s = new RenameTable(dt, nn);
        s.setDatabase(db);
        verifyAll();
    }
    @Test public void openForWritingDelegated() {
        String nn; // new name
        RenameTable s; // subject
        ITable dt; // decorated table

        nn = "nn";
        dt = createMock(ITable.class);

        dt.openForWriting();
        replayAll();
        s = new RenameTable(dt, nn);
        s.openForWriting();
        verifyAll();
    }

    @Test public void loadTranslated() {
        String on; // old name
        String nn; // new name
        String cn1; // first column name
        String cn2; // second column name
        ITable dt; // decorated table
        RenameTable s; // subject
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

        on = "on";
        nn = "nn";
        dt = createMock(ITable.class);

        sn1 = new QualifiedColumnName(on, "c1");
        sn2 = new QualifiedColumnName(on, "c2");
        tn1 = new QualifiedColumnName(nn, "c1");
        tn2 = new QualifiedColumnName(nn, "c2");

        sr1 = new QualifiedNameRow(sn1, sn2);
        sr2 = new QualifiedNameRow(sn1, sn2);
        tr1 = new QualifiedNameRow(tn1, tn2);
        tr2 = new QualifiedNameRow(tn1, tn2);

        sr1.setData(sn1, "d11");
        sr1.setData(sn2, "d12");
        sr2.setData(sn1, "d21");
        sr2.setData(sn2, "d22");

        tr1.setData(tn1, "d11");
        tr1.setData(tn2, "d12");
        tr2.setData(tn1, "d21");
        tr2.setData(tn2, "d22");

        lrs = new LinkedList<IQualifiedNameRow>();
        lrs.add(sr1);
        lrs.add(sr2);
        expect(dt.load()).andReturn(lrs.iterator());

        replayAll();
        s = new RenameTable(dt, nn);
        r = s.load();
        rrs = new LinkedList<IQualifiedNameRow>();
        while(r.hasNext()) {
            rrs.add(r.next());
        }

        assertThat(rrs, hasItems(tr1, tr2));
        assertThat(rrs.size(), is(equalTo(2)));
        verifyAll();
    }


    @Test public void storeRowTranslated() {
        String on; // old name
        String nn; // new name
        RenameTable s; // subject
        ITable dt; // decorated table
        IQualifiedNameRow ir; // input row
        IQualifiedNameRow or; // output row

        IQualifiedColumnName icn1; // input column name 1
        IQualifiedColumnName icn2; // input colum name 2
        IQualifiedColumnName ocn1; // output column name 1
        IQualifiedColumnName ocn2; // output column name 2

        on = "o";
        nn = "n";
        dt = createMock(ITable.class);
        icn1 = new QualifiedColumnName(nn, "c1");
        icn2 = new QualifiedColumnName(nn, "c2");
        ocn1 = new QualifiedColumnName(on, "c1");
        ocn2 = new QualifiedColumnName(on, "c2");

        ir = new QualifiedNameRow(icn1, icn2);
        ir.setData(icn1, "d1");
        ir.setData(icn2, "d2");

        or = new QualifiedNameRow(ocn1, ocn2);
        or.setData(ocn1, "d1");
        or.setData(ocn2, "d2");

        dt.storeRow(or);
        expect(dt.getName()).andReturn(on);
        replayAll();
        s = new RenameTable(dt, nn);
        s.storeRow(ir);
        verifyAll();
    }

    @Test public void appendRowTranslated() {
        String on; // old name
        String nn; // new name
        RenameTable s; // subject
        ITable dt; // decorated table
        IQualifiedNameRow ir; // input row
        IQualifiedNameRow or; // output row

        IQualifiedColumnName icn1; // input column name 1
        IQualifiedColumnName icn2; // input colum name 2
        IQualifiedColumnName ocn1; // output column name 1
        IQualifiedColumnName ocn2; // output column name 2

        on = "o";
        nn = "n";
        dt = createMock(ITable.class);
        icn1 = new QualifiedColumnName(nn, "c1");
        icn2 = new QualifiedColumnName(nn, "c2");
        ocn1 = new QualifiedColumnName(on, "c1");
        ocn2 = new QualifiedColumnName(on, "c2");

        ir = new QualifiedNameRow(icn1, icn2);
        ir.setData(icn1, "d1");
        ir.setData(icn2, "d2");

        or = new QualifiedNameRow(ocn1, ocn2);
        or.setData(ocn1, "d1");
        or.setData(ocn2, "d2");

        dt.appendRow(or);
        expect(dt.getName()).andReturn(on);
        replayAll();
        s = new RenameTable(dt, nn);
        s.appendRow(ir);
        verifyAll();
    }

    @Test public void closeDelegated() {
        String nn; // new name
        ITable dt; // decorated table
        RenameTable s; // subject

        nn = "n";
        dt = createMock(ITable.class);
        dt.close();
        replayAll();
        s = new RenameTable(dt, nn);
        s.close();
        verifyAll();
    }
}
