package edu.rivfader.relalg.rowselector;

import edu.rivfader.relalg.IQualifiedNameRow;

public class Always implements IRowSelector {
    @Override
    public boolean acceptsRow(IQualifiedNameRow examinedRow) {
        return true;
    }
}
