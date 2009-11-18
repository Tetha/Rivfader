package edu.rivfader.relalg.rowselector;

import edu.rivfader.data.Row;

public class Always implements IRowSelector {
    @Override
    public boolean acceptsRow(Row examinedRow) {
        return true;
    }
}
