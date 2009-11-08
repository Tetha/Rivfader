package edu.rivfader.data;

import java.util.Iterator;

/**
 * Class which encapsulates access to the database files.
 * @author harald
 */
public class Database {
    /**
     * loads the table and returns an iterator over the rows.
     * @param tableName the name of the table to load
     * @return a row iterator.
     */
    public Iterator<Row> loadTable(final String tableName) {
        return null;
    }

    /**
     * deletes a table if it exists.
     * @param tableName the name of the table to delete.
     */
    public void dropTable(final String tableName) {
    }
}
