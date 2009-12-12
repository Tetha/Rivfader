package edu.rivfader.commands;

import edu.rivfader.data.Row;
import edu.rivfader.data.Database;
import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.QualifiedNameRow;
import edu.rivfader.relalg.IQualifiedColumnName;

import edu.rivfader.errors.NoColumnValueMappingPossible;
import java.util.Map;
import java.util.Set;
import java.util.List;

import java.io.Writer;
import java.io.IOException;
import edu.rivfader.relalg.ITable;

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
