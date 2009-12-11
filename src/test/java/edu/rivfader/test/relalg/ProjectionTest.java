package edu.rivfader.test.relalg;

import edu.rivfader.data.Database;
import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.Projection;
import edu.rivfader.relalg.IRelAlgExpr;
import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.relalg.IColumnProjection;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import org.powermock.api.easymock.annotation.Mock;
import static org.easymock.EasyMock.expect;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Database.class)
public class ProjectionTest {
    private Projection subject;

    @Test
    public void evaluate() {
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

        expect(se.evaluate(db)).andReturn(ser.iterator());
        expect(cp.project(row)).andReturn(scs);
        expect(row.getData(scn)).andReturn("some data");

        replayAll();
        Projection subject = new Projection(se, ps);
        res = subject.evaluate(db);
        while(res.hasNext()) {
            rrs.add(res.next());
        }
        assertThat(rrs.size(), is(equalTo(1)));
        assertThat(rrs.get(0).getData(scn), is(equalTo("some data")));

        verifyAll();
    }
}
