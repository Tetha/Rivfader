package edu.rivfader.test.relalg.rowselector;

import edu.rivfader.relalg.rowselector.ValueComparer;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ValueComparerTest {
    @Test public void equals() {
        assertTrue(ValueComparer.EQUALS.isGoodValuePair("a","a"));
        assertFalse(ValueComparer.EQUALS.isGoodValuePair("a", "b"));
    }

    @Test public void notEquals() {
        assertTrue(ValueComparer.NOTEQUALS.isGoodValuePair("a", "b"));
        assertFalse(ValueComparer.NOTEQUALS.isGoodValuePair("a", "a"));
    }

    @Test public void less() {
        assertTrue(ValueComparer.LESS.isGoodValuePair("a", "b"));
        assertFalse(ValueComparer.LESS.isGoodValuePair("a", "a"));
        assertFalse(ValueComparer.LESS.isGoodValuePair("b", "a"));
    }

    @Test public void lessEquals() {
        assertTrue(ValueComparer.LESSEQUALS.isGoodValuePair("a", "b"));
        assertTrue(ValueComparer.LESSEQUALS.isGoodValuePair("a", "a"));
        assertFalse(ValueComparer.LESSEQUALS.isGoodValuePair("b", "a"));
    }

    @Test public void greaterEquals() {
        assertTrue(ValueComparer.GREATEREQUALS.isGoodValuePair("b", "a"));
        assertTrue(ValueComparer.GREATEREQUALS.isGoodValuePair("a", "a"));
        assertFalse(ValueComparer.GREATEREQUALS.isGoodValuePair("a", "b"));
    }

    @Test public void greater() {
        assertTrue(ValueComparer.GREATER.isGoodValuePair("b", "a"));
        assertFalse(ValueComparer.GREATER.isGoodValuePair("a", "a"));
        assertFalse(ValueComparer.GREATER.isGoodValuePair("a", "b"));
    }
}
