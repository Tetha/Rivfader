package edu.rivfader.commands;

import edu.rivfader.data.Database;
import edu.rivfader.data.Row;
import edu.rivfader.errors.NoColumnValueMappingPossible;

import java.io.Writer;
import java.io.IOException;

import java.util.List;

/**
 * This command implements inserting without specifying the columns.
 * @author harald
 */
public class InsertCommandWithoutValues implements ICommand {
    /**
     * contains the table to insert into.
     */
    private String tableName;
    /**
     * contains the values to set.
     */
    private List<String> values;

    /**
     * constructs a new insertion node.
     * @param pTableName the name of the table to modify.
     * @param pValues the values to insert into the table
     */
    public InsertCommandWithoutValues(final String pTableName,
            final List<String> pValues) {
        tableName = pTableName;
        values = pValues;
    }

    @Override
    public void execute(final Database context, final Writer output)
        throws IOException {
        List<String> columnNames = context.getColumnNames(tableName);
        if(columnNames.size() > values.size()) {
            throw new
                NoColumnValueMappingPossible("Not enough values for table");
        } else if(columnNames.size() < values.size()) {
            throw new
                NoColumnValueMappingPossible("Too many values for table");
        }
        Row valueRow = new Row(columnNames);
        for(int i = 0; i < values.size(); i++) {
            valueRow.setData(columnNames.get(i), values.get(i));
        }
        context.appendRow(tableName, valueRow);
    }
}
