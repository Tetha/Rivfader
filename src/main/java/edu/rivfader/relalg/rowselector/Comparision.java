package edu.rivfader.relalg.rowselector;

import edu.rivfader.data.Row;

public class Comparision implements RowSelector {
    /**
     * Contains the comparision mode.
     */
    private ValueComparer internalComparision;

    /**
     * contains the first operand to compare.
     */
    private ValueProvider firstOperand;

    /**
     * contains the second operand to compare.
     */
    private ValueProvider secondOperand;

    /**
     * Constructs a new comparision node.
     * @param pFirstOperand the first operand to compare
     * @param pSecondOperand the second operand to compare
     * @param pInternalComparision the way of comparing values
     */
    public Comparision(final ValueProvider pFirstOperand,
                       final ValueProvider pSecondOperand,
                       final ValueComparer pInternalComparision) {
        internalComparision = pInternalComparision;
        firstOperand = pFirstOperand;
        secondOperand = pSecondOperand;
    }


    @Override
    public boolean acceptsRow(final Row data) {
        String actualFirstValue = firstOperand.getValue(data);
        String actualSecondValue = secondOperand.getValue(data);
        return internalComparision.isGoodValuePair(actualFirstValue,
                                                   actualSecondValue);
    }
}
