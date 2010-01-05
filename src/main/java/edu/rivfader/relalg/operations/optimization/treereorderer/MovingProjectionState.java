package edu.rivfader.relalg.operations.optimization.treereorderer;

import edu.rivfader.relalg.representation.IRelAlgExpr;
import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.relalg.representation.Projection;

public class MovingProjectionState extends TreeReordererState {
    private Projection acceptedProjection;
    public MovingProjectionState(TreeReorderer context,
                                 IReorderingScheme scheme,
                                 Projection p) {
        super(context, scheme);
        acceptedProjection = p;
    }

    @Override
    protected IRelAlgExpr insertAcceptedNode(IRelAlgExpr subExpression) {
        return new Projection(subExpression,
                              acceptedProjection.getSelectedFields());
    }
}
