package edu.rivfader.test.relalg.representation;

import edu.rivfader.relalg.representation.IQualifiedColumnName;
import edu.rivfader.relalg.representation.QualifiedColumnName;
import org.junit.Test;
import org.junit.Before;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

public class QualifiedColumnNameTest {
    private String tableName = "table";
    private String columnName = "column";
    private IQualifiedColumnName subject;

    /**
     * initializes subject.
     */
    @Before
    public void createSubject() {
        subject = new QualifiedColumnName(tableName, columnName);
    }

    /**
     * checks that the table name is stored.
     */
    @Test public void tableStored() {
        assertEquals(tableName, subject.getTable());
    }

    /**
     * checks that the column name is stored.
     */
    @Test public void columnNameStored() {
        assertEquals(columnName, subject.getColumn());
    }

    /**
     * checks equality.
     */
    @Test public void equalityHashcode() {
        IQualifiedColumnName equalName = new QualifiedColumnName(tableName,
                                                                 columnName);
        IQualifiedColumnName unqualifiedEqualName = new QualifiedColumnName(
                                            null, columnName);

        IQualifiedColumnName tableDiffers =
            new QualifiedColumnName(tableName + "1", columnName);
        IQualifiedColumnName columnDiffers =
            new QualifiedColumnName(tableName, columnName + "1");

        assertTrue(subject.equals(equalName));
        assertTrue(subject.hashCode() == equalName.hashCode());
        assertTrue(subject.equals(unqualifiedEqualName));
        assertTrue(subject.hashCode() == unqualifiedEqualName.hashCode());
        assertFalse(subject.equals(tableDiffers));
        assertFalse(subject.equals(columnDiffers));
        assertFalse(subject.equals("nonsense"));
    }

    /**
     * checks ordering.
     */
    @Test public void orderingCheck() {
        String stn; // small table name
        String ltn; // large table name
        String scn; // small column name
        String lcn; // large column name
        IQualifiedColumnName tscs; // table small column small
        IQualifiedColumnName tscl; // table small column large
        IQualifiedColumnName tlcs; // table large column small
        IQualifiedColumnName tlcl; // table large column large

        stn = "a";
        ltn = "x";
        scn = "a";
        lcn = "x";

        assertThat(stn.compareTo(ltn), is(lessThan(0)));
        assertThat(scn.compareTo(lcn), is(lessThan(0)));
        tscs = new QualifiedColumnName(stn, scn);
        tscl = new QualifiedColumnName(stn, lcn);
        tlcs = new QualifiedColumnName(ltn, scn);
        tlcl = new QualifiedColumnName(ltn, lcn);

        assertThat(tscs.compareTo(tscs), is(equalTo(0)));
        assertThat(tscs.compareTo(tscl), is(lessThan(0)));
        assertThat(tscs.compareTo(tlcs), is(lessThan(0)));
        assertThat(tscs.compareTo(tlcl), is(lessThan(0)));

        assertThat(tscl.compareTo(tscs), is(greaterThan(0)));
        assertThat(tscl.compareTo(tscl), is(equalTo(0)));
        assertThat(tscl.compareTo(tlcs), is(lessThan(0)));
        assertThat(tscl.compareTo(tlcl), is(lessThan(0)));

        assertThat(tlcs.compareTo(tscs), is(greaterThan(0)));
        assertThat(tlcs.compareTo(tscl), is(greaterThan(0)));
        assertThat(tlcs.compareTo(tlcs), is(equalTo(0)));
        assertThat(tlcs.compareTo(tlcl), is(lessThan(0)));

        assertThat(tlcl.compareTo(tscs), is(greaterThan(0)));
        assertThat(tlcl.compareTo(tscl), is(greaterThan(0)));
        assertThat(tlcl.compareTo(tlcs), is(greaterThan(0)));
        assertThat(tlcl.compareTo(tlcl), is(equalTo(0)));
    }
}
