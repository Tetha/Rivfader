package edu.rivfader.optimizations;

import edu.rivfader.relalg.IRelAlgExpr;
import edu.rivfader.relalg.Projection;
import edu.rivfader.relalg.Selection;

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
