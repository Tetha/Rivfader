package edu.rivfader.relalg;

import java.util.Collection;

public interface IQualifiedNameRow {
    /**
     * returns all columns of this row.
     * @return a collection of all columns.
     */
    Collection<QualifiedColumnName> columns();

    /**
     * resolves a string to a name, raieses errors on ambiguities.
     * @param columnName the columnName to resolve.
     * @return the qualified column name
     */
    QualifiedColumnName resolveUnqualifiedName(String columnName);

    /**
     * gets the data for the given column name.
     * @param column the column to modify
     * @return the data of the column
     */
    String getData(QualifiedColumnName column);

    /**
     * sets the data in the row.
     * @param column the column to modify
     * @param newData the data to enter
     */
    void setData(QualifiedColumnName column, String newData);

}
