package edu.rivfader.relalg.rowselector;

import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.relalg.IQualifiedNameRow;

public class QualifiedColumn implements IValueProvider {
    private IQualifiedColumnName inspectedColumn;

    public QualifiedColumn(IQualifiedColumnName pInspectedColumn) {
        inspectedColumn = pInspectedColumn;
    }

    @Override
    public String getValue(IQualifiedNameRow inspectedRow) {
        return inspectedRow.getData(inspectedColumn);
    }
}
