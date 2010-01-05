package edu.rivfader.test.relalg.operation.optimization;

import edu.rivfader.relalg.representation.IRelAlgExpr;
import edu.rivfader.relalg.operations.optimization.IOptimization;
import edu.rivfader.relalg.operations.optimization.IteratedOptimization;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.is;

import org.powermock.modules.junit4.PowerMockRunner;
import static org.easymock.EasyMock.expect;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.verifyAll;
import static org.powermock.api.easymock.PowerMock.replayAll;


@RunWith(PowerMockRunner.class)
public class IteratedOptimizationTest {
    @Test
    public void testIteratedUntilTriviality() {
        IOptimization iteratedOptimization = createMock(IOptimization.class);

        IRelAlgExpr inputTree = createMock(IRelAlgExpr.class);
        IRelAlgExpr firstResult = createMock(IRelAlgExpr.class);
        IRelAlgExpr secondResult = createMock(IRelAlgExpr.class);

        expect(iteratedOptimization.optimize(inputTree)).andReturn(firstResult);
        expect(iteratedOptimization.wasTrivial()).andReturn(false);
        expect(iteratedOptimization.optimize(firstResult))
            .andReturn(secondResult);
        expect(iteratedOptimization.wasTrivial()).andReturn(true);

        replayAll();
        IteratedOptimization subject = new IteratedOptimization(
                                                iteratedOptimization);
        IRelAlgExpr result = subject.optimize(inputTree);
        verifyAll();
        assertThat(result, is(secondResult));
        assertThat(new Boolean(subject.wasTrivial()), is(false));
    }

    @Test
    public void testTriviality() {
        IOptimization iteratedOptimization = createMock(IOptimization.class);
        IRelAlgExpr input = createMock(IRelAlgExpr.class);

        expect(iteratedOptimization.optimize(input)).andReturn(input);
        expect(iteratedOptimization.wasTrivial()).andReturn(true);

        replayAll();
        IteratedOptimization subject = new IteratedOptimization(
                                            iteratedOptimization);
        IRelAlgExpr result = subject.optimize(input);
        verifyAll();
        assertThat(result, is(input));
        assertThat(subject.wasTrivial(), is(true));
    }
}
