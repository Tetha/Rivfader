package edu.rivfader.relalg.rowselector;

import edu.rivfader.relalg.IQualifiedNameRow;

public class StubResult implements IRowSelector {
    private boolean[] result;
    int nextResult;
    public StubResult(boolean... pResult) {
        result = pResult;
        nextResult = 0;
    }

    public boolean getResult() {
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
