package edu.rivfader.relalg;

import java.util.List;
import java.util.LinkedList;

/**
 * This class provides a simple mechanism to create sequences of
 * of rows.
 * @author harald
 */
public class RowFactory {
    private List<IQualifiedColumnName> columnNames;
    private List<IQualifiedNameRow> rows;

    /**
     * builds a new rowfactory which constructs rows with the given
     * column names.
     * @param pColumnNames {table, column}-pairs
     */
    public RowFactory(String[]... pColumnNames) {
        rows = new LinkedList<IQualifiedNameRow>();
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
     * gets the list of rows in the expected row set.
     * @return the constructed sequence of rows.
     */
    public List<IQualifiedNameRow> getRows() {
        return rows;
    }

    /**
     * expects a new row.
     * @param values, in the order of the constructor parameter
     */
    public void addRow(String... values) {
        IQualifiedNameRow nr; // new row

        nr = new QualifiedNameRow(columnNames);
        for(int i = 0; i < values.length; i++) {
            nr.setData(columnNames.get(i), values[i]);
        }
        rows.add(nr);
    }

    /**
     * expecst an otherwisely constructed row.
     * @param row the additional row to expect
     */
    public void addRow(IQualifiedNameRow newRow) {
        rows.add(newRow);
    }
}
