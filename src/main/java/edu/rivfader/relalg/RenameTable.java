package edu.rivfader.relalg;

import edu.rivfader.data.Database;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

public class RenameTable implements ITable {
    private ITable renamedTable;
    private String newName;
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

    public String getName() {
        return newName;
    }

    public List<IQualifiedColumnName> getColumnNames() {
        List<IQualifiedColumnName> o; // output

        o = new LinkedList<IQualifiedColumnName>();
        // renamed column name
        for (IQualifiedColumnName rcn : renamedTable.getColumnNames()) {
            o.add(new QualifiedColumnName(newName, rcn.getColumn()));
        }

        return o;
    }

    public void setDatabase(Database context) {
        renamedTable.setDatabase(context);
    }

    public void openForWriting() {
        renamedTable.openForWriting();
    }

    public Iterator<IQualifiedNameRow> load() {
        return new RenamingIterator(renamedTable.load());
    }

    private class RenamingIterator implements Iterator<IQualifiedNameRow> {
        private Iterator<IQualifiedNameRow> source;

        public RenamingIterator(Iterator<IQualifiedNameRow> pSource) {
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
    public Iterator<IQualifiedNameRow> evaluate(Database context) {
        return new RenamingIterator(renamedTable.evaluate(context));
    }

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

    public void close() {
        renamedTable.close();
    }
}
