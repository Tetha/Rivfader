package edu.rivfader.rowselector.operations;

import edu.rivfader.rowselector.representation.IRowSelector;
import edu.rivfader.relalg.representation.IQualifiedNameRow;

public class RowselectorStubResult<R> implements IRowSelector {
    private R[] result;
    int nextResult;
    public RowselectorStubResult(R... pResult) {
        result = pResult;
        nextResult = 0;
    }

    public R getResult() {
        if (nextResult == result.length) {
            throw new IllegalStateException("Stubresult exhausted");
        } else {
            try {
                return result[nextResult];
            } finally {
                nextResult++;
            }
        }
    }
}
