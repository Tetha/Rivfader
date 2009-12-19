package edu.rivfader.optimizations;

import edu.rivfader.relalg.IRelAlgExpr;
import edu.rivfader.relalg.IRelAlgExprTransformation;
import edu.rivfader.relalg.BaseRelalgTransformation;
import edu.rivfader.relalg.Selection;
import edu.rivfader.relalg.rowselector.IRowSelector;
import edu.rivfader.relalg.rowselector.BinaryOperation;
import edu.rivfader.relalg.rowselector.BooleanValueCombination;

import edu.rivfader.relalg.Projection;
import edu.rivfader.relalg.Product;
import edu.rivfader.relalg.LoadTable;
import edu.rivfader.relalg.RenameTable;

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
