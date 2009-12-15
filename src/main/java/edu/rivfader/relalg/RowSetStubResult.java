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
    private List<IQualifiedColumnName> columnNames;

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

    public void setColumnNames(String[]... pColumnNames) {
        columnNames = new LinkedList<IQualifiedColumnName>();
        for (int i = 0; i < pColumnNames.length; i++) {
            if (pColumnNames[i].length != 2) {
                throw new IllegalArgumentException("column name "
                                                    + i
                                                    + " messed up.");
            }
            columnNames.add(new QualifiedColumnName(pColumnNames[i][0],
                                                    pColumnNames[i][1]));
        }
    }

    /**
     * expects a new row.
     * @param columnValuePairs column-value specifications.
     */
    public void expectRow(String... values) {
        IQualifiedNameRow nr; // new row

        nr = new QualifiedNameRow(columnNames);
        for(int i = 0; i < values.length; i += 3) {
            nr.setData(columnNames.get(i), values[i]);
        }
        resultRows.add(nr);
    }

    @Override
    public Iterator<IQualifiedNameRow> getExpectedResult() {
        return resultRows.iterator();
    }
}
