package edu.rivfader.commands;

import edu.rivfader.data.Row;
import edu.rivfader.data.Database;
import edu.rivfader.relalg.IRelAlgExpr;

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

    private String buildColumns(final Row source) {
        Iterator<String> names = source.columns().iterator();
        if(!names.hasNext()) {
            return "";
        }
        StringBuilder columnRow = new StringBuilder();
        columnRow.append(names.next());
        while(names.hasNext()) {
            columnRow.append(" ");
            columnRow.append(names.next());
        }
        columnRow.append('\n');
        return columnRow.toString();
    }

    private String buildValueRow(final Row source) {
        Iterator<String> names = source.columns().iterator();
        if(!names.hasNext()) {
            return "";
        }
        StringBuilder valueRow = new StringBuilder();
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
        Iterator<Row> rows = query.evaluate(context);
        if(!rows.hasNext()) {
            return;
        }
        Row current = rows.next();
        output.write(buildColumns(current));
        output.write(buildValueRow(current));
        while(rows.hasNext()) {
            current = rows.next();
            output.write(buildValueRow(current));
        }
    }
}
