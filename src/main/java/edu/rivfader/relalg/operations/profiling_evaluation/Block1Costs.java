package edu.rivfader.relalg.operations.profiling_evaluation;

import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.relalg.representation.Projection;
import edu.rivfader.relalg.representation.Product;
import edu.rivfader.relalg.representation.IRelAlgExpr;

import java.util.Set;
import java.util.HashSet;
public class Block1Costs implements ICostAccumulator {
    private int costs;
    private Set<IRelAlgExpr> countedNodes;

    public Block1Costs() {
        costs = 0;
        countedNodes = new HashSet<IRelAlgExpr>();
    }

    @Override
    public void handleSelectionStatistics(Selection actingNode,
                                   int outputRows,
                                   int columns) {
        if (countedNodes.contains(actingNode)) {
            return;
        } else {
            costs += outputRows * columns;
            countedNodes.add(actingNode);
        }
    }

    @Override
    public void handleProjectionStatistics(Projection actingNode,
                                    int inputRows,
                                    int columns) {
        if (countedNodes.contains(actingNode)) {
            return;
        } else {
            costs += actingNode.getSelectedFields().size() * inputRows;
            countedNodes.add(actingNode);
        }
    }

    @Override
    public void handleProductStatistics(Product actingNode,
                                 int rows,
                                 int columns) {
        if (countedNodes.contains(actingNode)) {
            return;
        } else {
            costs += rows * columns;
            countedNodes.add(actingNode);
        }
    }

    @Override
    public int getCost() {
        return costs;
    }
}
