package edu.rivfader.errors;

import java.io.IOException;

public class ColumnLoadError extends RuntimeException {
    public ColumnLoadError(IOException cause) {
        super(cause);
    }
}
