package edu.rivfader.relalg;

import edu.rivfader.data.Database;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This class implements the cartesian product of two relational algebra
 * expressions.
 * @author harald
 */
public class Product implements IRelAlgExpr {
    /**
     * contains the lhs.
     */
    private IRelAlgExpr left;
    /**
     * contains the rhs.
     */
    private IRelAlgExpr right;

    /**
     * Constructs a new product node.
     * @param pLeft the left subexpression
     * @param pRight the right subexpression
     */
    public Product(final IRelAlgExpr pLeft, final IRelAlgExpr pRight) {
        left = pLeft;
        right = pRight;
    }

    public IRelAlgExpr getLeft() {
        return left;
    }

    public IRelAlgExpr getRight() {
        return right;
    }
}
