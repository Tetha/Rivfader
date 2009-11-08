package edu.rivfader.commands;

import edu.rivfader.data.Database;

import java.util.List;

import java.io.Writer;
import java.io.IOException;

/**
 * This implements creating a table.
 * @author harald
 */
public class CreateTable implements ICommand {
    /**
     * contains the name of the table to create.
     */
    private String tableName;

    /**
     * contains the names of the columns to create.
     */
    private List<String> columnNames;

    /**
     * This creates a new table creation node.
     *
     * Note: The type of the columns is implied to be varchar.
     *
     * @param pTableName the name of the table to create
     * @param pColumnNames the column names the table is supposed to have.
     */
    public CreateTable(final String pTableName,
                       final List<String> pColumnNames) {
        tableName = pTableName;
        columnNames = pColumnNames;
    }

    @Override
    public void execute(final Database context, final Writer output)
        throws IOException {
        context.createTable(tableName, columnNames);
    }
}
