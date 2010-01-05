package edu.rivfader.relalg.operations;

import edu.rivfader.relalg.representation.IRelAlgExpr;

public interface IStubResult<R> extends IRelAlgExpr {
    R getExpectedResult();
}
