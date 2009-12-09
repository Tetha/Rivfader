package edu.rivfader.test.relalg;

import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.relalg.QualifiedColumnName;
import org.junit.Test;
import org.junit.Before;

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
     * checks that table name != null is required.
     */
    @Test(expected = IllegalArgumentException.class)
    public void tableNotNull() {
        new QualifiedColumnName(null, columnName);
    }

    /**
     * checks that the table name is stored.
     */
    @Test public void tableStored() {
        assertEquals(tableName, subject.getTable());
    }

    /**
     * checks that column name != null is required.
     */
    @Test(expected = IllegalArgumentException.class)
    public void columnNotNull() {
        new QualifiedColumnName(tableName, null);
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
        IQualifiedColumnName tableDiffers =
            new QualifiedColumnName(tableName + "1", columnName);
        IQualifiedColumnName columnDiffers =
            new QualifiedColumnName(tableName, columnName + "1");

        assertTrue(subject.equals(equalName));
        assertTrue(subject.hashCode() == equalName.hashCode());
        assertFalse(subject.equals(tableDiffers));
        assertFalse(subject.equals(columnDiffers));
        assertFalse(subject.equals("nonsense"));
    }
}
