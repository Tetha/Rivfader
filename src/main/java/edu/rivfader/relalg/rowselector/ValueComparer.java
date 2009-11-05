package edu.rivfader.relalg.rowselector;

/**
 * This class implements the comparision of two values.
 * @author harald
 */
public enum ValueComparer {
    /**
     * implements the equality check.
     */
    EQUALS {
        @Override
        public boolean isGoodValuePair(final String first,
                                       final String second) {
            return first.compareTo(second) == 0;
        }
    },
    /**
     * implements the inequality check.
     */
    NOTEQUALS {
        @Override
        public boolean isGoodValuePair(final String first,
                                       final String second) {
            return first.compareTo(second) != 0;
        }
    },
    /**
     * implements the check for less-than.
     */
    LESS {
        @Override
        public boolean isGoodValuePair(final String first,
                                       final String second) {
            return first.compareTo(second) < 0;
        }
    },
    /**
     * implements the check for less-than or equal.
     */
    LESSEQUALS {
        @Override
        public boolean isGoodValuePair(final String first,
                                       final String second) {
            return first.compareTo(second) <= 0;
        }
    },
    /**
     * implements the check for greater-than or equal.
     */
    GREATEREQUALS {
        @Override
        public boolean isGoodValuePair(final String first,
                                       final String second) {
            return first.compareTo(second) >= 0;
        }
    },
    /**
     * implements the check for greater-than.
     */
    GREATER {
        @Override
        public boolean isGoodValuePair(final String first,
                                       final String second) {
            return first.compareTo(second) > 0;
        }
    };

    /**
     * checks if the two parameters agree according to the relation.
     * @param firstValue the first operand value
     * @param secondValue the second operand value
     * @return true if the two parameters agree according to the relation
     */
    public abstract boolean isGoodValuePair(String firstValue,
                                            String secondValue);
}
