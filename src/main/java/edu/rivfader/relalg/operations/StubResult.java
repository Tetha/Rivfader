package edu.rivfader.relalg.operations;

public class StubResult<R> implements IStubResult<R> {
    R expectedResult;

    public StubResult(R pExpectedResult) {
        expectedResult = pExpectedResult;
    }

    @Override
    public R getExpectedResult() {
        return expectedResult;
    }
}
