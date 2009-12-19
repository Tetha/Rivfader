package edu.rivfader.profiling;

import edu.rivfader.relalg.Selection;
import edu.rivfader.relalg.Projection;
import edu.rivfader.relalg.Product;
import edu.rivfader.relalg.IRelAlgExpr;

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
