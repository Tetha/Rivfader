package edu.rivfader.errors;

public class UnknownColumnName extends RuntimeException {
    public UnknownColumnName(String columnName) {
        super("Unknown column name " + columnName);
    }

    public UnknownColumnName(String columnName, Throwable cause) {
        super("Unknown column name " + columnName, cause);
    }
}
