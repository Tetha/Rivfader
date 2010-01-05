package edu.rivfader.commands;

import edu.rivfader.data.Database;
import edu.rivfader.relalg.representation.IQualifiedNameRow;
import edu.rivfader.relalg.representation.QualifiedNameRow;
import edu.rivfader.relalg.representation.IQualifiedColumnName;

import java.util.Map;

import java.io.Writer;
import java.io.IOException;
import edu.rivfader.relalg.representation.ITable;

/**
 * This implements the insert command with column names
 * given.
 * @author harald
 */
public class InsertCommandWithValues implements ICommand {
    /**
     * contains the name of the table to insert values into.
     */
    private ITable table;
    /**
     * contains the values to insert into the table.
     */
    private Map<String, String> values;

    /**
     * constructs a new InsertCommandWithValues-node.
     * @param pTableName the name of the table to insert into
     * @param pValues the values to insert into the table.
     */
    public InsertCommandWithValues(final ITable pTable,
                         final Map<String, String> pValues) {
        table = pTable;
        values = pValues;
    }

    @Override
    public void execute(final Database context, final Writer output)
        throws IOException {
        IQualifiedNameRow ir; // inserted row
        table.setDatabase(context);
        ir = new QualifiedNameRow(table.getColumnNames());
        for(String c : values.keySet()) { // column
            IQualifiedColumnName rcn; // resolved column name
            rcn = ir.resolveUnqualifiedName(c);
            ir.setData(rcn, values.get(c));
        }
        table.appendRow(ir);
    }
}
