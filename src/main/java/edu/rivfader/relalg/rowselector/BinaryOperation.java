package edu.rivfader.relalg.rowselector;

import edu.rivfader.data.Row;

/**
 * This class is a binary operation on RowSelectors.
 *
 * Examples of such operations are and and or.
 */
public class BinaryOperation implements RowSelector {
    /**
     * contains the left hand side of the operation.
     */
    private RowSelector left;
    /**
     * contains the operation to combine both values.
     */
    private BooleanValueCombination combination;
    /**
     * contains the right hand side of the operation.
     */
    private RowSelector right;

    /**
     * this constructs a new BinaryOperation.
     * @param pLeft the left hand side of the operation
     * @param pCombination the function to combine the values
     * @param pRight the right hand side of the operation
     */
    public BinaryOperation(final RowSelector pLeft,
                           final BooleanValueCombination pCombination,
                           final RowSelector pRight) {
        left = pLeft;
        right = pRight;
        combination = pCombination;
    }

    @Override
    public boolean acceptsRow(final Row data) {
        return combination.combineValues(left.acceptsRow(data),
                                         right.acceptsRow(data));
    }
}
