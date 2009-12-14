package edu.rivfader.test.relalg;

import edu.rivfader.data.Database;
import edu.rivfader.data.Row;
import edu.rivfader.relalg.Evaluator;
import edu.rivfader.relalg.StubResult;
import edu.rivfader.relalg.IRelAlgExpr;
import edu.rivfader.relalg.Product;
import edu.rivfader.relalg.QualifiedNameRow;
import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.QualifiedColumnName;
import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.relalg.Projection;
import edu.rivfader.relalg.IColumnProjection;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

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

import java.util.Iterator;
import java.util.NoSuchElementException;

public class EvaluatorTest {
    @Test
    public void transformProductWorks() {
        Database database = createMock(Database.class);
        List<IQualifiedNameRow> leftResult =
            new LinkedList<IQualifiedNameRow>();
        IQualifiedColumnName ln = new QualifiedColumnName("t", "c");
        IQualifiedNameRow leftFirst = new QualifiedNameRow(ln);
        leftFirst.setData(ln, "cow");
        leftResult.add(leftFirst);
        QualifiedNameRow leftSecond = new QualifiedNameRow(ln);
        leftSecond.setData(ln, "more cow");
        leftResult.add(leftSecond);

        List<IQualifiedNameRow> rightResult =
            new LinkedList<IQualifiedNameRow>();
        IQualifiedColumnName rn = new QualifiedColumnName("u", "d");
        IQualifiedNameRow rightFirst = new QualifiedNameRow(rn);
        rightFirst.setData(rn, "chicken");
        rightResult.add(rightFirst);
        IQualifiedNameRow rightSecond = new QualifiedNameRow(rn);
        rightSecond.setData(rn, "more chicken");
        rightResult.add(rightSecond);

        IRelAlgExpr left = new StubResult<Iterator<IQualifiedNameRow>>(leftResult);
        IRelAlgExpr right = new StubResult<Iterator<IQualifiedNameRow>>(rightResult);

        replayAll();
        Product input = new Product(left, right);
        Evaluator subject = new Evaluator(database);
        Iterator<IQualifiedNameRow> resultRows = subject.transformProduct(input);

        List<IQualifiedNameRow> gotRows = new LinkedList<IQualifiedNameRow>();
        while(resultRows.hasNext()) {
            gotRows.add(resultRows.next());
        }

        List<IQualifiedNameRow> expectedRows =
            new LinkedList<IQualifiedNameRow>();

        expectedRows.add(new QualifiedNameRow(leftFirst, rightFirst));
        expectedRows.add(new QualifiedNameRow(leftFirst, rightSecond));
        expectedRows.add(new QualifiedNameRow(leftSecond, rightFirst));
        expectedRows.add(new QualifiedNameRow(leftSecond, rightSecond));

        assertEquals(expectedRows, gotRows);
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

        se = new StubResult<Iterator<IQualifiedNameRow>>(ser);
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


}
