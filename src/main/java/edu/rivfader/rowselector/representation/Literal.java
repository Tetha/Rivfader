package edu.rivfader.rowselector.representation;

import edu.rivfader.relalg.representation.IQualifiedNameRow;

import edu.rivfader.relalg.representation.IQualifiedColumnName;
import java.util.Collection;
import java.util.HashSet;
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


    @Override
    public Collection<IQualifiedColumnName> getRequiredColumns() {
        return new HashSet<IQualifiedColumnName>();
    }
}
