package edu.rivfader.commands;

import edu.rivfader.data.Database;
import edu.rivfader.relalg.IRelAlgExpr;
import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.relalg.IQualifiedNameRow;

import java.io.Writer;
import java.io.IOException;
import java.util.Iterator;

/**
 * Implements the command to print the results of a certain query.
 * @author harald
 */
public class PrintQuery implements ICommand {
    /**
     * contains the query to execute.
     */
    private IRelAlgExpr query;

    /**
     * constucts a new printQuery command.
     * @param pQuery the query to execute.
     */
    public PrintQuery(final IRelAlgExpr pQuery) {
        query = pQuery;
    }

    private String buildColumns(final IQualifiedNameRow source) {
        Iterator<IQualifiedColumnName> names = source.columns().iterator();
        StringBuilder columnRow = new StringBuilder();
        IQualifiedColumnName cn; // current name
        cn = names.next();
        columnRow.append(cn.getTable() + "." + cn.getColumn());
        while(names.hasNext()) {
            columnRow.append(" ");
            cn = names.next();
            columnRow.append(cn.getTable() + "." + cn.getColumn());
        }
        columnRow.append('\n');
        return columnRow.toString();
    }

    private String buildValueRow(final IQualifiedNameRow source) {
        Iterator<IQualifiedColumnName> names = source.columns().iterator();
        StringBuilder valueRow = new StringBuilder();
        IQualifiedColumnName cn; // current name

        valueRow.append(source.getData(names.next()));
        while(names.hasNext()) {
            valueRow.append(" ");
            valueRow.append(source.getData(names.next()));
        }
        valueRow.append('\n');
        return valueRow.toString();
    }

    @Override
    public void execute(final Database context, final Writer output)
        throws IOException {
        Iterator<IQualifiedNameRow> rows = query.evaluate(context);
        if(!rows.hasNext()) {
            output.write("Empty result set.\n");
            return;
        }
        IQualifiedNameRow current = rows.next();
        output.write(buildColumns(current));
        output.write(buildValueRow(current));
        while(rows.hasNext()) {
            output.write(buildValueRow(rows.next()));
        }
    }
}
