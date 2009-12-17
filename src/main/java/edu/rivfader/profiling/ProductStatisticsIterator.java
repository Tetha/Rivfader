package edu.rivfader.profiling;

import edu.rivfader.relalg.Product;
import edu.rivfader.relalg.IQualifiedNameRow;
import java.util.Iterator;

public class ProductStatisticsIterator extends StatisticsIterator {
    private Product activeNode;
    private ICountingIterator leftInput;
    private ICountingIterator rightInput;

    public ProductStatisticsIterator(Product pActiveNode,
                            ICostAccumulator statisticsDestination,
                            ICountingIterator<IQualifiedNameRow> pLeftInput,
                            ICountingIterator<IQualifiedNameRow> pRightInput,
                            Iterator<IQualifiedNameRow> productOutput) {
        super(productOutput, statisticsDestination);
        leftInput = pLeftInput;
        rightInput = pRightInput;
        activeNode = pActiveNode;
    }

    protected void announceStatistics() {
        statisticsDestination.handleProductStatistics(activeNode,
                                            leftInput.getNumberOfElements(),
                                            rightInput.getNumberOfElements(),
                                            columns);
    }
}
