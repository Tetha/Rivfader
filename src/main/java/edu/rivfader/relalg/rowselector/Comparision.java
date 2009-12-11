package edu.rivfader.relalg.rowselector;

import edu.rivfader.relalg.IQualifiedNameRow;

/**
 * This implements a comparision of values.
 * @author harald
 */
public class Comparision implements IRowSelector {
    /**
     * Contains the comparision mode.
     */
    private ValueComparer internalComparision;

    /**
     * contains the first operand to compare.
     */
    private IValueProvider firstOperand;

    /**
     * contains the second operand to compare.
     */
    private IValueProvider secondOperand;

    /**
     * Constructs a new comparision node.
     * @param pFirstOperand the first operand to compare
     * @param pSecondOperand the second operand to compare
     * @param pInternalComparision the way of comparing values
     */
    public Comparision(final IValueProvider pFirstOperand,
                       final IValueProvider pSecondOperand,
                       final ValueComparer pInternalComparision) {
        internalComparision = pInternalComparision;
        firstOperand = pFirstOperand;
        secondOperand = pSecondOperand;
    }


    @Override
    public boolean acceptsRow(final IQualifiedNameRow data) {
        String actualFirstValue = firstOperand.getValue(data);
        String actualSecondValue = secondOperand.getValue(data);
        return internalComparision.isGoodValuePair(actualFirstValue,
                                                   actualSecondValue);
    }
}
