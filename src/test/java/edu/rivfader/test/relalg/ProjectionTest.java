package edu.rivfader.test.relalg;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import static org.easymock.EasyMock.expect;

import edu.rivfader.data.Row;
import edu.rivfader.relalg.Projection;
import edu.rivfader.relalg.RelAlgExpr;

import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Row.class)
public class ProjectionTest {
    private Projection subject;
    private StringStringMap renamedFields;
    private StringSet selectedFields;
    private RelAlgExpr subExpression;

    private interface StringStringMap extends Map<String,String> {};
    private interface StringSet extends Set<String> {};

    @Before public void createSubject() {
        renamedFields = createMock(StringStringMap.class);
        selectedFields = createMock(StringSet.class);
        subExpression = createMock(RelAlgExpr.class);

    }

    @Test public void evaluateRenamedRowsAreMoved() {
        List<String> columns = new LinkedList<String>();
        columns.add("cow");
        columns.add("chicken");
        List<Row> previousRows = new LinkedList<Row>();
        Row first = createMock(Row.class);
        expect(first.columns()).andReturn(columns).anyTimes();
        expect(first.getData("chicken")).andReturn("wool").anyTimes();
        previousRows.add(first);

        expect(selectedFields.iterator())
            .andReturn(new LinkedList<String>().iterator());
        expect(renamedFields.containsKey("cow")).andReturn(false).anyTimes();
        expect(renamedFields.containsKey("chicken")).andReturn(true).anyTimes();
        expect(renamedFields.get("chicken")).andReturn("sheep").anyTimes();

        expect(subExpression.evaluate()).andReturn(previousRows.iterator());
        replayAll();
        subject = new Projection(subExpression, selectedFields, renamedFields);
        List<Row> gotRows = new LinkedList<Row>();
        Iterator<Row> resultRows = subject.evaluate();
        while(resultRows.hasNext()) {
            gotRows.add(resultRows.next());
        }
        List<String> expectedColumns = new LinkedList<String>();
        expectedColumns.add("sheep");
        Row expectedRow = new Row(expectedColumns);
        expectedRow.setData("sheep", "wool");
        assertEquals(expectedRow, gotRows.get(0));
        verifyAll();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void projectionEvaluationDoesNotRemove() {
        expect(selectedFields.iterator())
            .andReturn(new LinkedList<String>().iterator());
        expect(subExpression.evaluate())
            .andReturn(new LinkedList<Row>().iterator());

        replayAll();
        try {
            subject = new Projection(subExpression, selectedFields,
                    renamedFields);
            subject.evaluate().remove();
        } finally {
            verifyAll();
        }
    }

    @Test
    public void sSelectedFieldsAreTransformedCorrectly() {
        List<String> selectedFieldsContent = new LinkedList<String>();
        selectedFieldsContent.add("cow");
        selectedFieldsContent.add("chicken");
        expect(selectedFields.iterator())
            .andReturn(selectedFieldsContent.iterator());
        expect(renamedFields.put("cow", "cow")).andReturn(null);
        expect(renamedFields.put("chicken", "chicken")).andReturn(null);
        replayAll();
            subject = new Projection(subExpression, selectedFields,
                    renamedFields);
        verifyAll();
    }
}
