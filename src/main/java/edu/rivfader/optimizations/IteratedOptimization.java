package edu.rivfader.optimizations;

import edu.rivfader.relalg.IRelAlgExpr;

public class IteratedOptimization implements IOptimization {
    private IOptimization internalOptimization;
    private int nontrivialPassesCount;

    public IteratedOptimization(IOptimization iteratedOptimization) {
        internalOptimization = iteratedOptimization;
        nontrivialPassesCount = 0;
    }

    public boolean wasTrivial() {
        return nontrivialPassesCount == 0;
    }

    public IRelAlgExpr optimize(IRelAlgExpr input) {
        IRelAlgExpr currentTree = input;
        do {
            currentTree = internalOptimization.optimize(currentTree);
            nontrivialPassesCount++;
        } while(!internalOptimization.wasTrivial());
        nontrivialPassesCount--; // the last pass was trivial.
        return currentTree;
    }
}
