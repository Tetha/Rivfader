package edu.rivfader.test.relalg.operations.profiling_evaluation;

import edu.rivfader.relalg.operations.profiling_evaluation.CountingIterator;
import edu.rivfader.relalg.operations.profiling_evaluation.ICountingIterator;

import java.util.List;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
public class CountingIteratorTest {
    @Test public void countingWorks() {
        List<String> input;
        List<String> output;
        ICountingIterator<String> subject;
        input = new LinkedList<String>();
        input.add("one");
        input.add("two");
        input.add("three");

        output = new LinkedList<String>();

        subject = new CountingIterator<String>(input.iterator());
        while(subject.hasNext()) {
            output.add(subject.next());
        }

        assertThat(output, is(equalTo(input)));
        assertThat(subject.getNumberOfElements(), is(equalTo(input.size())));
    }
}
