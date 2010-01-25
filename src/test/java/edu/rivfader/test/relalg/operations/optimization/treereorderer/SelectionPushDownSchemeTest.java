package edu.rivfader.test.relalg.operations.optimization.treereorderer;

import edu.rivfader.relalg.operations.optimization.treereorderer.SelectionPushDownScheme;
import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.rowselector.representation.IRowSelector;
import static org.junit.Assert.assertTrue;
import edu.rivfader.relalg.representation.IRelAlgExpr;
import edu.rivfader.relalg.representation.Projection;
import static org.junit.Assert.assertFalse;
import edu.rivfader.relalg.representation.IColumnProjection;
import java.util.Collection;
import java.util.HashSet;
import edu.rivfader.relalg.operations.optimization.treereorderer.IReorderingScheme;
import static org.junit.Assert.assertEquals;
import edu.rivfader.relalg.representation.Product;
import edu.rivfader.relalg.representation.IQualifiedColumnName;
import edu.rivfader.rowselector.operations.RowselectorStubResult;
import edu.rivfader.relalg.operations.StubResult;
import org.powermock.api.easymock.annotation.Mock;
import edu.rivfader.data.Database;

@RunWith(PowerMockRunner.class)
public class SelectionPushDownSchemeTest {
    @Mock Database database;
    SelectionPushDownScheme subject;

    @Before public void createSubject() {
        subject = new SelectionPushDownScheme(database);
    }

    @Test public void acceptsSelectionIsTrue() {
        IRowSelector predicate = createMock(IRowSelector.class);
        IRelAlgExpr subExpression = createMock(IRelAlgExpr.class);
        Selection input = new Selection(predicate, subExpression);
        replayAll();
        assertTrue(subject.acceptsSelection(input));
        verifyAll();
    }

    @Test public void acceptsProjectionIsFalse() {
        Collection<IColumnProjection> projections
            = new HashSet<IColumnProjection>();
        IRelAlgExpr subExpression = createMock(IRelAlgExpr.class);
        Projection input = new Projection(subExpression, projections);
        replayAll();
        assertFalse(subject.acceptsProjection(input));
        verifyAll();
    }

    @Test public void moveAlongSelectionIsTrivialDown() {
        IRowSelector alongPredicate = createMock(IRowSelector.class);
        IRowSelector predicate = createMock(IRowSelector.class);
        IRelAlgExpr alongSubExpression = createMock(IRelAlgExpr.class);
        IRelAlgExpr subExpression = createMock(IRelAlgExpr.class);
        Selection accepted = new Selection(predicate, subExpression);
        subject.acceptsSelection(accepted);
        Selection along = new Selection(alongPredicate, alongSubExpression);
        replayAll();
        assertEquals(IReorderingScheme.ActionAlongUnaryOperation.TRIVIAL_MOVE_DOWN,
                     subject.moveAlongSelection(along));
        verifyAll();
    }

    @Test public void moveAlongProjectionIsDown() {
        Collection<IColumnProjection> alongProjections
            = new HashSet<IColumnProjection>();
        IRelAlgExpr alongSubExpression = createMock(IRelAlgExpr.class);
        Projection alongExpression = new Projection(alongSubExpression,
                                                    alongProjections);


        IRowSelector predicate = createMock(IRowSelector.class);
        IRelAlgExpr subExpression = createMock(IRelAlgExpr.class);
        Selection accepted = new Selection(predicate, subExpression);

        subject.acceptsSelection(accepted);
        replayAll();
        assertEquals(IReorderingScheme.ActionAlongUnaryOperation.MOVE_DOWN,
                     subject.moveAlongProjection(alongExpression));
    }

    @Test public void alongProductTowardsLeft() {
        IQualifiedColumnName requiredColumn = createMock(IQualifiedColumnName.class);
        Collection<IQualifiedColumnName> leftProvidedColumns
            = new HashSet<IQualifiedColumnName>();
        leftProvidedColumns.add(requiredColumn);
        Collection<IQualifiedColumnName> rightProvidedColumns
            = new HashSet<IQualifiedColumnName>();

        IRelAlgExpr left =
            new StubResult<Collection<IQualifiedColumnName>>
                (leftProvidedColumns);
        IRelAlgExpr right =
            new StubResult<Collection<IQualifiedColumnName>>
                (rightProvidedColumns);

        Collection<IQualifiedColumnName> requiredColumns =
            new HashSet<IQualifiedColumnName>();
        requiredColumns.add(requiredColumn);
        IRowSelector predicate =
            new RowselectorStubResult<Collection<IQualifiedColumnName>>(requiredColumns);

        Collection<IQualifiedColumnName> requiredColumnsSubExpr
            = new HashSet<IQualifiedColumnName>();

        IRelAlgExpr subExpression =
            new StubResult<Collection<IQualifiedColumnName>>
            (requiredColumnsSubExpr);
        Product input = new Product(left, right);
        Selection acceptedSelection = new Selection(predicate, subExpression);
        replayAll();
        subject.acceptsSelection(acceptedSelection);
        assertEquals(IReorderingScheme.ActionAlongBinaryOperation.MOVE_LEFT,
                     subject.moveAlongProduct(input));
        verifyAll();
    }

    @Test public void alongProductTowardsRight() {
        IQualifiedColumnName requiredColumn = createMock(IQualifiedColumnName.class);
        Collection<IQualifiedColumnName> leftProvidedColumns
            = new HashSet<IQualifiedColumnName>();
        Collection<IQualifiedColumnName> rightProvidedColumns
            = new HashSet<IQualifiedColumnName>();
        rightProvidedColumns.add(requiredColumn);

        IRelAlgExpr left =
            new StubResult<Collection<IQualifiedColumnName>>
                (leftProvidedColumns);
        IRelAlgExpr right =
            new StubResult<Collection<IQualifiedColumnName>>
                (rightProvidedColumns);

        Collection<IQualifiedColumnName> requiredColumns =
            new HashSet<IQualifiedColumnName>();
        requiredColumns.add(requiredColumn);
        IRowSelector predicate =
            new RowselectorStubResult<Collection<IQualifiedColumnName>>(requiredColumns);

        Collection<IQualifiedColumnName> requiredColumnsSubExpr
            = new HashSet<IQualifiedColumnName>();

        IRelAlgExpr subExpression =
            new StubResult<Collection<IQualifiedColumnName>>
            (requiredColumnsSubExpr);
        Product input = new Product(left, right);
        Selection acceptedSelection = new Selection(predicate, subExpression);
        replayAll();
        subject.acceptsSelection(acceptedSelection);
        assertEquals(IReorderingScheme.ActionAlongBinaryOperation.MOVE_RIGHT,
                     subject.moveAlongProduct(input));
        verifyAll();
    }

    @Test public void alongProductStop() {
        IQualifiedColumnName requiredColumn = createMock(IQualifiedColumnName.class);
        IQualifiedColumnName otherRequiredColumn = createMock(IQualifiedColumnName.class);
        Collection<IQualifiedColumnName> leftProvidedColumns
            = new HashSet<IQualifiedColumnName>();
        leftProvidedColumns.add(otherRequiredColumn);
        Collection<IQualifiedColumnName> rightProvidedColumns
            = new HashSet<IQualifiedColumnName>();
        rightProvidedColumns.add(requiredColumn);

        IRelAlgExpr left =
            new StubResult<Collection<IQualifiedColumnName>>
                (leftProvidedColumns);
        IRelAlgExpr right =
            new StubResult<Collection<IQualifiedColumnName>>
                (rightProvidedColumns);

        Collection<IQualifiedColumnName> requiredColumns =
            new HashSet<IQualifiedColumnName>();
        requiredColumns.add(requiredColumn);
        requiredColumns.add(otherRequiredColumn);
        IRowSelector predicate =
            new RowselectorStubResult<Collection<IQualifiedColumnName>>(requiredColumns);

        Collection<IQualifiedColumnName> requiredColumnsSubExpr
            = new HashSet<IQualifiedColumnName>();

        IRelAlgExpr subExpression =
            new StubResult<Collection<IQualifiedColumnName>>
            (requiredColumnsSubExpr);
        Product input = new Product(left, right);
        Selection acceptedSelection = new Selection(predicate, subExpression);
        replayAll();
        subject.acceptsSelection(acceptedSelection);
        assertEquals(IReorderingScheme.ActionAlongBinaryOperation.NO_OPERATION,
                     subject.moveAlongProduct(input));
        verifyAll();
    }

    @Ignore @Test public void alongProductResolveUnqualifiedNames() {
    }
}
