package edu.rivfader.test.profiling;

import edu.rivfader.relalg.Product;
import edu.rivfader.relalg.Selection;
import edu.rivfader.relalg.Projection;
import edu.rivfader.relalg.IColumnProjection;
import edu.rivfader.profiling.ICostAccumulator;
import edu.rivfader.profiling.Block1Costs;
import java.util.Collection;

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

@RunWith(PowerMockRunner.class)
@PrepareForTest({Product.class, Selection.class, Projection.class})
public class Block1CostsTests {
    private interface ProjectionCollection
            extends Collection<IColumnProjection> {
    }
    @Test public void checkSelectionCalculation() {
        int result;
        Selection activeNode = createMock(Selection.class);
        int outcomingRowCount = 5;
        int columns = 3;
        ICostAccumulator subject = new Block1Costs();
        subject.handleSelectionStatistics(activeNode,
                                          outcomingRowCount,
                                          columns);
        assertThat(subject.getCost(), is(equalTo(outcomingRowCount*columns)));
    }

    @Test public void checkProjectionCalculation() {
        int result;
        Projection activeNode;
        int ingoingRows = 10;
        int columns = 5;
        int projectionCount = 3;
        Collection<IColumnProjection> selectedFields;
        ICostAccumulator subject;

        activeNode = createMock(Projection.class);
        selectedFields = createMock(ProjectionCollection.class);
        expect(activeNode.getSelectedFields()).andReturn(selectedFields);
        expect(selectedFields.size()).andReturn(projectionCount);

        subject = new Block1Costs();
        replayAll();
        subject.handleProjectionStatistics(activeNode, ingoingRows, columns);
        verifyAll();
        assertThat(subject.getCost(), is(equalTo(projectionCount*ingoingRows)));
    }

    @Test public void checkProductCalculation() {
        ICostAccumulator subject;
        Product activeNode;
        int noRows = 100;
        int columns = 5;

        activeNode = createMock(Product.class);

        subject = new Block1Costs();
        replayAll();
        subject.handleProductStatistics(activeNode,
                                 noRows,
                                 columns);
        verifyAll();
        assertThat(subject.getCost(), is(equalTo(noRows*columns)));
    }

    @Test public void checkCostsAddedTogether() {
        int result;
        Selection activeNode = createMock(Selection.class);
        int outcomingRowCount = 5;
        int columns = 3;
        ICostAccumulator subject = new Block1Costs();
        subject.handleSelectionStatistics(activeNode,
                                          outcomingRowCount, columns);
        subject.handleSelectionStatistics(activeNode,
                                          outcomingRowCount, columns);
        assertThat(subject.getCost(), is(equalTo(2*outcomingRowCount*columns)));
    }
}
