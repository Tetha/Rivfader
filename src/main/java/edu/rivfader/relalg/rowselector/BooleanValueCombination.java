package edu.rivfader.relalg.rowselector;

/**
 * This class contains the ways to combine boolean expressions.
 * @author harald
 */
public enum BooleanValueCombination {
    /**
     * implements the conjunction of two boolean values.
     */
    AND {
        @Override
        public boolean combineValues(final boolean first,
                                     final boolean second) {
            return first && second;
        }
    },
    /**
     * implements the disjunction of two boolean values.
     */
    OR {
        @Override
        public boolean combineValues(final boolean first,
                                     final boolean second) {
            return first || second;
        }
    };

    /**
     * This is called to combine two values.
     * @param first the left hand side of the operation
     * @param second the right hand side of the operation.
     * @return the combination of both values
     */
    public abstract boolean combineValues(boolean first, boolean second);
}
