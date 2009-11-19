package edu.rivfader.data;

/**
 * This class implements column names which are qualified by 
 * a table name. This is necessary in order to support renaming tables.
 * @author harald
 */
public class QualifiedColumnName {
    /**
     * contains the table name
     */
    private final String tableName;

    /**
     * contains the column name
     */
    private final String columnName;

    /**
     * constructs a new qualified column name.
     * @param pTableName the table name
     * @param pColumnName the column name
     */
    public QualifiedColumnName(final String pTableName, final String pColumnName) {
        if (pColumnName == null) {
            throw new IllegalArgumentException("column name is null");
        }
        if (pTableName == null) {
            throw new IllegalArgumentException("table name is null");
        }
        tableName = pTableName;
        columnName = pColumnName;
    }

    /**
     * gets the table name.
     * @return the table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * gets the column name.
     * @return the column name
     */
    public String getColumnName() {
        return columnName;
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof QualifiedColumnName)) {
            return false;
        }

        QualifiedColumnName castedOther = (QualifiedColumnName) other;

        return tableName.equals(castedOther.tableName)
                && columnName.equals(castedOther.columnName);
    }

    @Override
    public int hashCode() {
        return tableName.hashCode() ^ columnName.hashCode();
    }
}
