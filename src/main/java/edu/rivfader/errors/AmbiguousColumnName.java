package edu.rivfader.errors;

/**
 * Thrown if a column name is ambiguos.
 * @author harald
 */
public class AmbiguousColumnName extends RuntimeException {
    /**
     * creates a new error.
     * @param ambiguousName the column name in question.
     */
    public AmbiguousColumnName(final String ambiguousName) {
        super("Column name " + ambiguousName + " is ambiguous!");
    }
}
