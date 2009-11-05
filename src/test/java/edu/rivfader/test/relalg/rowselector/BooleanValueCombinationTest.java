package edu.rivfader.test.relalg.rowselector;

import edu.rivfader.relalg.rowselector.BooleanValueCombination;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class BooleanValueCombinationTest {
    @Test public void testAnd() {
        assertTrue(BooleanValueCombination.AND.combineValues(true, true));
        assertFalse(BooleanValueCombination.AND.combineValues(true, false));
        assertFalse(BooleanValueCombination.AND.combineValues(false, true));
        assertFalse(BooleanValueCombination.AND.combineValues(false, false));
    }

    @Test public void testOr() {
        assertTrue(BooleanValueCombination.OR.combineValues(true, true));
        assertTrue(BooleanValueCombination.OR.combineValues(true, false));
        assertTrue(BooleanValueCombination.OR.combineValues(false, true));
        assertFalse(BooleanValueCombination.OR.combineValues(false, false));
    }
}
