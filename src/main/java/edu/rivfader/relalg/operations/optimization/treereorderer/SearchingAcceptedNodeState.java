package edu.rivfader.relalg.operations.optimization.treereorderer;

import edu.rivfader.relalg.representation.IRelAlgExpr;
import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.relalg.representation.Projection;
import edu.rivfader.relalg.representation.Product;

public class SearchingAcceptedNodeState extends TreeReordererState {
    public SearchingAcceptedNodeState(TreeReorderer context,
                                      IReorderingScheme scheme) {
        super(context, scheme);
    }

    @Override
    public IRelAlgExpr transformSelection(Selection s) {
        if (scheme.acceptsSelection(s)) {
            context.setState(new MovingSelectionState(context, scheme, s));
            return context.transform(s.getSubExpression());
        } else {
            return new Selection(s.getPredicate(),
                            context.transform(s.getSubExpression()));
        }
    }

    @Override
    public IRelAlgExpr transformProjection(Projection p) {
        if (scheme.acceptsProjection(p)) {
            context.setState(new MovingProjectionState(context, scheme, p));
            return context.transform(p.getSubExpression());
        } else {
            return new Projection(context.transform(p.getSubExpression()),
                                  p.getSelectedFields());
        }
    }

    @Override
    public IRelAlgExpr transformProduct(Product p) {
        return new Product(context.transform(p.getLeft()),
                           context.transform(p.getRight()));
    }

    @Override
    public IRelAlgExpr insertAcceptedNode(IRelAlgExpr r) {
        return r;
    }
}
