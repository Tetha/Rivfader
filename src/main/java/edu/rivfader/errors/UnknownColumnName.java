package edu.rivfader.errors;

/**
 * this error is raised if an unknown column name is referenced.
 * @author harald
 */
public class UnknownColumnName extends RuntimeException {
    /**
     * constructs a new exception
     * @param columnName the unknown column name
     */
    public UnknownColumnName(String columnName) {
        super("Unknown column name " + columnName);
    }

    /**
     * constructs a new exception
     * @param columnName the unknown column name
     * @param cause the cause of the exception
     */
    public UnknownColumnName(String columnName, Throwable cause) {
        super("Unknown column name " + columnName, cause);
    }
}
