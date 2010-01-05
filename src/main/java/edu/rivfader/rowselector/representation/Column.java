package edu.rivfader.rowselector.representation;

import edu.rivfader.relalg.representation.IQualifiedNameRow;
import edu.rivfader.relalg.representation.IQualifiedColumnName;

/**
 * This class implements the use of column values in comparisions.
 * @author harald
 */
public class Column implements IValueProvider {
    /**
     * contains the column name to get.
     */
    private String columnName;

    /**
     * Constructs a new column value prodider.
     * @param pColumnName the name of the column to get
     */
    public Column(final String pColumnName) {
        columnName = pColumnName;
    }

    @Override
    public String getValue(final IQualifiedNameRow data) {
        // resolved name
        IQualifiedColumnName rn = data.resolveUnqualifiedName(columnName);
        return data.getData(rn);
    }
}
