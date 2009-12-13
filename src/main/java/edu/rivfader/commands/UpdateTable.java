package edu.rivfader.commands;

import edu.rivfader.data.Database;
import edu.rivfader.relalg.rowselector.IRowSelector;
import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.relalg.ITable;

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
    private ITable table;

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
    public UpdateTable(final ITable pTable,
                       final Map<String, String> pAssignments,
                       final IRowSelector pSelectedRows) {
        table = pTable;
        assignments = pAssignments;
        selectedRows = pSelectedRows;
    }

    @Override
    public void execute(final Database context, final Writer output)
        throws IOException {
        IQualifiedNameRow cr; // current row
        Iterator<IQualifiedNameRow> rs; // rows

        table.setDatabase(context);
        table.openForWriting();

        rs = table.load();
        while(rs.hasNext()) {
            cr = rs.next();
            if(selectedRows.acceptsRow(cr)) {
                for(String cn : assignments.keySet()) { // column name
                    IQualifiedColumnName rcn; // resolved column name
                    rcn = cr.resolveUnqualifiedName(cn);
                    cr.setData(rcn, assignments.get(cn));
                }
            }
            table.storeRow(cr);
        }

        table.close();
    }
}
