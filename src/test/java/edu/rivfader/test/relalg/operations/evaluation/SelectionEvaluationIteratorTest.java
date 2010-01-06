package edu.rivfader.test.relalg.operations.evaluation;

import edu.rivfader.relalg.representation.IQualifiedNameRow;
import edu.rivfader.relalg.representation.IRelAlgExpr;
import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.rowselector.representation.IRowSelector;
import edu.rivfader.relalg.operation.evaluation.SelectionEvaluationIterator;
import edu.rivfader.relalg.operation.evaluation.Evaluator;
import edu.rivfader.relalg.operations.RowSetStubResult;
import edu.rivfader.rowselector.operations.RowselectorStubResult;
import edu.rivfader.data.Database;

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

@RunWith(PowerMockRunner.class)
@PrepareForTest({Database.class})
public class SelectionEvaluationIteratorTest {
    @Test
    public void transformSelection() {
        Database db = createMock(Database.class);
        IQualifiedNameRow first = createMock(IQualifiedNameRow.class);
        IQualifiedNameRow second = createMock(IQualifiedNameRow.class);
        IQualifiedNameRow third = createMock(IQualifiedNameRow.class);

        IRowSelector predicate =
            new RowselectorStubResult<Boolean>(true, false, true);
        List<IQualifiedNameRow> previousRows =
            new LinkedList<IQualifiedNameRow>();
        previousRows.add(first);
        previousRows.add(second);
        previousRows.add(third);
        IRelAlgExpr subExpression = new RowSetStubResult(previousRows);
        Selection s = new Selection(predicate, subExpression);
        Evaluator e = new Evaluator(db);

        replayAll();
        SelectionEvaluationIterator subject =
            new SelectionEvaluationIterator(s, e);
        List<IQualifiedNameRow> gotRows = new LinkedList<IQualifiedNameRow>();
        while(subject.hasNext()) {
            gotRows.add(subject.next());
        }
        verifyAll();

        List<IQualifiedNameRow> expectedRows =
            new LinkedList<IQualifiedNameRow>();
        expectedRows.add(first);
        expectedRows.add(third);
        assertThat(gotRows, is(equalTo(expectedRows)));
    }
}
