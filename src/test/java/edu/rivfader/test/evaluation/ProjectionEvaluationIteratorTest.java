package edu.rivfader.test.evaluation;

import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.relalg.Projection;
import edu.rivfader.relalg.IColumnProjection;
import edu.rivfader.evaluation.ProjectionEvaluationIterator;
import edu.rivfader.evaluation.Evaluator;
import edu.rivfader.relalg.IRelAlgExpr;

import org.junit.Test;
import org.junit.runner.RunWith;
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

@RunWith(PowerMockRunner.class)
@PrepareForTest({Evaluator.class, Projection.class})
public class ProjectionEvaluationIteratorTest {
    @Test
    public void transformProjection() {
        IQualifiedColumnName scn; // selected column name
        List<IQualifiedNameRow> ser; // sub expression result
        IColumnProjection cp; // column projection mock
        IQualifiedNameRow row; // row
        List<IQualifiedColumnName> scs; // selected columns
        Set<IColumnProjection> ps; // projections
        List<IQualifiedNameRow> rrs; // returned rows
        Projection p; // input projection
        Evaluator e; // mock evaluator
        IRelAlgExpr se; // sub expression

        e = createMock(Evaluator.class);
        p = createMock(Projection.class);
        se = createMock(IRelAlgExpr.class);
        scn = createMock(IQualifiedColumnName.class);
        ser = new LinkedList<IQualifiedNameRow>();
        row = createMock(IQualifiedNameRow.class);
        cp = createMock(IColumnProjection.class);
        scs = new LinkedList<IQualifiedColumnName>();
        ps = new HashSet<IColumnProjection>();
        rrs = new LinkedList<IQualifiedNameRow>();

        expect(p.getSubExpression()).andReturn(se);
        expect(p.getSelectedFields()).andReturn(ps);
        ser.add(row);

        expect(e.transform(se)).andReturn(ser.iterator());

        scs.add(scn);

        ps.add(cp);

        expect(cp.project(row)).andReturn(scs);
        expect(row.getData(scn)).andReturn("some data");

        replayAll();
        ProjectionEvaluationIterator subject =
            new ProjectionEvaluationIterator(p, e);
        while(subject.hasNext()) {
            rrs.add(subject.next());
        }
        assertThat(rrs.size(), is(equalTo(1)));
        assertThat(rrs.get(0).getData(scn), is(equalTo("some data")));
        assertThat(rrs.get(0).columns().size(), is(equalTo(1)));
        verifyAll();
    }
}
