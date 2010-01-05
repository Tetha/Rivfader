package edu.rivfader.test.relalg.operations.evaluation;

import edu.rivfader.relalg.representation.IQualifiedNameRow;
import edu.rivfader.relalg.operations.RowFactory;
import edu.rivfader.relalg.operation.evaluation.RenameTableEvaluationIterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

import org.powermock.modules.junit4.PowerMockRunner;

import java.util.LinkedList;
import java.util.Collection;

@RunWith(PowerMockRunner.class)
public class RenameTableEvaluationIteratorTest {
    @Test public void transformRenameTable() {
        String nn; // new name
        Collection<IQualifiedNameRow> rrs; // result rows
        RenameTableEvaluationIterator s;

        RowFactory srs; // source rows
        RowFactory trs; // transformed rows

        nn = "nn";

        srs = new RowFactory(new String[] {"on", "c1"},
                             new String[] {"on", "c2"});
        srs.addRow("d11", "d12");
        srs.addRow("d21", "d22");

        trs = new RowFactory(new String[] {"nn", "c1"},
                             new String[] {"nn", "c2"});
        trs.addRow("d11", "d12");
        trs.addRow("d21", "d22");

        s = new RenameTableEvaluationIterator(srs.getRows().iterator(), nn);
        rrs = new LinkedList<IQualifiedNameRow>();
        while(s.hasNext()) {
            rrs.add(s.next());
        }

        assertThat(new LinkedList<IQualifiedNameRow>(rrs),
                   is(equalTo(trs.getRows())));
    }
}
