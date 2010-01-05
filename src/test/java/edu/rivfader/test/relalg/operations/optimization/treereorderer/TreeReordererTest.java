package edu.rivfader.test.relalg.operation.optimization.treereorderer;

import edu.rivfader.relalg.operations.optimization.treereorderer.TreeReorderer;
import edu.rivfader.relalg.operations.optimization.treereorderer.IReorderingScheme;
import edu.rivfader.relalg.representation.IRelAlgExpr;
import edu.rivfader.relalg.operations.IStubResult;
import edu.rivfader.relalg.operations.StubResult;
import edu.rivfader.rowselector.representation.IRowSelector;
import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.relalg.representation.Projection;
import edu.rivfader.relalg.representation.Product;
import edu.rivfader.relalg.representation.LoadTable;
import edu.rivfader.relalg.representation.RenameTable;
import edu.rivfader.relalg.representation.ITable;
import edu.rivfader.relalg.representation.IColumnProjection;
import java.util.Collection;

import org.junit.Test;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import org.powermock.api.easymock.annotation.Mock;

@RunWith(PowerMockRunner.class)
public class TreeReordererTest {
    @Mock private IReorderingScheme schemeMock;
    @Mock private IRelAlgExpr subResult;
    private IRelAlgExpr subExpression;

    private interface ExpressionSubresult extends IStubResult<IRelAlgExpr> {
    }
    private interface ProjectionCollection extends Collection<IColumnProjection> {
    }

    @Before
    public void createSubResult() {
        subExpression = new StubResult<IRelAlgExpr>(subResult);
    }
    @Test
    public void testMultipleSequencesTriedIfNoneAccepted() {
        IRowSelector predicate = createMock(IRowSelector.class);
        IRowSelector predicate2 = createMock(IRowSelector.class);

        Selection innerSelection = new Selection(predicate2, subExpression);
        Selection outerSelection = new Selection(predicate, innerSelection);
        expect(schemeMock.acceptsSelection(outerSelection)).andReturn(false);
        expect(schemeMock.acceptsSelection(innerSelection)).andReturn(false);

        replayAll();
        TreeReorderer subject = new TreeReorderer(schemeMock);
        IRelAlgExpr output = subject.optimize(outerSelection);
        verifyAll();

        assertThat(output, is(instanceOf(Selection.class)));
        Selection outerOutput = (Selection) output;
        assertThat(outerOutput.getSubExpression(),
                    is(instanceOf(Selection.class)));
        Selection innerOutput = (Selection) outerOutput.getSubExpression();

        assertThat(outerOutput.getPredicate(), is(equalTo(predicate)));
        assertThat(innerOutput.getPredicate(), is(equalTo(predicate2)));
        assertThat(innerOutput.getSubExpression(), is(equalTo(subResult)));
        assertThat(new Boolean(subject.wasTrivial()), is(true));
    }

    @Test
    public void testSelectionsAreMovedTrivialityRemains() {
        IRowSelector movedPredicate = createMock(IRowSelector.class);
        IRowSelector outerPredicate = createMock(IRowSelector.class);
        IRowSelector innerPredicate = createMock(IRowSelector.class);

        Selection innerSelection = new Selection(innerPredicate,
                                                 subExpression);
        Selection outerSelection = new Selection(outerPredicate,
                                                 innerSelection);
        Selection movedSelection = new Selection(movedPredicate,
                                                 outerSelection);

        expect(schemeMock.acceptsSelection(movedSelection)).andReturn(true);
        expect(schemeMock.moveAlongSelection(outerSelection))
            .andReturn(IReorderingScheme
                            .ActionAlongUnaryOperation.TRIVIAL_MOVE_DOWN);
        expect(schemeMock.moveAlongSelection(innerSelection))
            .andReturn(IReorderingScheme
                        .ActionAlongUnaryOperation.NO_OPERATION);

        replayAll();
        TreeReorderer subject = new TreeReorderer(schemeMock);
        IRelAlgExpr output = subject.optimize(movedSelection);
        verifyAll();

        assertThat(output, is(instanceOf(Selection.class)));
        Selection outerOutput = (Selection) output;
        assertThat(outerOutput.getSubExpression(),
                    is(instanceOf(Selection.class)));
        Selection movedOutput = (Selection) outerOutput.getSubExpression();
        assertThat(movedOutput.getSubExpression(),
                    is(instanceOf(Selection.class)));
        Selection innerOutput = (Selection) movedOutput.getSubExpression();
        assertThat(innerOutput.getSubExpression(), is(equalTo(subExpression)));

        assertThat(outerOutput.getPredicate(), is(equalTo(outerPredicate)));
        assertThat(movedOutput.getPredicate(), is(equalTo(movedPredicate)));
        assertThat(innerOutput.getPredicate(), is(equalTo(innerPredicate)));

        assertThat(new Boolean(subject.wasTrivial()), is(true));
    }

    @Test
    public void testSelectionsAreMovedAlongProjections() {
        IRowSelector movedPredicate = createMock(IRowSelector.class);
        Collection<IColumnProjection> outerProjections =
            createMock(ProjectionCollection.class);
        Collection<IColumnProjection> innerProjections =
            createMock(ProjectionCollection.class);

        Projection innerProjection = new Projection(subExpression,
                                                    innerProjections);
        Projection outerProjection = new Projection(innerProjection,
                                                    outerProjections);
        Selection movedSelection = new Selection(movedPredicate,
                                                    outerProjection);

        expect(schemeMock.acceptsSelection(movedSelection)).andReturn(true);
        expect(schemeMock.moveAlongProjection(outerProjection))
            .andReturn(IReorderingScheme
                            .ActionAlongUnaryOperation.MOVE_DOWN);
        expect(schemeMock.moveAlongProjection(innerProjection))
            .andReturn(IReorderingScheme
                        .ActionAlongUnaryOperation.NO_OPERATION);

        replayAll();
        TreeReorderer subject = new TreeReorderer(schemeMock);
        IRelAlgExpr output = subject.optimize(movedSelection);
        verifyAll();

        assertThat(output, is(instanceOf(Projection.class)));
        Projection outerOutput = (Projection) output;
        assertThat(outerOutput.getSubExpression(),
                    is(instanceOf(Selection.class)));
        Selection movedOutput = (Selection) outerOutput.getSubExpression();
        assertThat(movedOutput.getSubExpression(),
                    is(instanceOf(Projection.class)));
        Projection innerOutput = (Projection) movedOutput.getSubExpression();
        assertThat(innerOutput.getSubExpression(), is(equalTo(subExpression)));

        assertThat(outerOutput.getSelectedFields(),
                    is(equalTo(outerProjections)));
        assertThat(movedOutput.getPredicate(), is(equalTo(movedPredicate)));
        assertThat(innerOutput.getSelectedFields(),
                    is(equalTo(innerProjections)));

        assertThat(new Boolean(subject.wasTrivial()), is(false));
    }

    @Test
    public void testProjectionsAreMovedAlongSelections() {
        Collection<IColumnProjection> movedProjections =
            createMock(ProjectionCollection.class);
        IRowSelector outerPredicate = createMock(IRowSelector.class);
        IRowSelector innerPredicate = createMock(IRowSelector.class);

        Selection innerSelection = new Selection(innerPredicate,
                                                 subExpression);
        Selection outerSelection = new Selection(outerPredicate,
                                                 innerSelection);
        Projection movedProjection = new Projection(outerSelection,
                                                    movedProjections);

        expect(schemeMock.acceptsProjection(movedProjection)).andReturn(true);
        expect(schemeMock.moveAlongSelection(outerSelection))
            .andReturn(IReorderingScheme.ActionAlongUnaryOperation.MOVE_DOWN);
        expect(schemeMock.moveAlongSelection(innerSelection))
            .andReturn(IReorderingScheme.
                        ActionAlongUnaryOperation.NO_OPERATION);

        replayAll();
        TreeReorderer subject = new TreeReorderer(schemeMock);
        IRelAlgExpr output = subject.optimize(movedProjection);
        verifyAll();

        assertThat(output, is(instanceOf(Selection.class)));
        Selection outerOutput = (Selection) output;
        assertThat(outerOutput.getSubExpression(),
                    is(instanceOf(Projection.class)));
        Projection movedOutput = (Projection) outerOutput.getSubExpression();
        assertThat(movedOutput.getSubExpression(),
                    is(instanceOf(Selection.class)));
        Selection innerOutput = (Selection) movedOutput.getSubExpression();
        assertThat(innerOutput.getSubExpression(), is(equalTo(subExpression)));

        assertThat(outerOutput.getPredicate(), is(equalTo(outerPredicate)));
        assertThat(movedOutput.getSelectedFields(),
                    is(equalTo(movedProjections)));
        assertThat(innerOutput.getPredicate(), is(equalTo(innerPredicate)));

        assertThat(new Boolean(subject.wasTrivial()), is(false));
    }

    @Test
    public void loadTableStopsPushDown() {
        LoadTable subExpression = new LoadTable("tableName");
        IRowSelector predicate = createMock(IRowSelector.class);
        Selection input = new Selection(predicate, subExpression);

        expect(schemeMock.acceptsSelection(input)).andReturn(true);

        replayAll();
        TreeReorderer subject = new TreeReorderer(schemeMock);
        IRelAlgExpr output = subject.optimize(input);
        verifyAll();

        assertThat(output, is(instanceOf(Selection.class)));
        Selection outputSelection = (Selection) output;
        assertThat(outputSelection.getPredicate(),
                    is(equalTo(predicate)));
        assertThat(outputSelection.getSubExpression(),
                    is(instanceOf(LoadTable.class)));
        LoadTable outputLoadTable =
            (LoadTable) outputSelection.getSubExpression();
        assertThat(outputLoadTable.getName(), is(equalTo("tableName")));
    }

    @Test
    public void renameTableStopsPushDown() {
        ITable tableMock = createMock(ITable.class);
        RenameTable subExpression = new RenameTable(tableMock, "tableName");
        IRowSelector predicate = createMock(IRowSelector.class);
        Selection input = new Selection(predicate, subExpression);

        expect(schemeMock.acceptsSelection(input)).andReturn(true);

        replayAll();
        TreeReorderer subject = new TreeReorderer(schemeMock);
        IRelAlgExpr output = subject.optimize(input);
        verifyAll();

        assertThat(output, is(instanceOf(Selection.class)));
        Selection outputSelection = (Selection) output;
        assertThat(outputSelection.getPredicate(),
                    is(equalTo(predicate)));
        assertThat(outputSelection.getSubExpression(),
                    is(instanceOf(RenameTable.class)));
        RenameTable outputRenameTable =
            (RenameTable) outputSelection.getSubExpression();
        assertThat(outputRenameTable.getSource(), is(equalTo(tableMock)));
        assertThat(outputRenameTable.getName(), is(equalTo("tableName")));
    }

    @Test
    public void productTraversed() {
        IRowSelector movedPredicate = createMock(IRowSelector.class);
        IRowSelector blockedPredicate = createMock(IRowSelector.class);

        IRelAlgExpr left = createMock(IRelAlgExpr.class);
        IRelAlgExpr rightResult = createMock(IRelAlgExpr.class);
        StubResult<IRelAlgExpr> right = new StubResult<IRelAlgExpr>(rightResult);
        Selection stopSelection = new Selection(blockedPredicate, left);
        Selection movedSelection = new Selection(movedPredicate, stopSelection);
        Product input = new Product(movedSelection, right);

        expect(schemeMock.acceptsSelection(movedSelection)).andReturn(true);
        expect(schemeMock.moveAlongSelection(stopSelection)).andReturn(
                IReorderingScheme.ActionAlongUnaryOperation.NO_OPERATION);

        replayAll();
        TreeReorderer subject = new TreeReorderer(schemeMock);
        IRelAlgExpr output = subject.optimize(input);
        verifyAll();

        assertThat(output, is(instanceOf(Product.class)));
        Product outputProduct = (Product) output;

        assertThat(outputProduct.getRight(), is(equalTo(rightResult)));
        assertThat(outputProduct.getLeft(), is(instanceOf(Selection.class)));
        Selection outputSelection = (Selection) outputProduct.getLeft();
        assertThat(outputSelection.getPredicate(), is(equalTo(movedPredicate)));
        assertThat(outputSelection.getSubExpression(),
                    is(instanceOf(Selection.class)));
        Selection secondOutputSelection =
            (Selection) outputSelection.getSubExpression();
        assertThat(secondOutputSelection.getPredicate(),
                    is(equalTo(blockedPredicate)));
        assertThat(secondOutputSelection.getSubExpression(), is(equalTo(left)));
        assertThat(new Boolean(subject.wasTrivial()), is(true));
    }

    @Test
    public void alongProductTowardsLeft() {
        IRelAlgExpr rightResult = createMock(IRelAlgExpr.class);
        IRelAlgExpr ignoredRight = createMock(IRelAlgExpr.class);
        IRelAlgExpr leftExpression = createMock(IRelAlgExpr.class);
        Collection<IColumnProjection> projections =
            createMock(ProjectionCollection.class);

        Projection blockingProjection = new Projection(leftExpression,
                                                        projections);
        Product product = new Product(blockingProjection, ignoredRight);
        IRowSelector movedPredicate = createMock(IRowSelector.class);
        Selection movedSelection = new Selection(movedPredicate, product);

        expect(schemeMock.acceptsSelection(movedSelection)).andReturn(true);
        expect(schemeMock.moveAlongProduct(product)).andReturn(
                IReorderingScheme.ActionAlongBinaryOperation.MOVE_LEFT);
        expect(schemeMock.moveAlongProjection(blockingProjection)).andReturn(
                IReorderingScheme.ActionAlongUnaryOperation.NO_OPERATION);

        replayAll();
        TreeReorderer subject = new TreeReorderer(schemeMock);
        IRelAlgExpr output = subject.optimize(movedSelection);
        verifyAll();

        assertThat(output, is(instanceOf(Product.class)));
        Product outputProduct = (Product) output;
        assertThat(outputProduct.getRight(), is(equalTo(ignoredRight)));
        assertThat(outputProduct.getLeft(), is(instanceOf(Selection.class)));
        Selection outputSelection = (Selection) outputProduct.getLeft();
        assertThat(outputSelection.getPredicate(), is(equalTo(movedPredicate)));
        assertThat(outputSelection.getSubExpression(),
                        is(instanceOf(Projection.class)));
        Projection outputProjection = (Projection)
            outputSelection.getSubExpression();
        assertThat(outputProjection.getSelectedFields(),
                    is(equalTo(projections)));
        assertThat(outputProjection.getSubExpression(),
                    is(equalTo(leftExpression)));
    }

    @Test
    public void alongProductTowardsRight() {
        IRelAlgExpr leftResult = createMock(IRelAlgExpr.class);
        IRelAlgExpr ignoredLeft = createMock(IRelAlgExpr.class);
        IRelAlgExpr rightExpression = createMock(IRelAlgExpr.class);
        Collection<IColumnProjection> projections =
            createMock(ProjectionCollection.class);

        Projection blockingProjection = new Projection(rightExpression,
                                                        projections);
        Product product = new Product(ignoredLeft, blockingProjection);
        IRowSelector movedPredicate = createMock(IRowSelector.class);
        Selection movedSelection = new Selection(movedPredicate, product);

        expect(schemeMock.acceptsSelection(movedSelection)).andReturn(true);
        expect(schemeMock.moveAlongProduct(product)).andReturn(
                IReorderingScheme.ActionAlongBinaryOperation.MOVE_RIGHT);
        expect(schemeMock.moveAlongProjection(blockingProjection)).andReturn(
                IReorderingScheme.ActionAlongUnaryOperation.NO_OPERATION);

        replayAll();
        TreeReorderer subject = new TreeReorderer(schemeMock);
        IRelAlgExpr output = subject.optimize(movedSelection);
        verifyAll();

        assertThat(output, is(instanceOf(Product.class)));
        Product outputProduct = (Product) output;
        assertThat(outputProduct.getLeft(), is(equalTo(ignoredLeft)));
        assertThat(outputProduct.getRight(), is(instanceOf(Selection.class)));
        Selection outputSelection = (Selection) outputProduct.getRight();
        assertThat(outputSelection.getPredicate(), is(equalTo(movedPredicate)));
        assertThat(outputSelection.getSubExpression(),
                        is(instanceOf(Projection.class)));
        Projection outputProjection = (Projection)
            outputSelection.getSubExpression();
        assertThat(outputProjection.getSelectedFields(),
                    is(equalTo(projections)));
        assertThat(outputProjection.getSubExpression(),
                    is(equalTo(rightExpression)));
    }

    @Test
    public void ProductNoOperation() {
        IRowSelector predicate = createMock(IRowSelector.class);
        IRelAlgExpr left = createMock(IRelAlgExpr.class);
        IRelAlgExpr right = createMock(IRelAlgExpr.class);

        Product subExpression = new Product(left, right);
        Selection input = new Selection(predicate, subExpression);

        expect(schemeMock.acceptsSelection(input)).andReturn(true);
        expect(schemeMock.moveAlongProduct(subExpression)).andReturn(
                IReorderingScheme.ActionAlongBinaryOperation.NO_OPERATION);

        replayAll();
        TreeReorderer subject = new TreeReorderer(schemeMock);
        IRelAlgExpr output = subject.optimize(input);
        verifyAll();

        assertThat(output, is(instanceOf(Selection.class)));
        Selection outputSelection = (Selection) output;
        assertThat(outputSelection.getPredicate(), is(equalTo(predicate)));

        assertThat(outputSelection.getSubExpression(),
                    is(instanceOf(Product.class)));
        Product subOutput = (Product) outputSelection.getSubExpression();

        assertThat(subOutput.getLeft(), is(equalTo(left)));
        assertThat(subOutput.getRight(), is(equalTo(right)));
        assertThat(new Boolean(subject.wasTrivial()), is(true));
    }

    @Test
    public void restartingTreeReorderer() {
        IRowSelector firstPredicate = createMock(IRowSelector.class);
        IRowSelector secondPredicate = createMock(IRowSelector.class);

        IRelAlgExpr restTree = createMock(IRelAlgExpr.class);
        Selection firstSelection = new Selection(firstPredicate, restTree);
        Selection secondSelection = new Selection(secondPredicate, firstSelection);

        expect(schemeMock.acceptsSelection(secondSelection)).andReturn(true);
        expect(schemeMock.moveAlongSelection(firstSelection)).andReturn(
                IReorderingScheme.ActionAlongUnaryOperation.NO_OPERATION);
        expect(schemeMock.acceptsSelection(secondSelection)).andReturn(true);
        expect(schemeMock.moveAlongSelection(firstSelection)).andReturn(
                IReorderingScheme.ActionAlongUnaryOperation.NO_OPERATION);

        replayAll();
        TreeReorderer subject = new TreeReorderer(schemeMock);
        subject.optimize(secondSelection);
        subject.optimize(secondSelection);
        verifyAll();
    }
}
