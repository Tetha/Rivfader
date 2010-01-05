package edu.rivfader.test.relalg.representation;

import edu.rivfader.relalg.representation.IColumnProjection;
import edu.rivfader.relalg.representation.IQualifiedNameRow;
import edu.rivfader.relalg.representation.IQualifiedColumnName;
import edu.rivfader.relalg.representation.QualifiedColumnSelector;
import java.util.Collection;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

public class QualifiedColumnSelectorTest {
    @Test public void checkProjection() {
        IColumnProjection s; // subject
        IQualifiedNameRow pr; // projected row
        Collection<IQualifiedColumnName> r; // result
        IQualifiedColumnName cn; // column name

        cn = createMock(IQualifiedColumnName.class);
        pr = createMock(IQualifiedNameRow.class);
        s = new QualifiedColumnSelector(cn);
        replayAll();
        r = s.project(pr);
        assertThat(r.size(), is(equalTo(1)));
        assertThat(r, hasItem(cn));
        verifyAll();
    }
}
