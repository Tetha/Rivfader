package edu.rivfader.commands;

import edu.rivfader.data.Database;
import edu.rivfader.relalg.representation.IRelAlgExpr;
import edu.rivfader.relalg.representation.IQualifiedColumnName;
import edu.rivfader.relalg.representation.IQualifiedNameRow;
import edu.rivfader.relalg.operation.evaluation.Evaluator;

import edu.rivfader.relalg.operations.profiling_evaluation.ProfilingEvaluator;
import edu.rivfader.relalg.operations.profiling_evaluation.Block1Costs;
import edu.rivfader.relalg.operations.profiling_evaluation.ICostAccumulator;

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
        ICostAccumulator costs = new Block1Costs();
        Evaluator e = new ProfilingEvaluator(context, costs);
        Iterator<IQualifiedNameRow> rows = e.transform(query);
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
        output.write("Costs: " + costs.getCost() + "\n");
    }
}
