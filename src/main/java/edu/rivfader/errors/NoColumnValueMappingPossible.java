package edu.rivfader.errors;

/**
 * This class is thrown if a command cannot map it's values
 * to the columns.
 *
 * This happens e.g. if there are not enoguh (or too many)
 * values.
 * @author harald
 */
public class NoColumnValueMappingPossible extends RuntimeException {
    public NoColumnValueMappingPossible(String s) {
        super(s);
    }
}
