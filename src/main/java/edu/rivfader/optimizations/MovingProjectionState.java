package edu.rivfader.optimizations;

import edu.rivfader.relalg.IRelAlgExpr;
import edu.rivfader.relalg.Selection;
import edu.rivfader.relalg.Projection;

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
