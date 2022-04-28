package org.renaissance.jdk.concurrent;

import org.renaissance.Benchmark;
import org.renaissance.BenchmarkContext;
import org.renaissance.BenchmarkResult;
import org.renaissance.BenchmarkResult.Validators;

import static org.renaissance.Benchmark.*;

@Name("matrix-multiply")
@Group("concurrency")
@Group("jdk-concurrent")
@Summary("Runs matrix multiplication using different concurrency strategies (threads, runnables, thread pools) and different partial multiplication techniques (consecutive elements by row/column, k-th elements).")
@Repetitions(20)
@Parameter(name = "max_tasks_no", defaultValue = "12")
public final class MatrixMultiply implements Benchmark {

    private int maxTasksNo;

    private JavaMatrixMultiply benchmark;

    @Override
    public void setUpBeforeAll(BenchmarkContext c) {
        maxTasksNo = c.parameter("max_tasks_no").toInteger();

        benchmark = new JavaMatrixMultiply();
    }

    @Override
    public BenchmarkResult run(BenchmarkContext c) {
        for (int taskNo = 1; taskNo <= maxTasksNo; taskNo++) {
            try {
                benchmark.run(taskNo);
            } catch (InterruptedException ie) {
                System.out.println("INTERRUPTED_EXCEPTION: taskNo= " + String.valueOf(taskNo));
            }
        }

        return Validators.simple("matrix-multiply", 0, 0);
    }
}
