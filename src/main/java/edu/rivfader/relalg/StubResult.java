package edu.rivfader.relalg;

import edu.rivfader.data.Database;
import java.util.Iterator;

public class StubResult<R> implements IRelAlgExpr {
    R expectedResult;

    public StubResult(R pExpectedResult) {
        expectedResult = pExpectedResult;
    }

    public R getExpectedResult() {
        return expectedResult;
    }

    public Iterator<IQualifiedNameRow> evaluate(Database context) {
        return null;
    }
}
