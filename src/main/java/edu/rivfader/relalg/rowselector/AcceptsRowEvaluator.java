package edu.rivfader.relalg.rowselector;

import edu.rivfader.relalg.IQualifiedNameRow;
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
