package edu.rivfader.relalg;

import edu.rivfader.data.Database;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

/**
 * This is a decorator that renames a table.
 * @author harald
 */
public class RenameTable implements ITable {
    /**
     * contains the table to rename.
     */
    private ITable renamedTable;

    /**
     * contains the new name of the table
     */
    private String newName;

    /**
     * creates a new renaming table.
     * @param pRenamedTable the table to rename
     * @param pNewName the new table name
     */
    public RenameTable(ITable pRenamedTable, String pNewName) {
        if (pRenamedTable == null) {
            throw new IllegalArgumentException("pRenamedTable is null");
        }
        if (pNewName == null) {
            throw new IllegalArgumentException("pNewName is null");
        }

        renamedTable = pRenamedTable;
        newName = pNewName;
    }

    @Override
    public String getName() {
        return newName;
    }

    @Override
    public List<IQualifiedColumnName> getColumnNames() {
        List<IQualifiedColumnName> o; // output

        o = new LinkedList<IQualifiedColumnName>();
        // renamed column name
        for (IQualifiedColumnName rcn : renamedTable.getColumnNames()) {
            o.add(new QualifiedColumnName(newName, rcn.getColumn()));
        }

        return o;
    }

    @Override
    public void setDatabase(Database context) {
        renamedTable.setDatabase(context);
    }

    @Override
    public void openForWriting() {
        renamedTable.openForWriting();
    }

    @Override
    public Iterator<IQualifiedNameRow> load() {
        return new RenamingIterator(renamedTable.load(), newName);
    }

    public Iterator<IQualifiedNameRow> evaluate(Database context) {
        return new RenamingIterator(renamedTable.evaluate(context),
                                    newName);
    }

    private static class RenamingIterator
            implements Iterator<IQualifiedNameRow> {
        private Iterator<IQualifiedNameRow> source;
        private String newName;

        public RenamingIterator(Iterator<IQualifiedNameRow> pSource,
                                String pNewName) {
            newName = pNewName;
            source = pSource;
        }

        @Override
        public boolean hasNext() {
            return source.hasNext();
        }

        @Override
        public IQualifiedNameRow next() {
            IQualifiedNameRow i; // input
            IQualifiedNameRow o; // output
            List<IQualifiedColumnName> rcns; // renamed column names

            i = source.next();

            rcns = new LinkedList<IQualifiedColumnName>();
            // renamed column name
            for (IQualifiedColumnName rcn : i.columns()) {
                rcns.add(new QualifiedColumnName(newName, rcn.getColumn()));
            }

            o = new QualifiedNameRow(rcns);

            // renamed column name
            for(IQualifiedColumnName rcn : i.columns()) {
                o.setData(new QualifiedColumnName(newName, rcn.getColumn()),
                          i.getData(rcn));
            }

            return o;
        }

        @Override
        public void remove() {
            source.remove();
        }
    }

    @Override
    public void storeRow(IQualifiedNameRow newRow) {
        IQualifiedNameRow o; // output
        List<IQualifiedColumnName> rcns; // renamed column names
        String rtn; // renamed table name

        rtn = renamedTable.getName();
        rcns = new LinkedList<IQualifiedColumnName>();
        for(IQualifiedColumnName rcn : newRow.columns()) {
            rcns.add(new QualifiedColumnName(rtn, rcn.getColumn()));
        }
        o = new QualifiedNameRow(rcns);

        // renamed column name
        for(IQualifiedColumnName rcn : newRow.columns()) {
            o.setData(new QualifiedColumnName(rtn, rcn.getColumn()),
                      newRow.getData(rcn));
        }
        renamedTable.storeRow(o);
    }

    @Override
    public void appendRow(IQualifiedNameRow newRow) {
        IQualifiedNameRow o; // output
        List<IQualifiedColumnName> rcns; // renamed column names
        String rtn; // renamed table name

        rtn = renamedTable.getName();
        rcns = new LinkedList<IQualifiedColumnName>();
        for(IQualifiedColumnName rcn : newRow.columns()) {
            rcns.add(new QualifiedColumnName(rtn, rcn.getColumn()));
        }
        o = new QualifiedNameRow(rcns);

        // renamed column name
        for(IQualifiedColumnName rcn : newRow.columns()) {
            o.setData(new QualifiedColumnName(rtn, rcn.getColumn()),
                      newRow.getData(rcn));
        }
        renamedTable.appendRow(o);
    }

    @Override
    public void close() {
        renamedTable.close();
    }
}
