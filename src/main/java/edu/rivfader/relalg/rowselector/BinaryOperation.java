package edu.rivfader.relalg.rowselector;

import edu.rivfader.relalg.IQualifiedNameRow;

/**
 * This class is a binary operation on IRowSelectors.
 *
 * Examples of such operations are and and or.
 * @author harald
 */
public class BinaryOperation implements IRowSelector {
    /**
     * contains the left hand side of the operation.
     */
    private IRowSelector left;
    /**
     * contains the operation to combine both values.
     */
    private BooleanValueCombination combination;
    /**
     * contains the right hand side of the operation.
     */
    private IRowSelector right;

    /**
     * this constructs a new BinaryOperation.
     * @param pLeft the left hand side of the operation
     * @param pCombination the function to combine the values
     * @param pRight the right hand side of the operation
     */
    public BinaryOperation(final IRowSelector pLeft,
                           final BooleanValueCombination pCombination,
                           final IRowSelector pRight) {
        left = pLeft;
        right = pRight;
        combination = pCombination;
    }

    public IRowSelector getLeft() {
        return left;
    }

    public IRowSelector getRight() {
        return right;
    }

    public BooleanValueCombination getCombination() {
        return combination;
    }
}
