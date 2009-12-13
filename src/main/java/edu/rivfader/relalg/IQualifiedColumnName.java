package edu.rivfader.relalg;

/**
 * a qualified column name is a regular column name, but qualified
 * by a table name.
 * @author harald
 */
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
