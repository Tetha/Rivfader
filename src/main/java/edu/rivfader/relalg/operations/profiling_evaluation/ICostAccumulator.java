package edu.rivfader.relalg.operations.profiling_evaluation;

import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.relalg.representation.Projection;
import edu.rivfader.relalg.representation.Product;

public interface ICostAccumulator {
    /**
     * computes and stores costs for the selection.
     */
    void handleSelectionStatistics(Selection actingNode,
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
