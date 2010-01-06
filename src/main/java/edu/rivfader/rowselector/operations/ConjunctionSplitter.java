package edu.rivfader.rowselector.operations;

import edu.rivfader.rowselector.representation.IRowSelector;
import edu.rivfader.rowselector.representation.Comparision;
import edu.rivfader.rowselector.representation.Always;
import edu.rivfader.rowselector.operations.RowselectorStubResult;
import edu.rivfader.rowselector.representation.BinaryOperation;
import edu.rivfader.rowselector.representation.BooleanValueCombination;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;

public class ConjunctionSplitter
    extends BaseRowSelectorTransformation<Collection<IRowSelector>> {

    protected Collection<IRowSelector> transformBinaryOperation(BinaryOperation castedR) {
        List<IRowSelector> result = new LinkedList<IRowSelector>();
        if (castedR.getCombination() == BooleanValueCombination.AND) {
            result.addAll(transform(castedR.getLeft()));
            result.addAll(transform(castedR.getRight()));
        } else {
            result.add(castedR);
        }
        return result;
    }

    protected Collection<IRowSelector> transformComparision(Comparision r) {
        List<IRowSelector> result = new LinkedList<IRowSelector>();
        result.add(r);
        return result;
    }

    protected Collection<IRowSelector> transformAlways(Always r) {
        List<IRowSelector> result = new LinkedList<IRowSelector>();
        result.add(r);
        return result;
    }
}
