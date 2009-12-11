package edu.rivfader.relalg.rowselector;

import edu.rivfader.relalg.IQualifiedNameRow;

/**
 * This class implements the use of literals in a Comparision.
 * @author harald
 */
public class Literal implements IValueProvider {
    /**
     * contains the literal value.
     */
    private String literalValue;

    /**
     * creates a new literal.
     * @param pLiteralValue the value of the literal
     */
    public Literal(final String pLiteralValue) {
        literalValue = pLiteralValue;
    }

    @Override
    public String getValue(final IQualifiedNameRow data) {
        return literalValue;
    }
}
