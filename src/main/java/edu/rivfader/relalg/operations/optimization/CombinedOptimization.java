package edu.rivfader.relalg.operations.optimization;

import edu.rivfader.relalg.representation.IRelAlgExpr;

import java.util.Collection;

public class CombinedOptimization implements IOptimization {
    private Collection<IOptimization> combinedOptimizations;

    public CombinedOptimization(
            Collection<IOptimization> pCombinedOptimizations) {
        combinedOptimizations = pCombinedOptimizations;
    }

    public boolean wasTrivial() {
        boolean allTrivial = true;
        for (IOptimization currentOptimization : combinedOptimizations) {
            allTrivial = allTrivial && currentOptimization.wasTrivial();
        }
        return allTrivial;
    }

    public IRelAlgExpr optimize(IRelAlgExpr input) {
        IRelAlgExpr currentTree = input;
        for (IOptimization currentOptimization : combinedOptimizations) {
            currentTree = currentOptimization.optimize(currentTree);
        }
        return currentTree;
    }
}
