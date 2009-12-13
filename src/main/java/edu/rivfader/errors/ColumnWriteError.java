package edu.rivfader.errors;

import java.io.IOException;

/**
 * This error is raised if something bad happens during writing a row.
 * TODO: rename -> RowWriteError
 * @author harald.
 */
public class ColumnWriteError extends RuntimeException {
    /**
     * constructs a new exception.
     * @param message a reason why the write failed.
     */
    public ColumnWriteError(String message) {
        super(message);
    }

    /**
     * Constructs a new exception.
     * @param cause the IOException that caused the error
     */
    public ColumnWriteError(IOException cause) {
        super(cause);
    }
}
