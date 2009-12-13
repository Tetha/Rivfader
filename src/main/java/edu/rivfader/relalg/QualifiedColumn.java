package edu.rivfader.relalg.rowselector;

import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.relalg.IQualifiedNameRow;

/**
 * retrieves a column from a table via the qualified name of the
 * column.
 * @author harald
 */
public class QualifiedColumn implements IValueProvider {
    /**
     * contains the name of the column to get.
     */
    private IQualifiedColumnName inspectedColumn;

    /**
     * constructs the new valueprovider.
     * @param pInspectedColumn the column to get.
     */
    public QualifiedColumn(IQualifiedColumnName pInspectedColumn) {
        inspectedColumn = pInspectedColumn;
    }

    @Override
    public String getValue(IQualifiedNameRow inspectedRow) {
        return inspectedRow.getData(inspectedColumn);
    }
}
