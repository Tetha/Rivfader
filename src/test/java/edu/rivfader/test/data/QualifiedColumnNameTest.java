package edu.rivfader.test.data;

import edu.rivfader.data.QualifiedColumnName;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

public class QualifiedColumnNameTest {
    @Test
    public void columnNameIsStored() {
        String tableName = "table";
        String columnName = "column";
        QualifiedColumnName subject = new QualifiedColumnName(tableName,
                                                              columnName);
        assertEquals(columnName, subject.getColumnName());
    }

    @Test
    public void tableNameIsStored() {
        String tableName = "table";
        String columnName = "column";
        QualifiedColumnName subject = new QualifiedColumnName(tableName,
                                                              columnName);
        assertEquals(tableName, subject.getTableName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void columnNameNullFails() {
        new QualifiedColumnName("table", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void tableNameNullFails() {
        new QualifiedColumnName(null, "column");
    }

    @Test
    public void equalsHashCode() {
        QualifiedColumnName subject = new QualifiedColumnName("t", "c");
        QualifiedColumnName equal = new QualifiedColumnName("t", "c");
        QualifiedColumnName tableDiffers = new QualifiedColumnName("u", "c");
        QualifiedColumnName columnDiffers = new QualifiedColumnName("t", "d");

        assertTrue(subject.equals(equal));
        assertTrue(subject.hashCode() == equal.hashCode());
        assertFalse(subject.equals(tableDiffers));
        assertFalse(subject.equals(columnDiffers));
        assertFalse(subject.equals("nonsense"));
    }
}
