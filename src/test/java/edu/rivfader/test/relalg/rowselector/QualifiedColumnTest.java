package edu.rivfader.test.relalg.rowselector;

import edu.rivfader.relalg.rowselector.QualifiedColumn;
import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.relalg.IQualifiedNameRow;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

import static org.easymock.EasyMock.expect;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

@RunWith(PowerMockRunner.class)
public class QualifiedColumnTest {
    @Test public void valueIsRetrieved() {
        String ev; // expected value
        QualifiedColumn s; // subject
        IQualifiedColumnName qcn; // retrieved qualified name
        IQualifiedNameRow ir; // input row

        ev = "foo";
        ir = createMock(IQualifiedNameRow.class);
        qcn = createMock(IQualifiedColumnName.class);
        expect(ir.getData(qcn)).andReturn(ev);

        replayAll();
        s = new QualifiedColumn(qcn);
        assertThat(ev, is(equalTo(s.getValue(ir))));
        verifyAll();
    }
}
