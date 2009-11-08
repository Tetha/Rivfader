package edu.rivfader.test.data;

import edu.rivfader.data.Row;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import static org.easymock.EasyMock.createMock;

import java.util.List;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.HashSet;


/**
 * This class contains tests for edu.rivfader.data.Row.
 */
public class RowTest {
    private List<String> buildList(String[] values) {
        List<String> r = new LinkedList<String>();
        for(String v : values) {
            r.add(v);
        }
        return r;
    }
    @Test public void dataIsStored() {
        Row subject = new Row("cow");

        subject.setData("cow", "milk");
        assertEquals("milk", subject.getData("cow"));
    }

    @Test(expected = NoSuchElementException.class)
    public void wrongColumnNamesRaise() {
        Row subject = new Row();

        subject.setData("cow", "milk");
    }

    @Test(expected = NoSuchElementException.class)
    public void getRaises() {
        Row subject = new Row();

        subject.getData("cow");
    }

    private void assertIterableYields(Iterable<String> source,
                                      String... expectedValues) {
        Set<String> iteratedElements = new HashSet<String>();
        for(String e : source) iteratedElements.add(e);

        assertEquals(expectedValues.length, iteratedElements.size());
        for(String expected : expectedValues) {
            assertTrue(iteratedElements.contains(expected));
        }
    }
    @Test public void allColumnsIterated() {
        Row subject = new Row("cow", "chicken");
        assertIterableYields(subject.columns(), "cow", "chicken");
    }

    @Test public void creationByIterator() {
        List<String> columnNames = buildList(new String[] {"cow", "chicken"});
        Row subject = new Row(columnNames.iterator());
        assertIterableYields(subject.columns(), "cow", "chicken");
    }

    @Test public void creationByVarargs() {
        Row subject = new Row("cow", "chicken");
        assertIterableYields(subject.columns(), "cow", "chicken");
    }

    @Test public void equalityPositive() {
        Row subject = new Row("cow");
        Row identicalSubject = new Row("cow");

        subject.setData("cow", "milk");
        identicalSubject.setData("cow", "milk");

        assertTrue(subject.equals(identicalSubject));
        assertTrue(subject.hashCode() == identicalSubject.hashCode());
    }

    @Test
    public void equalityNegativeColumnNames() {
        Row first = new Row("cow");
        Row second = new Row();

        assertFalse(first.equals(second));
    }

    @Test
    public void equalityNegativeValues() {
        Row first = new Row("cow");
        Row second = new Row("cow");

        first.setData("cow", "milk");

        assertFalse(first.equals(second));
    }

    @Test
    public void testToString() {
        Row subject = new Row("cow");

        subject.setData("cow", "milk");

        assertEquals("Row(cow -> milk )", subject.toString());
    }

    @Test
    public void doesNotequalsNull() {
        Row subject = new Row("cow");

        assertFalse(subject.equals(null));
    }

    @Test
    public void doesNotEqualNonsense() {
        Row subject = new Row("cow");
        assertFalse(subject.equals("moo"));
    }
}
