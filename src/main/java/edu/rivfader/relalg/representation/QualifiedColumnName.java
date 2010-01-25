package edu.rivfader.relalg.representation;

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
        if (table != null && co.getTable() != null) {
            if(!table.equals(co.getTable())) {
                return false;
            }
        }

        if (column == null) {
            if (co.getColumn() != null) {
                return false;
            }
        } else {
            if (!column.equals(co.getColumn())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int columnHashcode = column == null? 0 : column.hashCode();

        return columnHashcode;
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
