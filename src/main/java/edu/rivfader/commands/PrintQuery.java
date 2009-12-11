package edu.rivfader.commands;

import edu.rivfader.data.Database;
import edu.rivfader.relalg.IRelAlgExpr;
import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.relalg.IQualifiedNameRow;

import java.io.Writer;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

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
        List<IQualifiedColumnName> ns; // names
        StringBuilder o; // output
        boolean f; // first

        ns = new LinkedList<IQualifiedColumnName>(source.columns());
        Collections.sort(ns);
        o =  new StringBuilder();
        f = true;

        for (IQualifiedColumnName cn : ns) { // current name
            if (!f) {
                o.append(" ");
            }
            f = false;
            o.append(cn.getTable() + "." + cn.getColumn());
        }
        o.append('\n');
        return o.toString();
    }

    private String buildValueRow(final IQualifiedNameRow source) {
        List<IQualifiedColumnName> ns; // names
        StringBuilder o; // output
        boolean f; // first

        ns = new LinkedList<IQualifiedColumnName>(source.columns());
        Collections.sort(ns);
        o = new StringBuilder();
        f = true;
        for (IQualifiedColumnName cn : ns) { // current name
            if (!f) {
                o.append(" ");
            }
            f = false;
            o.append(source.getData(cn));
        }
        o.append('\n');
        return o.toString();
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
