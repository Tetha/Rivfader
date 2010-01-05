package edu.rivfader.relalg.operations.optimization;

import edu.rivfader.relalg.representation.IRelAlgExpr;

public interface IOptimization {
    /**
     * This performs some optimization on the tree.
     */
    IRelAlgExpr optimize(IRelAlgExpr tree);

    /**
     * This returns true if the optimization was _trivial_.
     * The basic assumption is that an optimization does a
     * certain amount of interesting, non-trivial steps
     * until it reaches a fixed point and then performs a
     * potentially infinite sequence of trivial steps.
     * e.g. permuting selections is a trivial reordering,
     * while swapping a product with a selection is not trivial.
     */
    boolean wasTrivial();
}
