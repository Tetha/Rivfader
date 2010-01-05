package edu.rivfader.relalg.operations.optimization.treereorderer;

import edu.rivfader.relalg.representation.IRelAlgExpr;
import edu.rivfader.relalg.representation.Projection;
import edu.rivfader.relalg.representation.Selection;

public class MovingSelectionState extends TreeReordererState {
    private Selection acceptedSelection;
    public MovingSelectionState(TreeReorderer context,
                                IReorderingScheme scheme,
                                Selection pAcceptedSelection) {
        super(context, scheme);
        acceptedSelection = pAcceptedSelection;
    }

    @Override
    protected IRelAlgExpr insertAcceptedNode(IRelAlgExpr subExpression) {
        return new Selection(acceptedSelection.getPredicate(), subExpression);
    }
}
