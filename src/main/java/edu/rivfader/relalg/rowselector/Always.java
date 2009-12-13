package edu.rivfader.relalg.rowselector;

import edu.rivfader.relalg.IQualifiedNameRow;

/**
 * A predicate that selects everything.
 * @author harald
 */
public class Always implements IRowSelector {
    @Override
    public boolean acceptsRow(IQualifiedNameRow examinedRow) {
        return true;
    }
}
