package edu.rivfader.test.relalg.operations;

import edu.rivfader.relalg.operations.RequiredColumnsAnalysis;
import edu.rivfader.relalg.representation.IRelAlgExpr;
import edu.rivfader.relalg.representation.LoadTable;
import edu.rivfader.relalg.representation.ITable;
import edu.rivfader.relalg.representation.LoadTable;
import edu.rivfader.relalg.representation.RenameTable;
import edu.rivfader.relalg.representation.IQualifiedColumnName;
import edu.rivfader.relalg.operations.StubResult;
import edu.rivfader.relalg.representation.Product;
import edu.rivfader.rowselector.representation.IRowSelector;
import edu.rivfader.rowselector.operations.RowselectorStubResult;
import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.relalg.representation.IColumnProjection;
import edu.rivfader.relalg.representation.Projection;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class RequiredColumnsAnalysisTest {
    @Test public void transformLoadTable() {
        RequiredColumnsAnalysis subject = new RequiredColumnsAnalysis();
        IRelAlgExpr input = new LoadTable("does not matter");

        assertTrue(subject.transform(input).isEmpty());
    }

    @Test public void transformRenameTable() {
        RequiredColumnsAnalysis subject = new RequiredColumnsAnalysis();
        ITable baseTable = createMock(ITable.class);
        IRelAlgExpr input = new RenameTable(baseTable, "irrelevant");
        assertTrue(subject.transform(input).isEmpty());
    }

    @Test public void transformProduct() {
        Collection<IQualifiedColumnName> leftResult
            = new LinkedList<IQualifiedColumnName>();
        leftResult.add(createMock(IQualifiedColumnName.class));

        Collection<IQualifiedColumnName> rightResult
            = new LinkedList<IQualifiedColumnName>();
        rightResult.add(createMock(IQualifiedColumnName.class));

        IRelAlgExpr left = new StubResult(leftResult);
        IRelAlgExpr right = new StubResult(rightResult);

        IRelAlgExpr input = new Product(left, right);

        replayAll();
        RequiredColumnsAnalysis subject = new RequiredColumnsAnalysis();
        Collection<IQualifiedColumnName> output = subject.transform(input);
        assertTrue(output.containsAll(leftResult));
        assertTrue(output.containsAll(rightResult));
        verifyAll();
    }

    @Test public void transformSelect() {
        Collection<IQualifiedColumnName> predicateResult
            = new LinkedList<IQualifiedColumnName>();
        predicateResult.add(createMock(IQualifiedColumnName.class));

        Collection<IQualifiedColumnName> subResult
            = new LinkedList<IQualifiedColumnName>();
        subResult.add(createMock(IQualifiedColumnName.class));

        IRowSelector predicate = new RowselectorStubResult(predicateResult);
        IRelAlgExpr subExpression = new StubResult(subResult);

        IRelAlgExpr input = new Selection(predicate, subExpression);

        replayAll();
        RequiredColumnsAnalysis subject = new RequiredColumnsAnalysis();
        Collection<IQualifiedColumnName> output = subject.transform(input);
        assertTrue(output.containsAll(subResult));
        verifyAll();
    }

    @Test public void transformProject() {
        IColumnProjection projection = createMock(IColumnProjection.class);
        Collection<IColumnProjection> projections =
            new LinkedList<IColumnProjection>();
        projections.add(projection);
        Collection<IQualifiedColumnName> subResult
            = new LinkedList<IQualifiedColumnName>();
        subResult.add(createMock(IQualifiedColumnName.class));

        IRelAlgExpr subExpression =
            new StubResult<Collection<IQualifiedColumnName>>(subResult);

        IRelAlgExpr input = new Projection(subExpression, projections);
        replayAll();
        RequiredColumnsAnalysis subject = new RequiredColumnsAnalysis();
        Collection<IQualifiedColumnName> output = subject.transform(input);
        assertTrue(output.containsAll(subResult));
        verifyAll();
    }
}
