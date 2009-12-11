package edu.rivfader.test.relalg;

import edu.rivfader.relalg.UnqualifiedColumnSelector;
import edu.rivfader.relalg.IColumnProjection;
import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.relalg.QualifiedColumnName;
import edu.rivfader.relalg.IQualifiedNameRow;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collection;
/**
 * Contains tests for the Unqualified column selector.
 * @author harald
 */
@RunWith(PowerMockRunner.class)
public class UnqualifiedColumnSelectorTest {
    /**
     * checks that the unqualified column name is looked up and the
     * qualified column name is returned.
     */
    @Test public void testProjection() {
        String cn = "c"; // column name
        IColumnProjection s = new UnqualifiedColumnSelector("c"); // subject
        // projected row
        IQualifiedNameRow pr = createMock(IQualifiedNameRow.class);

        expect(pr.resolveUnqualifiedName("c"))
            .andReturn(new QualifiedColumnName("t", "c"));

        replayAll();
        Collection<IQualifiedColumnName> r = s.project(pr); // result
        assertThat(r.size(), is(equalTo(1)));
        verifyAll();
        assertThat(r,
                hasItem((IQualifiedColumnName)new QualifiedColumnName("t", "c")));
    }
}
