package edu.rivfader.test.relalg.operation.optimization;

import edu.rivfader.relalg.representation.IRelAlgExpr;
import edu.rivfader.relalg.operations.optimization.IOptimization;
import edu.rivfader.relalg.operations.optimization.CombinedOptimization;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.Before;

import org.junit.runner.RunWith;
import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.is;

import static org.easymock.EasyMock.expect;
import org.powermock.api.easymock.annotation.Mock;
import static org.powermock.api.easymock.PowerMock.createMock;
import static org.powermock.api.easymock.PowerMock.replayAll;
import static org.powermock.api.easymock.PowerMock.verifyAll;

import org.powermock.modules.junit4.PowerMockRunner;

import java.util.LinkedList;
import java.util.Collection;

@RunWith(PowerMockRunner.class)
public class CombinedOptimizationTest {
    @Mock private IOptimization o1;
    @Mock private IOptimization o2;
    @Mock private IRelAlgExpr input;
    private Collection<IOptimization> optimizations;

    @Before
    public void initializeOptimizations() {
        optimizations = new LinkedList<IOptimization>();
        optimizations.add(o1);
        optimizations.add(o2);
    }

    @Test
    public void testAllCombinationsAreCalledUntilAllTrivial() {
        IRelAlgExpr firstOutput = createMock(IRelAlgExpr.class);
        IRelAlgExpr secondOutput = createMock(IRelAlgExpr.class);

        expect(o1.optimize(input)).andReturn(firstOutput);
        expect(o2.optimize(firstOutput)).andReturn(secondOutput);

        replayAll();
        CombinedOptimization subject = new CombinedOptimization(optimizations);
        IRelAlgExpr output = subject.optimize(input);
        verifyAll();
        assertThat(output, is(secondOutput));
    }

    @Test
    public void someNonTrivialStepMakesNonTrivial() {
        expect(o1.optimize(input)).andReturn(input);
        expect(o2.optimize(input)).andReturn(input);
        expect(o1.wasTrivial()).andReturn(false).times(0,1);
        expect(o2.wasTrivial()).andReturn(true).times(0,1);
        replayAll();
        CombinedOptimization subject = new CombinedOptimization(optimizations);
        subject.optimize(input);
        assertThat(subject.wasTrivial(), is(false));
        verifyAll();
    }

    @Test
    public void wasTrivialIfAllWereTrivial() {
        expect(o1.optimize(input)).andReturn(input);
        expect(o2.optimize(input)).andReturn(input);
        expect(o1.wasTrivial()).andReturn(true);
        expect(o2.wasTrivial()).andReturn(true);
        replayAll();
        CombinedOptimization subject = new CombinedOptimization(optimizations);
        subject.optimize(input);
        assertThat(subject.wasTrivial(), is(true));
        verifyAll();
    }
}
