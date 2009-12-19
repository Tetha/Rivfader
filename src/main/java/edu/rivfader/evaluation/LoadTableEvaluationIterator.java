package edu.rivfader.evaluation;

import edu.rivfader.data.Row;
import java.util.Iterator;
import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.QualifiedNameRow;

/**
 * this iterator turns all rows from the source into IQualifiedNameRows.
 * @author harald
 */
public class LoadTableEvaluationIterator
        implements Iterator<IQualifiedNameRow> {
    private Iterator<Row> source;
    private String tablename;
    public LoadTableEvaluationIterator(Iterator<Row> pSource,
                                       String pTablename) {
        source = pSource;
        tablename = pTablename;
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
