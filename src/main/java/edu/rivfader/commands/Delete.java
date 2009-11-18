package edu.rivfader.commands;

import edu.rivfader.data.Row;
import edu.rivfader.data.Database;
import edu.rivfader.relalg.rowselector.IRowSelector;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
/**
 * This class implements the Delete-Query.
 * @author harald
 */
public class Delete implements ICommand {
    /**
     * contains the name of the table to delete from
     */
    private String tableName;

    /**
     * contains the predicate when to delete.
     */
    private IRowSelector predicate;

    /**
     * constructs a new delete-node
     * @param pTableName the name of the table to delete from
     * @param pPredicate the predicate when to delete
     */
    public Delete(final String pTableName, final IRowSelector pPredicate) {
        tableName = pTableName;
        predicate = pPredicate;
    }

    @Override
    public void execute(final Database context, final Writer output)
        throws IOException {
        context.openTableForWriting(tableName);
        try {
            Iterator<Row> rows = context.loadTable(tableName);
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (!predicate.acceptsRow(currentRow)) {
                    context.storeRow(tableName, currentRow);
                }
            }
        } finally {
            context.closeTable(tableName);
        }
    }
}
