package edu.rivfader.relalg.representation;

import java.util.Collection;

/**
 * an IQualifiedNameRow is a Row, but the column names of the
 * row are fully qualified (opposed to the row, where they are
 * unqualified.
 */
public interface IQualifiedNameRow {
    /**
     * returns all columns of this row.
     * @return a collection of all columns.
     */
    Collection<IQualifiedColumnName> columns(); // TODO: inconsistent getter name

    /**
     * resolves a string to a name, raieses errors on ambiguities.
     * @param columnName the columnName to resolve.
     * @return the qualified column name
     */
    IQualifiedColumnName resolveUnqualifiedName(String columnName);

    /**
     * gets the data for the given column name.
     * @param column the column to modify
     * @return the data of the column
     */
    String getData(IQualifiedColumnName column);

    /**
     * sets the data in the row.
     * @param column the column to modify
     * @param newData the data to enter
     */
    void setData(IQualifiedColumnName column, String newData);

}
