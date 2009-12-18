package edu.rivfader.relalg;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

/**
 * renames a table in the result set.
 * @author harald
 */
public class RenameTableEvaluationIterator
        implements Iterator<IQualifiedNameRow> {
    private Iterator<IQualifiedNameRow> source;
    private String newName;

    public RenameTableEvaluationIterator(
                            Iterator<IQualifiedNameRow> pSource,
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
