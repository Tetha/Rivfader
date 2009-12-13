package edu.rivfader.errors;

import java.io.IOException;

/**
 * This error is raised if something bad happens during reading rows.
 * TODO: rename -> RowLoadError
 * @author harald
 */
public class ColumnLoadError extends RuntimeException {
    /**
     * constructs a new exception.
     * @param cause what went wrong.
     */
    public ColumnLoadError(IOException cause) {
        super(cause);
    }
}
