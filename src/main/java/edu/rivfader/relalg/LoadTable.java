package edu.rivfader.relalg;

import edu.rivfader.data.Row;
import edu.rivfader.data.Database;
import edu.rivfader.relalg.IQualifiedNameRow;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.io.IOException;
import edu.rivfader.errors.ColumnLoadError;
import edu.rivfader.errors.ColumnWriteError;

/**
 * Wrapper to load tables.
 * @author harald
 */
public class LoadTable implements ITable {
    /**
     * contains the name of the table to load.
     */
    private String tablename;

    private Database context;

    private boolean isOpen;

    /**
     * Constructs a new wrapper which loads rows from pTablename in pDatabase.
     * @param pTablename the table to load the rows from
     */
    public LoadTable(final String pTablename) {
        tablename = pTablename;
    }

    private void requireContextIsSet() {
        if (context == null) {
            throw new IllegalStateException("database unset");
        }
    }

    private void requireTableIsOpen() {
        if (!isOpen) {
            throw new IllegalStateException("table is not open");
        }
    }

    private void requireTableIsClosed() {
        if (isOpen) {
            throw new IllegalStateException("table is already open!");
        }
    }

    @Override
    public String getName() { // TODO: test
        return tablename;
    }

    @Override
    public void setDatabase(Database pContext) {
        context = pContext;
    }

    @Override
    public List<IQualifiedColumnName> getColumnNames() {
        List<String> ucns; // unqualified column names
        List<IQualifiedColumnName> o; // output

        requireContextIsSet();

        o = new LinkedList<IQualifiedColumnName>();

        try {
            ucns = context.getColumnNames(tablename);
        } catch (IOException e) {
            throw new ColumnLoadError(e);
        }
        for(String ucn : ucns) { // unqualified column name
            o.add(new QualifiedColumnName(tablename, ucn));
        }
        return o;
    }

    @Override
    public void openForWriting() {
        requireContextIsSet();
        requireTableIsClosed();

        try {
            context.openTableForWriting(tablename);
            isOpen = true;
        } catch (IOException e) {
            throw new ColumnLoadError(e);
        }
    }

    @Override
    public void storeRow(IQualifiedNameRow newRow) {
        Set<String> tns; // table names in newRow
        requireContextIsSet();
        requireTableIsOpen();

        if (newRow == null) {
            throw new IllegalArgumentException("newrow == null");
        }

        tns = new HashSet<String>();
        for (IQualifiedColumnName cn : newRow.columns()) { // column name
            tns.add(cn.getTable());
        }

        if (!(tns.size() == 1 && tns.contains(tablename))) {
            throw new ColumnWriteError("write to wrong table");
        }

        try {
            context.storeRow(tablename, QualifiedNameRow.toRow(newRow));
        } catch (IOException e) {
            throw new ColumnWriteError(e);
        }
    }

    @Override
    public void appendRow(IQualifiedNameRow newRow) {
        requireContextIsSet();
        try {
            context.appendRow(tablename, QualifiedNameRow.toRow(newRow));
        } catch (IOException e) {
            throw new ColumnWriteError(e);
        }
    }

    @Override
    public void close() {
        requireTableIsOpen();
        try {
            context.closeTable(tablename);
        } catch (IOException e) {
            throw new ColumnWriteError(e);
        }
    }

    @Override
    public Iterator<IQualifiedNameRow> load() {
        requireContextIsSet();

        try {
            return new WrappingIterator(context.loadTable(tablename));
        } catch (IOException e) {
            throw new RuntimeException("loading the table did not work", e);
        }
    }

    @Override
    public Iterator<IQualifiedNameRow> evaluate(Database context) {
        setDatabase(context);
        return load();
    }

    /**
     * this iterator turns all rows from the source into IQualifiedNameRows.
     * @author harald
     */
    private class WrappingIterator implements Iterator<IQualifiedNameRow> {
        private Iterator<Row> source;
        public WrappingIterator(Iterator<Row> pSource) {
            source = pSource;
        }

        @Override
        public boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public IQualifiedNameRow next() {
            return QualifiedNameRow.fromRow(tablename, source.next());
        }

        @Override
        public void remove() {
            source.remove();
        }
    }
}
