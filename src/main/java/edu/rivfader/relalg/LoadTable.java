package edu.rivfader.relalg;

import edu.rivfader.data.Row;
import edu.rivfader.data.Database;

import java.util.Iterator;

/**
 * Wrapper to load tables.
 * @author harald
 */
public class LoadTable implements IRelAlgExpr {
    /**
     * contains the name of the table to load.
     */
    private String tablename;

    /**
     * Constructs a new wrapper which loads rows from pTablename in pDatabase.
     * @param pTablename the table to load the rows from
     */
    public LoadTable(final String pTablename) {
        tablename = pTablename;
    }

    @Override
    public Iterator<Row> evaluate(final Database context) {
        return context.loadTable(tablename);
    }
}
