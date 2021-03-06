package edu.rivfader.commands;

import edu.rivfader.data.Database;
import edu.rivfader.relalg.representation.ITable;
import edu.rivfader.rowselector.representation.IRowSelector;
import edu.rivfader.rowselector.operations.AcceptsRowEvaluator;
import edu.rivfader.relalg.representation.IQualifiedNameRow;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
/**
 * This class implements the Delete-Query.
 * @author harald
 */
public class Delete implements ICommand {
    /**
     * contains the name of the table to delete from.
     */
    private ITable table;

    /**
     * contains the predicate when to delete.
     */
    private IRowSelector predicate;

    /**
     * constructs a new delete-node.
     * @param pTableName the name of the table to delete from
     * @param pPredicate the predicate when to delete
     */
    public Delete(final ITable pTable, final IRowSelector pPredicate) {
        table = pTable;
        predicate = pPredicate;
    }

    @Override
    public void execute(final Database context, final Writer output)
        throws IOException {
        Iterator<IQualifiedNameRow> rs; // rows
        IQualifiedNameRow cr; // current row

        table.setDatabase(context);
        table.openForWriting();

        rs = table.load();
        while (rs.hasNext()) {
            cr = rs.next();
            AcceptsRowEvaluator evaluator = new AcceptsRowEvaluator(cr);
            if (!evaluator.transform(predicate)) {
                table.storeRow(cr);
            }
        }

        table.close();
    }
}
