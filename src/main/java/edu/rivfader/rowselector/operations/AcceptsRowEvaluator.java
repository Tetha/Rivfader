package edu.rivfader.rowselector.operations;

import edu.rivfader.rowselector.representation.IRowSelector;
import edu.rivfader.rowselector.representation.BinaryOperation;
import edu.rivfader.rowselector.representation.Comparision;
import edu.rivfader.rowselector.representation.Always;
import edu.rivfader.rowselector.operations.RowselectorStubResult;

import edu.rivfader.relalg.representation.IQualifiedNameRow;
public class AcceptsRowEvaluator
    extends BaseRowSelectorTransformation<Boolean> {
    IQualifiedNameRow data;

    public AcceptsRowEvaluator(IQualifiedNameRow pData) {
        data = pData;
    }

    protected Boolean transformBinaryOperation(BinaryOperation bo) {
        return bo.getCombination().combineValues(
                                    transform(bo.getLeft()),
                                    transform(bo.getRight()));
    }

    protected Boolean transformComparision(Comparision c) {
        String actualFirstValue = c.getLeft().getValue(data);
        String actualSecondValue = c.getRight().getValue(data);
        return c.getComparision().isGoodValuePair(actualFirstValue,
                                                       actualSecondValue);
    }

    protected Boolean transformAlways(Always a) {
        return true;
    }
}
