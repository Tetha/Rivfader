package edu.rivfader.test.profiling;

import edu.rivfader.profiling.ProductStatisticsIterator;
import edu.rivfader.profiling.ICountingIterator;
import edu.rivfader.profiling.ICostAccumulator;

import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.RowFactory;
import edu.rivfader.relalg.Product;

import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Product.class)
public class ProductStatisticsIteratorTest {
    private interface RowCountingIterator
            extends ICountingIterator<IQualifiedNameRow> {
    }

    @Test public void evaluateWorks() {
        int leftCount = 2;
        int rightCount = 3;
        ICountingIterator<IQualifiedNameRow> leftInput =
            createMock(RowCountingIterator.class);
        ICountingIterator<IQualifiedNameRow> rightInput =
            createMock(RowCountingIterator.class);
        ICostAccumulator statisticsDestination =
            createMock(ICostAccumulator.class);
        Product activeNode;

        activeNode = createMock(Product.class);
        expect(leftInput.getNumberOfElements()).andReturn(leftCount);
        expect(rightInput.getNumberOfElements()).andReturn(rightCount);

        RowFactory productRows = new RowFactory(new String[] {"t", "c"});
        productRows.addRow("d1");
        productRows.addRow("d2");
        Iterator<IQualifiedNameRow> productOutput = productRows.getRows().iterator();
        statisticsDestination.handleProductStatistics(activeNode,
                                                    leftCount, rightCount,
                                                    1);

        replayAll();
        ProductStatisticsIterator subject = new ProductStatisticsIterator(
                    activeNode, statisticsDestination,
                    leftInput, rightInput, productOutput);
        List<IQualifiedNameRow> subjectOutput =
            new LinkedList<IQualifiedNameRow>();
        while(subject.hasNext()) {
            subjectOutput.add(subject.next());
        }
        verifyAll();
        assertThat(subjectOutput, is(equalTo(productRows.getRows())));
    }
}
