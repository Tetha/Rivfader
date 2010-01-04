package edu.rivfader.optimizations;

import edu.rivfader.relalg.IRelAlgExpr;
import edu.rivfader.relalg.Selection;
import edu.rivfader.relalg.Projection;
import edu.rivfader.relalg.Product;
import edu.rivfader.relalg.LoadTable;
import edu.rivfader.relalg.RenameTable;

import static edu.rivfader.optimizations.IReorderingScheme.ActionAlongUnaryOperation;
import static edu.rivfader.optimizations.IReorderingScheme.ActionAlongBinaryOperation;

public abstract class TreeReordererState {
    private boolean wasTrivial;
    protected TreeReorderer context;
    protected IReorderingScheme scheme;

    public TreeReordererState(TreeReorderer pContext,
                              IReorderingScheme pScheme) {
        wasTrivial = true;
        context = pContext;
        scheme = pScheme;
    }

    public boolean wasTrivial() {
        return wasTrivial;
    }

    public void makeNonTrivial() {
        wasTrivial = false;
    }

    public IRelAlgExpr transformSelection(Selection s) {
        switch (scheme.moveAlongSelection(s)) {
            case MOVE_DOWN:
                makeNonTrivial();
                /* fall through */
            case TRIVIAL_MOVE_DOWN:
                return new Selection(s.getPredicate(),
                                context.transform(s.getSubExpression()));
            case NO_OPERATION:
                return insertAcceptedNode(s);

            default:
                    throw new RuntimeException("unexpected enumeration value");
        }
    }

    public IRelAlgExpr transformProjection(Projection p) {
        switch (scheme.moveAlongProjection(p)) {
            case MOVE_DOWN:
                makeNonTrivial();
                /* fall through */
            case TRIVIAL_MOVE_DOWN:
                return new Projection(context.transform(p.getSubExpression()),
                                      p.getSelectedFields());
            case NO_OPERATION:
                return insertAcceptedNode(p);

            default:
                    throw new RuntimeException("Unexpected enumeration value");
        }
    }

    public IRelAlgExpr transformProduct(Product p) {
        switch(scheme.moveAlongProduct(p)) {
            case NO_OPERATION:
                return insertAcceptedNode(p);

            case MOVE_LEFT:
                return new Product(context.transform(p.getLeft()),
                                    p.getRight());

            case MOVE_RIGHT:
                return new Product(p.getLeft(),
                                   context.transform(p.getRight()));

            default:
                throw new RuntimeException("Unexpeted enumeration value");
        }
    }

    public IRelAlgExpr transformLoadTable(LoadTable l) {
        return insertAcceptedNode(l);
    }

    public IRelAlgExpr transformRenameTable(RenameTable r) {
        return insertAcceptedNode(r);
    }

    protected abstract IRelAlgExpr insertAcceptedNode(IRelAlgExpr subExpression);
}
