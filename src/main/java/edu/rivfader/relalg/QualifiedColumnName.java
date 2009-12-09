package edu.rivfader.relalg;

public class QualifiedColumnName implements IQualifiedColumnName {
    /**
     * contains the table name.
     */
    private String table;

    /**
     * contains the column name
     */
    private String column;

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
}
