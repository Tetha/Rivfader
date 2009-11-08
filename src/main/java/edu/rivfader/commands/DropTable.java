package edu.rivfader.commands;

import edu.rivfader.data.Database;

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
    public void execute(final Database context) {
        context.dropTable(tableToDrop);
    }
}
