package edu.rivfader.relalg.operations;

import edu.rivfader.relalg.representation.IRelAlgExpr;
/**
 * This transforms an IRelAlgTransformation into something else.
 */
public interface IRelAlgExprTransformation<R> {
    /**
     * transforms the given relalg expression.
     * @param input the IRelAlgExpr to transform
     * @return some result
     */
    public R transform(IRelAlgExpr input);
}
