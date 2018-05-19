package com.tngtech.archunit.junit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.reporting.ReportEntry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.engine.TestExecutionResult.Status.FAILED;
import static org.junit.platform.engine.TestExecutionResult.Status.SUCCESSFUL;

class EngineExecutionTestListener implements EngineExecutionListener {
    private final List<TestDescriptor> startedTests = new ArrayList<>();
    private final List<FinishedTest> finishedTests = new ArrayList<>();

    @Override
    public void dynamicTestRegistered(TestDescriptor testDescriptor) {
    }

    @Override
    public void executionSkipped(TestDescriptor testDescriptor, String reason) {
    }

    @Override
    public void executionStarted(TestDescriptor testDescriptor) {
        startedTests.add(testDescriptor);
    }

    @Override
    public void executionFinished(TestDescriptor testDescriptor, TestExecutionResult testExecutionResult) {
        finishedTests.add(new FinishedTest(testDescriptor, testExecutionResult));
    }

    @Override
    public void reportingEntryPublished(TestDescriptor testDescriptor, ReportEntry entry) {
    }

    void verifySuccessful(UniqueId testId) {
        verifyStarted(testId);
        FinishedTest test = finishedTests.stream().filter(result -> result.hasId(testId)).collect(onlyElement());
        assertThat(test.result.getStatus()).as("Test status of " + test).isEqualTo(SUCCESSFUL);
    }

    private void verifyStarted(UniqueId testId) {
        boolean testStarted = startedTests.stream().anyMatch(descriptor -> descriptor.getUniqueId().equals(testId));
        assertThat(testStarted).as("Test with id " + testId + " was started").isTrue();
    }

    void verifyViolation(UniqueId testId, String messagePart) {
        verifyStarted(testId);
        FinishedTest test = finishedTests.stream().filter(result -> result.hasId(testId)).collect(onlyElement());
        assertThat(test.result.getStatus())
                .as("Test status of " + test)
                .isEqualTo(FAILED);
        assertThat(test.result.getThrowable().isPresent())
                .as("Test has thrown Throwable: " + test)
                .isTrue();
        assertThat(test.result.getThrowable().get())
                .as("Test Throwable of " + test)
                .isInstanceOf(AssertionError.class);
        assertThat(test.result.getThrowable().get().getMessage())
                .as("AssertionError message of " + test)
                .containsSequence(messagePart);
    }

    private static class FinishedTest {
        final TestDescriptor testDescriptor;
        final TestExecutionResult result;

        FinishedTest(TestDescriptor testDescriptor, TestExecutionResult result) {
            this.testDescriptor = testDescriptor;
            this.result = result;
        }

        boolean hasId(UniqueId testId) {
            return testDescriptor.getUniqueId().equals(testId);
        }

        @Override
        public String toString() {
            return "FinishedTest{" +
                    "testDescriptor=" + testDescriptor +
                    ", result=" + result +
                    '}';
        }
    }

    private static <T> Collector<T, Collection<T>, T> onlyElement() {
        Supplier<Collection<T>> supplier = ArrayList::new;
        BiConsumer<Collection<T>, T> accumulator = Collection::add;
        BinaryOperator<Collection<T>> combiner = (left, right) -> {
            left.addAll(right);
            return left;
        };
        Function<Collection<T>, T> finisher = collection -> {
            if (collection.size() != 1) {
                throw new IllegalStateException("Expected collection to have exactly one element, but was " + collection);
            }
            return collection.iterator().next();
        };
        return Collector.of(supplier, accumulator, combiner, finisher);
    }
}
