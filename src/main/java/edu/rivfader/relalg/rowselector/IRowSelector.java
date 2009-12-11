package edu.rivfader.relalg.rowselector;
import edu.rivfader.relalg.IQualifiedNameRow;

/**
 * A rowselector implements the filtering of rows.
 * @author harald
 */
public interface IRowSelector {
    /**
     * this function either selects the row or discards the row.
     * @param data the row to examine
     * @return true if the row is selected.
     */
    boolean acceptsRow(IQualifiedNameRow data);
}
