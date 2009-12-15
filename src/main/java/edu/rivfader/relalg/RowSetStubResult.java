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

    /**
     * constructs a new empty rowset.
     */
    public RowSetStubResult() {
        resultRows = new LinkedList<IQualifiedNameRow>();
    }

    /**
     * constructs a new rowSet which contains the result rows initially.
     * @param pResultRows the initial result rows
     */
    public RowSetStubResult(List<IQualifiedNameRow> pResultRows) {
        resultRows = new LinkedList<IQualifiedNameRow>(pResultRows);
    }

    /**
     * expects a new row.
     * The parameters must be arrays of length 3, whereas the first entry
     * in the array must be the table name, the second parameter must be
     * the column name and the third parameter must be the value for
     * the column described by the first two entries.
     * @param columnValuePairs column-value specifications.
     */
    public void expectRow(String[][] columnValuePairs) {
        List<IQualifiedColumnName> qns; // qualified Names
        List<String> vs; // values
        IQualifiedNameRow nr; // new row

        qns = new LinkedList<IQualifiedColumnName>();
        vs = new LinkedList<String>();

        for(int i = 0; i < columnValuePairs.length; i++) {
            String[] ccvp = columnValuePairs[i]; // current column value pair
            if (ccvp.length != 3) {
                throw new IllegalArgumentException("column value pair #"
                                                    + i
                                                    + " has wrong length");
            }
            qns.add(new QualifiedColumnName(ccvp[0], ccvp[1]));
            vs.add(ccvp[2]);
        }

        nr = new QualifiedNameRow(qns);
        for(int i = 0; i < qns.size(); i++) {
            nr.setData(qns.get(i), vs.get(i));
        }
        resultRows.add(nr);
    }

    @Override
    public Iterator<IQualifiedNameRow> getExpectedResult() {
        return resultRows.iterator();
    }
}
