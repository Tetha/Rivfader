package edu.rivfader.data;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * This implements a row from the database.
 * @author harald
 */
public class Row {
    /**
     * contains the values of the column.
     */
    private Map<String, String> columnValues;

    /**
     * contains the valid column names for this row.
     */
    private List<String> columnNames;

    /**
     * Constructs a new Row.
     * @param pColumnNames the columnNames.
     */
    public Row(final List<String> pColumnNames) {
        columnNames = pColumnNames;
        columnValues = new HashMap<String, String>();
    }

    /**
     * returns true if the column exists in this row.
     * @param columnName the wanted column.
     * @return true if the row has such a column.
     */
    public boolean hasColumn(final String columnName) {
        return columnNames.contains(columnName);
    }

    /**
     * sets a column to a certain value in this row.
     * @param columnName the column to set
     * @param value the value to set
     */
    public void setData(final String columnName, final String value) {
        if (hasColumn(columnName)) {
            columnValues.put(columnName, value);
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * gets the value of a column in this row.
     * @param columnName the name of the column to get
     * @return the current content of the column.
     */
    public String getData(final String columnName) {
        if (hasColumn(columnName)) {
            return columnValues.get(columnName);
        } else {
            throw new NoSuchElementException();
        }
    }

    /**
     * returns an iterator over all column names.
     * @return an iterable for all column names.
     */
    public Iterable<String> columns() {
        return columnNames;
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Row)) {
            return false;
        }
        Row castedOther = (Row) other;
        return columnValues.equals(castedOther.columnValues)
                && columnNames.equals(castedOther.columnNames);
    }

    @Override
    public int hashCode() {
        return columnValues.hashCode() ^ columnNames.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Row(");
        for (String n : columnValues.keySet()) {
            result.append(n);
            result.append(" -> ");
            result.append(columnValues.get(n));
            result.append(" ");
        }
        result.append(")");
        return result.toString();
    }

    /**
     * Constructs a row which contains all columns and data from both parameter
     * rows.
     * @param left the first row to combine
     * @param right the second row to combine
     * @return the combination row.
     */
    public static Row combineRows(final Row left, final Row right) {
        List<String> columns = new LinkedList<String>();
        addAllTo(left.columns(), columns);
        addAllTo(right.columns(), columns);
        Row result = new Row(columns);
        copyData(left, result);
        copyData(right, result);
        return result;
    }

    /**
     * adds all elements from the iterable to the list.
     * @param i some iterable
     * @param l some list
     */
    private static void addAllTo(final Iterable<String> source,
                                 final List<String> destination) {
        for (String element : source) {
            destination.add(element);
        }
    }

    /**
     * copies all data from a row to another.
     * @param source where to copy the data from
     * @param destination where to copy the data to.
     */
    private static void copyData(final Row source, final Row destination) {
        for (String column : source.columns()) {
            destination.setData(column, source.getData(column));
        }
    }
}
