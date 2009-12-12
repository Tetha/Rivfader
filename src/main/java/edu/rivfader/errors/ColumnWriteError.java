package edu.rivfader.errors;

import java.io.IOException;

public class ColumnWriteError extends RuntimeException {
    public ColumnWriteError(String message) {
        super(message);
    }

    public ColumnWriteError(IOException cause) {
        super(cause);
    }
}
