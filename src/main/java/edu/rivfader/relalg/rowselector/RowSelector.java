package edu.rivfader.relalg.rowselector;
import edu.rivfader.data.Row;

/**
 * A rowselector implements the filtering of rows.
 * @author harald
 */
public interface RowSelector {
    /**
     * this function either selects the row or discards the row.
     * @param data the row to examine
     * @return true if the row is selected.
     */
    boolean acceptsRow(Row data);
}
