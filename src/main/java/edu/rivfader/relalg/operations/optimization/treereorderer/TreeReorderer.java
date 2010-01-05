package edu.rivfader.relalg.operations.optimization.treereorderer;

import edu.rivfader.relalg.operations.optimization.IOptimization;
import edu.rivfader.relalg.operations.BaseRelalgTransformation;
import edu.rivfader.relalg.representation.IRelAlgExpr;
import edu.rivfader.relalg.operations.IRelAlgExprTransformation;
import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.relalg.representation.Projection;
import edu.rivfader.relalg.representation.Product;
import edu.rivfader.relalg.representation.LoadTable;
import edu.rivfader.relalg.representation.RenameTable;

import static edu.rivfader.relalg.operations.optimization.treereorderer.IReorderingScheme.ActionAlongUnaryOperation;
/**
 * This tree reorderer moves operations down a tree. The metaphor behind
 * this tree restructuring is looking at a wall, picking a brick, prying
 * it off the wall and dropping it down (acceptsProjection or
 * acceptSelection returns true). While the brick is falling, we
 * are constantly checking if the brick keeps falling or stops falling
 * (which are MOVE_DOWN, TRIVIAL_MOVE_DOWN, MOVE_LEFT and MOVE_RIGHT)
 * or if the brick stops falling for some reason.
 * The tree reorderer will call the reordering trivial if no node in
 * the tree was ever accepted or if all operations were trivial moves.
 * If a single move was nontrivial, the tree reorderer considers the
 * overall tree restructuring nontrivial.
 */
public class TreeReorderer
    extends BaseRelalgTransformation<IRelAlgExpr>
    implements IRelAlgExprTransformation<IRelAlgExpr>, IOptimization {

    private IReorderingScheme scheme;
    private TreeReordererState state;

    public TreeReorderer(IReorderingScheme pScheme) {
        scheme = pScheme;
    }

    public void setState(TreeReordererState newState) {
        state = newState;
    }

    @Override
    public IRelAlgExpr optimize(IRelAlgExpr e) {
        state = new SearchingAcceptedNodeState(this, scheme);
        return transform(e);
    }

    @Override
    public boolean wasTrivial() {
        return state.wasTrivial();
    }

    public void makeNonTrivial() {
        state.makeNonTrivial();
    }

    @Override
    public IRelAlgExpr transformSelection(Selection s) {
        return state.transformSelection(s);
    }

    @Override
    public IRelAlgExpr transformProjection(Projection p) {
        return state.transformProjection(p);
    }

    @Override
    public IRelAlgExpr transformProduct(Product p) {
        return state.transformProduct(p);
    }

    @Override
    public IRelAlgExpr transformLoadTable(LoadTable l) {
        return state.transformLoadTable(l);
    }

    @Override
    public IRelAlgExpr transformRenameTable(RenameTable r) {
        return state.transformRenameTable(r);
    }
}
