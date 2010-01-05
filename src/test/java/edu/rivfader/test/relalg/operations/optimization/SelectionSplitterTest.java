package edu.rivfader.test.relalg.operation.optimization;

import edu.rivfader.relalg.representation.RenameTable;
import edu.rivfader.relalg.representation.LoadTable;
import edu.rivfader.relalg.representation.IRelAlgExpr;
import edu.rivfader.relalg.operations.StubResult;
import edu.rivfader.relalg.representation.IColumnProjection;
import edu.rivfader.relalg.representation.Projection;
import edu.rivfader.relalg.representation.Product;
import edu.rivfader.relalg.representation.Selection;
import edu.rivfader.rowselector.representation.IRowSelector;
import edu.rivfader.rowselector.representation.BinaryOperation;
import edu.rivfader.rowselector.representation.BooleanValueCombination;

import edu.rivfader.relalg.operations.optimization.SelectionSplitter;

import java.util.LinkedList;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.anyOf;

import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class SelectionSplitterTest {
    @Test public void checkTransformLoadTable() {
        LoadTable input = createMock(LoadTable.class);
        replayAll();
        SelectionSplitter subject = new SelectionSplitter();
        IRelAlgExpr result = subject.transformLoadTable(input);
        verifyAll();
        assertThat(result, is(equalTo((IRelAlgExpr)input)));
    }

    @Test public void checkTransformRenameTable() {
        RenameTable input = createMock(RenameTable.class);
        replayAll();
        SelectionSplitter subject = new SelectionSplitter();
        IRelAlgExpr result = subject.transformRenameTable(input);
        verifyAll();
        assertThat(result, is(equalTo((IRelAlgExpr)input)));
    }

    @Test public void checkTransformProjection() {
        IRelAlgExpr transformedSubExpression;
        StubResult<IRelAlgExpr> subExpression;
        Collection<IColumnProjection> projections;

        projections = new LinkedList<IColumnProjection>();
        transformedSubExpression = createMock(IRelAlgExpr.class);
        subExpression = new StubResult<IRelAlgExpr>(transformedSubExpression);
        Projection input = new Projection(subExpression, projections);
        replayAll();
        SelectionSplitter subject = new SelectionSplitter();
        IRelAlgExpr result = subject.transformProjection(input);
        verifyAll();
        assertThat(result, is(instanceOf(Projection.class)));
        Projection castedResult = (Projection) result;
        assertThat(castedResult.getSubExpression(),
                    is(equalTo(transformedSubExpression)));
        assertThat(castedResult.getSelectedFields(),
                    is(equalTo(projections)));
    }

    @Test public void checkTransformProduct() {
        IRelAlgExpr leftResult;
        IRelAlgExpr rightResult;
        StubResult<IRelAlgExpr> left;
        StubResult<IRelAlgExpr> right;

        leftResult = createMock(IRelAlgExpr.class);
        rightResult = createMock(IRelAlgExpr.class);
        left = new StubResult<IRelAlgExpr>(leftResult);
        right = new StubResult<IRelAlgExpr>(rightResult);
        Product input = new Product(left, right);
        replayAll();
        SelectionSplitter subject = new SelectionSplitter();
        IRelAlgExpr result = subject.transformProduct(input);
        verifyAll();
        assertThat(result, is(instanceOf(Product.class)));
        Product castedResult = (Product) result;
        assertThat(castedResult.getLeft(), is(equalTo(leftResult)));
        assertThat(castedResult.getRight(), is(equalTo(rightResult)));
    }


    @Test public void checkTransformSelectionAndsAreSplit() {
        IRelAlgExpr subResult;
        StubResult<IRelAlgExpr> subExpression;
        IRowSelector[] basePredicates = new IRowSelector[3];
        IRowSelector predicate;

        subResult = createMock(IRelAlgExpr.class);
        subExpression = new StubResult<IRelAlgExpr>(subResult);
        for(int i = 0; i < basePredicates.length; i++) {
            basePredicates[i] = createMock(IRowSelector.class);
        }

        predicate = new BinaryOperation(
                        new BinaryOperation(basePredicates[0],
                                            BooleanValueCombination.AND,
                                            basePredicates[1]),
                        BooleanValueCombination.AND,
                        basePredicates[2]);

        Selection input = new Selection(predicate, subExpression);

        replayAll();
        SelectionSplitter subject = new SelectionSplitter();
        IRelAlgExpr result = subject.transformSelection(input);
        verifyAll();

        Selection[] transformedSelections = new Selection[3];
        assertThat(result, is(instanceOf(Selection.class)));
        transformedSelections[0] = (Selection)result;
        assertThat(transformedSelections[0].getSubExpression(),
                    is(instanceOf(Selection.class)));
        transformedSelections[1] = (Selection)transformedSelections[0]
                                                    .getSubExpression();
        assertThat(transformedSelections[1].getSubExpression(),
                    is(instanceOf(Selection.class)));
        transformedSelections[2] = (Selection)transformedSelections[1]
                                                    .getSubExpression();

        assertThat(transformedSelections[2].getSubExpression(),
                    is(equalTo(subResult)));
        for(int i = 0; i < basePredicates.length; i++) {
            assertThat(basePredicates[i], is(anyOf(
                        equalTo(transformedSelections[0].getPredicate()),
                        equalTo(transformedSelections[1].getPredicate()),
                        equalTo(transformedSelections[2].getPredicate()))));
        }
    }

    @Test public void checkTransformProjectionKeepsOrsTogether() {
        IRelAlgExpr subResult = createMock(IRelAlgExpr.class);

        IRowSelector[] basePredicates = { createMock(IRowSelector.class),
                                          createMock(IRowSelector.class) };

        StubResult<IRelAlgExpr> subExpression =
            new StubResult<IRelAlgExpr>(subResult);
        IRowSelector predicate = new BinaryOperation(basePredicates[0],
                                                   BooleanValueCombination.OR,
                                                   basePredicates[1]);
        Selection input = new Selection(predicate, subExpression);
        replayAll();
        SelectionSplitter subject = new SelectionSplitter();
        IRelAlgExpr result = subject.transformSelection(input);
        verifyAll();

        assertThat(result, is(instanceOf(Selection.class)));
        Selection castedResult = (Selection)result;
        assertThat(castedResult.getSubExpression(), is(equalTo(subResult)));
        assertThat(castedResult.getPredicate(), is(equalTo(predicate)));
    }
}
