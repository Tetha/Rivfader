package edu.rivfader.relalg;

import edu.rivfader.data.Database;
import java.util.Iterator;
import java.util.Collection;
public class StubResult<R> implements IRelAlgExpr {

    R expectedResult;

    Collection<IQualifiedNameRow> internColl; // XXX: transition hack
    public StubResult(R pExpectedResult) {
        expectedResult = pExpectedResult;
    }

    // XXX: hack during transition
    public StubResult(Collection<IQualifiedNameRow> r) {
        expectedResult = (R) r.iterator();
        internColl = r;
    }

    public R getExpectedResult() {
        return expectedResult;
    }

    public Iterator<IQualifiedNameRow> evaluate(Database context) {
        return internColl.iterator();
    }
}
