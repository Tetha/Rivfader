package edu.rivfader.relalg;

/**
 * This is a column name, qualified by the table name.
 * @author harald
 */
public class QualifiedColumnName implements IQualifiedColumnName {
    /**
     * contains the table name.
     */
    private String table;

    /**
     * contains the column name.
     */
    private String column;

    /**
     * constructs a new name.
     * @param pTable the name of the table
     * @param pColumn the name of the column
     */
    public QualifiedColumnName(String pTable, String pColumn) {
        if (pTable == null) {
            throw new IllegalArgumentException("pTable == null");
        }

        if (pColumn == null) {
            throw new IllegalArgumentException("pColumn == null");
        }

        table = pTable;
        column = pColumn;
    }

    @Override
    public String getTable() {
        return table;
    }

    @Override
    public String getColumn() {
        return column;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof IQualifiedColumnName)) {
            return false;
        }

        IQualifiedColumnName co = (IQualifiedColumnName) other; //casted other
        return table.equals(co.getTable()) && column.equals(co.getColumn());
    }

    @Override
    public int hashCode() {
        return table.hashCode() ^ column.hashCode();
    }

    @Override
    public int compareTo(IQualifiedColumnName other) {
        int tno; // table name ordering
        int cno; // column name ordering

        tno = table.compareTo(other.getTable());
        cno = column.compareTo(other.getColumn());

        if (tno == 0) {
            return cno;
        } else {
            return tno;
        }
    }

    @Override //TODO: test
    public String toString() {
        return table + "." + column;
    }
}
