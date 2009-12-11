package edu.rivfader.test.relalg;

import edu.rivfader.data.Row;
import edu.rivfader.relalg.QualifiedNameRow;
import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.QualifiedColumnName;
import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.errors.UnknownColumnName;
import edu.rivfader.errors.TableAmbiguous;
import edu.rivfader.errors.AmbiguousColumnName;

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.Collection;

public class QualifiedNameRowTest {
    @Test public void combiningConstructor() {
        IQualifiedColumnName n1 = new QualifiedColumnName("t", "r");
        IQualifiedColumnName n2 = new QualifiedColumnName("u", "s");

        IQualifiedNameRow first = new QualifiedNameRow(n1);
        first.setData(n1, "cow");

        QualifiedNameRow second = new QualifiedNameRow(n2);
        second.setData(n2, "chicken");

        QualifiedNameRow subject = new QualifiedNameRow(first, second);

        assertEquals(2, subject.columns().size());
        assertTrue(subject.columns().contains(n1));
        assertTrue(subject.columns().contains(n2));
        assertEquals("cow", subject.getData(n1));
        assertEquals("chicken", subject.getData(n2));
    }

    @Test public void columnsStored() {
        IQualifiedColumnName n1 = new QualifiedColumnName("t", "r");
        IQualifiedColumnName n2 = new QualifiedColumnName("u", "s");
        IQualifiedNameRow subject = new QualifiedNameRow(n1, n2);
        assertEquals(2, subject.columns().size());
        assertTrue(subject.columns().contains(n1));
        assertTrue(subject.columns().contains(n2));
    }

    @Test public void columnsIndependent() {
        IQualifiedColumnName n = new QualifiedColumnName("t", "r");
        IQualifiedNameRow subject = new QualifiedNameRow(n);
        subject.columns().remove(n);
        assertTrue(subject.columns().contains(n));
    }

    @Test public void existingUniqueNameResolved() {
        IQualifiedColumnName n = new QualifiedColumnName("t", "r");
        IQualifiedNameRow subject = new QualifiedNameRow(n);
        assertEquals(n, subject.resolveUnqualifiedName("r"));
    }

    @Test(expected = AmbiguousColumnName.class)
    public void ambiguousNameResolveFails() {
        IQualifiedNameRow subject = new QualifiedNameRow(
                                new QualifiedColumnName("t", "r"),
                                new QualifiedColumnName("u", "r"));
        subject.resolveUnqualifiedName("r");
    }

    @Test(expected = UnknownColumnName.class)
    public void missingNameResolveFails() {
        IQualifiedNameRow subject = new QualifiedNameRow();
        subject.resolveUnqualifiedName("r");
    }

    @Test public void setGetData() {
        IQualifiedColumnName n = new QualifiedColumnName("t", "r");
        IQualifiedNameRow subject = new QualifiedNameRow(n);
        subject.setData(n, "cow");
        assertEquals("cow", subject.getData(n));
    }

    @Test(expected = UnknownColumnName.class)
    public void setGetDataMissingColumn() {
        IQualifiedNameRow subject = new QualifiedNameRow();
        subject.setData(new QualifiedColumnName("t", "r"), "cow");
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDataColumnNameNullFail() {
        IQualifiedNameRow subject = new QualifiedNameRow();
        subject.getData(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setdataColumnNameNullFails() {
        IQualifiedNameRow subject = new QualifiedNameRow();
        subject.setData(null, "cow");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setGetDataDataNullFails() {
        IQualifiedNameRow subject = new QualifiedNameRow();
        subject.setData(new QualifiedColumnName("t", "r"), null);
    }

    @Test public void fromRowPositive() {
        Row source = new Row("foo", "bar", "baz");
        source.setData("foo", "foo");
        source.setData("bar", "bar");
        source.setData("baz", "baz");
        IQualifiedNameRow result = QualifiedNameRow.fromRow("t", source);
        Collection<IQualifiedColumnName> columns = result.columns();
        assertEquals(3, columns.size());
        assertTrue(columns.contains(new QualifiedColumnName("t", "foo")));
        assertTrue(columns.contains(new QualifiedColumnName("t", "bar")));
        assertTrue(columns.contains(new QualifiedColumnName("t", "baz")));
        assertEquals("foo", result.getData(new QualifiedColumnName("t", "foo")));
        assertEquals("bar", result.getData(new QualifiedColumnName("t", "bar")));
        assertEquals("baz", result.getData(new QualifiedColumnName("t", "baz")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromRowTableNullFails() {
        Row source = new Row();
        QualifiedNameRow.fromRow(null, source);
    }
    @Test(expected = IllegalArgumentException.class)
    public void fromRowSourceNullFails() {
        QualifiedNameRow.fromRow("foo", null);
    }

    @Test public void toRowPositive() {
        IQualifiedColumnName n1 = new QualifiedColumnName("t", "a");
        IQualifiedColumnName n2 = new QualifiedColumnName("t", "b");
        IQualifiedNameRow source = new QualifiedNameRow(n1, n2);
        source.setData(n1, "cow");
        source.setData(n2, "rabbit");

        Row result = QualifiedNameRow.toRow(source);

        assertEquals(2, result.columns().size());
        assertTrue(result.columns().contains("a"));
        assertTrue(result.columns().contains("b"));
        assertEquals("cow", result.getData("a"));
        assertEquals("rabbit", result.getData("b"));
    }

    @Test(expected = TableAmbiguous.class)
    public void toRowTablesDiffer() {
        IQualifiedNameRow source = new QualifiedNameRow(
                                    new QualifiedColumnName("t", "a"),
                                    new QualifiedColumnName("w", "b"));
        QualifiedNameRow.toRow(source);
    }
}
