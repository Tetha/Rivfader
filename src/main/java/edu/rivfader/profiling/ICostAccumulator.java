package edu.rivfader.profiling;

import edu.rivfader.relalg.Selection;
import edu.rivfader.relalg.Projection;
import edu.rivfader.relalg.Product;

public interface ICostAccumulator {
    /**
     * computes and stores costs for the selection.
     */
    void handleSelectionStatistics(Selection actingNode,
                                   int inputRows,
                                   int outputRows,
                                   int columns);

    /**
     * computes and stores costs for the projection
     */
    void handleProjectionStatistics(Projection actingNode,
                                    int inputRows,
                                    int columns);

    /**
     * computes and stores costs for the product.
     */
    void handleProductStatistics(Product actingNode,
                                 int rowCount,
                                 int columns);

    /**
     * returns the overall costs.
     */
    int getCost();
}
