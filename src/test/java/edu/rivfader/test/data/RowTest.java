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
        List<String> columnNames = buildList(new String[] {"cow"});
        Row subject = new Row(columnNames);

        subject.setData("cow", "milk");
        assertEquals("milk", subject.getData("cow"));
    }

    @Test(expected = NoSuchElementException.class)
    public void wrongColumnNamesRaise() {
        List<String> columnNames = buildList(new String[] {});

        Row subject = new Row(columnNames);

        subject.setData("cow", "milk");
    }

    @Test(expected = NoSuchElementException.class)
    public void getRaises() {
        List<String> columnNames = buildList(new String[] {});

        Row subject = new Row(columnNames);

        subject.getData("cow");
    }

    @Test public void allColumnsIterated() {
        List<String> columnNames = buildList(new String[] {"cow", "chicken"});

        Row subject = new Row(columnNames);

        Set<String> iteratedNames = new HashSet<String>();
        for(String s : subject.columns()) iteratedNames.add(s);
        assertTrue(columnNames.containsAll(iteratedNames));
        assertTrue(iteratedNames.containsAll(columnNames));
    }

    @Test public void creationByIterator() {
        List<String> columnNames = buildList(new String[] {"cow", "chicken"});

        Row subject = new Row(columnNames.iterator());
        Set<String> iteratedNames = new HashSet<String>();
        for(String e : subject.columns()) iteratedNames.add(e);
        assertTrue(columnNames.containsAll(iteratedNames));
        assertTrue(iteratedNames.containsAll(columnNames));
    }

    @Test public void equalityPositive() {
        List<String> columnNames = buildList(new String[] {"cow"});
        List<String> identicalColumnNames = buildList(new String[] {"cow"});

        Row subject = new Row(columnNames);
        Row identicalSubject = new Row(identicalColumnNames);

        subject.setData("cow", "milk");
        identicalSubject.setData("cow", "milk");

        assertTrue(subject.equals(identicalSubject));
        assertTrue(subject.hashCode() == identicalSubject.hashCode());
    }

    @Test
    public void equalityNegativeColumnNames() {
        List<String> columnNames = buildList(new String[] {"cow"});
        List<String> differentColumnNames = buildList(new String[] {});

        Row first = new Row(columnNames);
        Row second = new Row(differentColumnNames);

        assertFalse(first.equals(second));
    }

    @Test
    public void equalityNegativeValues() {
        List<String> columnNames = buildList(new String[] {"cow"});
        List<String> sameColumnNames = buildList(new String[] {"cow"});

        Row first = new Row(columnNames);
        Row second = new Row(sameColumnNames);

        first.setData("cow", "milk");

        assertFalse(first.equals(second));
    }

    @Test
    public void testToString() {
        List<String> columnNames = buildList(new String[] {"cow"});

        Row subject = new Row(columnNames);

        subject.setData("cow", "milk");

        assertEquals("Row(cow -> milk )", subject.toString());
    }

    @Test
    public void doesNotequalsNull() {
        List<String> columnNames = buildList(new String[] {"cow"});

        Row subject = new Row(columnNames);

        assertFalse(subject.equals(null));
    }

    @Test
    public void doesNotEqualNonsense() {
        List<String> columnNames = buildList(new String[] {"cow"});

        Row subject = new Row(columnNames);
        assertFalse(subject.equals("moo"));
    }
}
