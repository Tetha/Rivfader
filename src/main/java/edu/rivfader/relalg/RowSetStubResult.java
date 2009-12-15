package edu.rivfader.relalg;

import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

/**
 * This stubresult returns multiple iterators on a single expected result
 * collection.
 */
public class RowSetStubResult
    implements IStubResult<Iterator<IQualifiedNameRow>> {
    private List<IQualifiedNameRow> resultRows;
    public RowSetStubResult(List<IQualifiedNameRow> pResultRows) {
        resultRows = pResultRows;
    }

    public Iterator<IQualifiedNameRow> getExpectedResult() {
        return resultRows.iterator();
    }
}
