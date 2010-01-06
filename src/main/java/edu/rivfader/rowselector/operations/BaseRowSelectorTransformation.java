package edu.rivfader.rowselector.operations;

import edu.rivfader.rowselector.representation.IRowSelector;
import edu.rivfader.rowselector.representation.BinaryOperation;
import edu.rivfader.rowselector.representation.Comparision;
import edu.rivfader.rowselector.representation.Always;

import java.util.Collection;

public abstract class BaseRowSelectorTransformation<R> {
    public R transform(IRowSelector r) {
        if (r instanceof BinaryOperation) {
            return transformBinaryOperation((BinaryOperation)r);
        } else if(r instanceof Comparision) {
            return transformComparision((Comparision)r);
        } else if (r instanceof Always) {
            return transformAlways((Always) r);
        }  else if (r instanceof RowselectorStubResult) {
            return ((RowselectorStubResult<R>) r)
                        .getResult();
        } else {
            throw new IllegalArgumentException("Unexpected type: " + r);
        }
    }

    protected abstract R transformBinaryOperation(BinaryOperation bo);
    protected abstract R transformComparision(Comparision c);
    protected abstract R transformAlways(Always a);
}
