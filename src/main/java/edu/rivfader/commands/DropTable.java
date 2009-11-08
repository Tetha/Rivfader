package edu.rivfader.commands;

import edu.rivfader.data.Database;

public class DropTable implements ICommand {
    private String tableToDrop;

    public DropTable(String tableName) {
        tableToDrop = tableName;
    }

    @Override
    public void execute(final Database context) {
        context.dropTable(tableToDrop);
    }
}
