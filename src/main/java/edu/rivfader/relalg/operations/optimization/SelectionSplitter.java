package edu.rivfader.relalg.operations.optimization;

import edu.rivfader.relalg.representation.IRelAlgExpr;
import edu.rivfader.relalg.operations.IRelAlgExprTransformation;
import edu.rivfader.relalg.operations.BaseRelalgTransformation;
import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.rowselector.representation.IRowSelector;
import edu.rivfader.rowselector.representation.BinaryOperation;
import edu.rivfader.rowselector.representation.BooleanValueCombination;

import edu.rivfader.relalg.representation.Projection;
import edu.rivfader.relalg.representation.Product;
import edu.rivfader.relalg.representation.LoadTable;
import edu.rivfader.relalg.representation.RenameTable;

import java.util.List;
import java.util.LinkedList;

public class SelectionSplitter
    extends BaseRelalgTransformation<IRelAlgExpr>
    implements IRelAlgExprTransformation<IRelAlgExpr> {


    private List<IRowSelector> collectPredicates(IRowSelector s) {
        List<IRowSelector> result = new LinkedList<IRowSelector>();
        if (s instanceof BinaryOperation) {
            BinaryOperation castedS = (BinaryOperation) s;
            if (castedS.getCombination() == BooleanValueCombination.AND) {
                result.addAll(collectPredicates(castedS.getLeft()));
                result.addAll(collectPredicates(castedS.getRight()));
            } else {
                result.add(s);
            }
        } else {
            result.add(s);
        }
        return result;
    }

    @Override
    public IRelAlgExpr transformSelection(Selection s) {
        IRelAlgExpr result = transform(s.getSubExpression());

        for(IRowSelector predicate : collectPredicates(s.getPredicate())) {
            result = new Selection(predicate, result);
        }

        return result;
    }

    @Override
    public IRelAlgExpr transformProjection(Projection p) {
        return new Projection(transform(p.getSubExpression()),
                              p.getSelectedFields());
    }

    @Override
    public IRelAlgExpr transformProduct(Product p) {
        return new Product(transform(p.getLeft()),
                           transform(p.getRight()));
    }

    @Override
    public IRelAlgExpr transformLoadTable(LoadTable l) {
        return l;
    }

    @Override
    public IRelAlgExpr transformRenameTable(RenameTable r) {
        return r;
    }
}
