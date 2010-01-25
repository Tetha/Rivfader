package edu.rivfader.rowselector.representation;

import edu.rivfader.relalg.representation.IQualifiedColumnName;
import edu.rivfader.relalg.representation.IQualifiedNameRow;

import edu.rivfader.relalg.representation.IQualifiedColumnName;
import java.util.Collection;
import java.util.HashSet;

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

    @Override
    public Collection<IQualifiedColumnName> getRequiredColumns() {
        Collection<IQualifiedColumnName> result = new HashSet();
        result.add(inspectedColumn);
        return result;
    }
}
