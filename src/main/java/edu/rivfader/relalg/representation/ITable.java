package edu.rivfader.relalg.representation;

import edu.rivfader.data.Database;
import java.util.Iterator;
import java.util.List;


/**
 * an Itable is an abstraction of a table in the database.
 * @author harald
 */
public interface ITable extends IRelAlgExpr {
    /**
     * gets the name of the table.
     * @return the name of the table
     */
    String getName();

    /**
     * gets the columns of the table
     * @return the list of column names.
     */
    List<IQualifiedColumnName> getColumnNames();

    /**
     * sets the database the table is contained in.
     * Must be called before calling any database-related function.
     * @param context the database to operate in
     */
    void setDatabase(Database context);

    /**
     * opens the table for writing, necessary for store.
     */
    void openForWriting();

    /**
     * loads the rows of the table.
     */
    Iterator<IQualifiedNameRow> load();

    /**
     * convenience, so we can still use LoadTable in the relalg.
     * TODO: kick.
     */
    Iterator<IQualifiedNameRow> evaluate(Database context);

    /**
     * stores the row in the table. The table must be open for
     * writing.
     * @param newRow the new row to store.
     */
    void storeRow(IQualifiedNameRow newRow);

    /**
     * appends the row to the table. The table must not be open
     * for writing in this case.
     * @param newRow the new row to store.
     */
    void appendRow(IQualifiedNameRow newRow);

    /**
     * closes the table and writes the changes into the actual
     * database
     */
    void close();
}
