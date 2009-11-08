package edu.rivfader.commands;

import edu.rivfader.data.Row;
import edu.rivfader.data.Database;
import edu.rivfader.relalg.rowselector.IRowSelector;

import java.util.Map;
import java.util.Iterator;

import java.io.IOException;
import java.io.Writer;

/**
 * This class implements updating a table.
 * @author harald
 */
public class UpdateTable implements ICommand {
    /**
     * contains the name of the table to change.
     */
    private String tableName;

    /**
     * contains the assignments to make.
     */
    private Map<String, String> assignments;

    /**
     * contains the predicate of which rows to update.
     */
    private IRowSelector selectedRows;

    /**
     * Constructs a new Update table command.
     * @param pTableName the name of the table to update
     * @param pAssignments a mapping from names to new values
     * @param pSelectedRows the predicate to select rows.
     */
    public UpdateTable(final String pTableName,
                       final Map<String, String> pAssignments,
                       final IRowSelector pSelectedRows) {
        tableName = pTableName;
        assignments = pAssignments;
        selectedRows = pSelectedRows;
    }

    @Override
    public void execute(final Database context, final Writer output)
        throws IOException {
        context.openTableForWriting(tableName);
        Row row;
        Iterator<Row> rows = context.loadTable(tableName);
        while(rows.hasNext()) {
            row = rows.next();
            if(selectedRows.acceptsRow(row)) {
                for(String column : row.columns()) {
                    if(assignments.containsKey(column)) {
                        row.setData(column, assignments.get(column));
                    }
                }
            }
            context.storeRow(tableName, row);
        }
        context.closeTable(tableName);
    }
}
