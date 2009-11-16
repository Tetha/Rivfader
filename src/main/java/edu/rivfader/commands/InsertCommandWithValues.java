package edu.rivfader.commands;

import edu.rivfader.data.Row;
import edu.rivfader.data.Database;
import edu.rivfader.errors.NoColumnValueMappingPossible;
import java.util.Map;
import java.util.Set;
import java.util.List;

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
        List<String> columnNames = context.getColumnNames(tableName);
        if(!values.keySet().containsAll(columnNames)) {
            Set<String> insertedColumns = values.keySet();
            insertedColumns.removeAll(context.getColumnNames(tableName));
            StringBuilder error = new StringBuilder();
            error.append("Table " + tableName + " has no Value");
            for(String badColumn : insertedColumns) {
                error.append(" ");
                error.append(badColumn);
            }
            throw new NoColumnValueMappingPossible(error.toString());
        }
        Row insertedRow = new Row(columnNames);
        for(String column : values.keySet()) {
            insertedRow.setData(column, values.get(column));
        }
        context.appendRow(tableName, insertedRow);
    }
}
