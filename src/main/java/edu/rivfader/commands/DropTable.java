package edu.rivfader.commands;

import edu.rivfader.data.Database;

import java.io.Writer;
import java.io.IOException;

/**
 * This is the implementation of the drop table query.
 * @author harald.
 */
public class DropTable implements ICommand {
    /**
     * contains the name of the table to drop.
     */
    private String tableToDrop;

    /**
     * constructs a new DropTable command.
     * @param tableName the name of the table to drop.
     */
    public DropTable(final String tableName) {
        tableToDrop = tableName;
    }

    @Override
    public void execute(final Database context, final Writer output)
            throws IOException {
        context.dropTable(tableToDrop);
    }
}
