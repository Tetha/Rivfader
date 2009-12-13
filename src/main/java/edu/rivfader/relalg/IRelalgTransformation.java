package edu.rivfader.relalg;

/**
 * This transforms an IRelAlgTransformation into something else.
 */
public interface IRelalgTransformation<R> {
    /**
     * transforms the given relalg expression.
     * @param input the IRelAlgExpr to transform
     * @return some result
     */
    public R transform(IRelAlgExpr input);
}
