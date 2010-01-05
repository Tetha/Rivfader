package edu.rivfader.rowselector.operations;

import edu.rivfader.rowselector.representation.IRowSelector;
import edu.rivfader.rowselector.representation.BinaryOperation;
import edu.rivfader.rowselector.representation.Comparision;
import edu.rivfader.rowselector.representation.Always;
import edu.rivfader.rowselector.representation.StubResult;

import edu.rivfader.relalg.representation.IQualifiedNameRow;
public class AcceptsRowEvaluator {
    IQualifiedNameRow data;

    public AcceptsRowEvaluator(IQualifiedNameRow pData) {
        data = pData;
    }

    public boolean evaluate(IRowSelector source) {
        if (source instanceof BinaryOperation) {
            BinaryOperation bo = (BinaryOperation) source;
            return bo.getCombination().combineValues(
                                        evaluate(bo.getLeft()),
                                        evaluate(bo.getRight()));
        } else if(source instanceof Comparision) {
            Comparision c = (Comparision) source;
            String actualFirstValue = c.getLeft().getValue(data);
            String actualSecondValue = c.getRight().getValue(data);
            return c.getComparision().isGoodValuePair(actualFirstValue,
                                                           actualSecondValue);
        } else if (source instanceof Always) {
            return true;
        } else if (source instanceof StubResult) {
            StubResult s = (StubResult) source;
            return s.getResult();
        }

        throw new RuntimeException("Unexpected parameter " + source);
    }
}
