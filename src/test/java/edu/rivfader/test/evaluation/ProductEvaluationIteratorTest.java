package edu.rivfader.test.evaluation;

import edu.rivfader.evaluation.Evaluator;
import edu.rivfader.relalg.RowSetStubResult;
import edu.rivfader.relalg.QualifiedNameRow;
import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.Product;
import edu.rivfader.relalg.RowFactory;
import edu.rivfader.evaluation.ProductEvaluationIterator;

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
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.List;
import java.util.LinkedList;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Evaluator.class)
public class ProductEvaluationIteratorTest {
    @Test
    public void transformProductWorks() {
        List<IQualifiedNameRow> leftResult;
        RowFactory leftRows = new RowFactory(new String[] {"t", "c"});
        leftRows.addRow("cow");
        leftRows.addRow("more cow");
        leftResult = leftRows.getRows();

        List<IQualifiedNameRow> rightResult;
        RowFactory rightRows = new RowFactory(new String[] {"u", "d"});
        rightRows.addRow("chicken");
        rightRows.addRow("more chicken");
        rightResult = rightRows.getRows();

        RowSetStubResult right = new RowSetStubResult(rightResult);
        RowSetStubResult left = new RowSetStubResult(leftResult);

        Product p = new Product(left, right);
        Evaluator evaluator = createMock(Evaluator.class);
        expect(evaluator.transform(left))
            .andReturn(leftRows.getRows().iterator());
        expect(evaluator.transform(right))
            .andReturn(rightRows.getRows().iterator());
        expect(evaluator.transform(right))
            .andReturn(rightRows.getRows().iterator());

        replayAll();
        ProductEvaluationIterator subject = new ProductEvaluationIterator(
                            p, evaluator);

        List<IQualifiedNameRow> gotRows = new LinkedList<IQualifiedNameRow>();
        while(subject.hasNext()) {
            gotRows.add(subject.next());
        }

        RowFactory expectedRows = new RowFactory();
        expectedRows.addRow(new QualifiedNameRow(leftResult.get(0),
                                                 rightResult.get(0)));
        expectedRows.addRow(new QualifiedNameRow(leftResult.get(0),
                                                 rightResult.get(1)));
        expectedRows.addRow(new QualifiedNameRow(leftResult.get(1),
                                                 rightResult.get(0)));
        expectedRows.addRow(new QualifiedNameRow(leftResult.get(1),
                                                 rightResult.get(1)));

        assertThat(gotRows, is(equalTo(expectedRows.getRows())));
        verifyAll();
    }
}
