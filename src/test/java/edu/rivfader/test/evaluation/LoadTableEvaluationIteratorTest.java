package edu.rivfader.test.evaluation;

import edu.rivfader.data.Row;
import edu.rivfader.relalg.IQualifiedNameRow;
import edu.rivfader.relalg.QualifiedColumnName;
import edu.rivfader.relalg.IQualifiedColumnName;
import edu.rivfader.evaluation.LoadTableEvaluationIterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

import org.powermock.modules.junit4.PowerMockRunner;

import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.easymock.EasyMock.expect;

import java.util.List;
import java.util.LinkedList;

import java.io.IOException;

@RunWith(PowerMockRunner.class)
public class LoadTableEvaluationIteratorTest {
    @Test public void transformLoadTable() throws IOException {
        String tn; // table name
        List<Row> dbrs; // database rows
        Row fir; // first row returned from database
        LoadTableEvaluationIterator s; // subject
        IQualifiedNameRow frr; // first result row

        tn = "table";
        dbrs = new LinkedList<Row>();
        fir= new Row("foo", "bar");
        fir.setData("foo", "foo");
        fir.setData("bar", "bar");
        dbrs.add(fir);

        s = new LoadTableEvaluationIterator(dbrs.iterator(), tn);
        frr = s.next();
        assertThat(frr.columns().size(), is(equalTo(2)));
        assertThat(frr.columns(),
            hasItem((IQualifiedColumnName)new QualifiedColumnName(tn, "foo")));
        assertThat(frr.columns(),
            hasItem((IQualifiedColumnName)new QualifiedColumnName(tn, "bar")));
        assertThat(frr.getData(new QualifiedColumnName(tn, "foo")),
                        is(equalTo("foo")));
        assertThat(frr.getData(new QualifiedColumnName(tn, "bar")),
                        is(equalTo("bar")));
        assertThat(s.hasNext(), is(false));
    }
}
