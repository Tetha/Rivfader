package edu.rivfader.commands;

import edu.rivfader.data.Row;
import edu.rivfader.data.Database;

import java.util.Map;

import java.io.Writer;
import java.io.IOException;

/**
 * This implements the insert command with column names
 * given.
 * @author harald
 */
public class InsertCommandWithValues implements ICommand {
    /**
     * contains the name of the table to insert values into.
     */
    private String tableName;
    /**
     * contains the values to insert into the table.
     */
    private Map<String, String> values;

    /**
     * constructs a new InsertCommandWithValues-node.
     * @param pTableName the name of the table to insert into
     * @param pValues the values to insert into the table.
     */
    public InsertCommandWithValues(final String pTableName,
                         final Map<String, String> pValues) {
        tableName = pTableName;
        values = pValues;
    }

    @Override
    public void execute(final Database context, final Writer output)
        throws IOException {
        Row insertedRow = new Row(values.keySet().iterator());
        for(String column : values.keySet()) {
            insertedRow.setData(column, values.get(column));
        }
        context.openTableForWriting(tableName);
        context.appendRow(tableName, insertedRow);
        context.closeTable(tableName);
    }
}
