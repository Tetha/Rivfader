package edu.rivfader.relalg;

public interface IQualifiedColumnName extends Comparable<IQualifiedColumnName> {
    /**
     * returns the name of the table of the column.
     * @return the name of the table
     */
    String getTable();

    /**
     * returns the column name.
     * @return the column name
     */
    String getColumn();
}
