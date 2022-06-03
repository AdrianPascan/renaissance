package org.renaissance.jdk.concurrent;

import org.renaissance.Benchmark;
import org.renaissance.BenchmarkContext;
import org.renaissance.BenchmarkResult;
import org.renaissance.BenchmarkResult.Validators;

import static org.renaissance.Benchmark.*;

@Name("matrix-multiply")
@Group("concurrency")
@Group("jdk-concurrent")
@Summary("Runs matrix multiplication using different partial multiplication techniques" +
        " (consecutive elements by row/column, k-th elements by row) and different concurrency strategies" +
        " (sequential, threads, thread pools).")
@Repetitions(20)
@Parameter(name = "l", defaultValue = "500", summary = "No. of rows for the 1st matrix")
@Parameter(name = "m", defaultValue = "300", summary = "No. of columns for the 1st matrix and no. of rows for the 2nd matrix")
@Parameter(name = "n", defaultValue = "500", summary = "No. of columns for the 2nd matrix")
@Parameter(name = "pm", defaultValue = "row", summary = "Partial multiplication: row (consecutive by row partial multiplication)," +
        " col (consecutive by column partial multiplication), row_kth (kth elements by row partial multiplication)")
@Parameter(name = "pmc", defaultValue = "1000", summary = "[Partial multiplication] partial multiplication count (no. of partial multiplications)")
@Parameter(name = "cs", defaultValue = "seq", summary = "Concurrency strategy: seq (sequential = no concurrency)," +
        " thread (threads for partial multiplications), pool (thread pool for partial multiplications)")
@Parameter(name = "csc", defaultValue = "100", summary = "[Concurrency strategy] max. thread count (no. of threads) for thread pool")
public final class MatrixMultiply implements Benchmark {

    private JavaMatrixMultiply benchmark;

    @Override
    public void setUpBeforeAll(BenchmarkContext c) {
        // matrices parameters
        int rowCount = c.parameter("l").toInteger();
        int commonCount = c.parameter("m").toInteger();
        int columnCount = c.parameter("n").toInteger();

        // partial multiplication parameters
        String partialMultiplicationParameter = c.parameter("pm").value();
        JavaMatrixMultiply.PartialMultiplication partialMultiplication = JavaMatrixMultiply.PartialMultiplication
                .getPartialMultiplicationByParameter(partialMultiplicationParameter)
                .orElseGet(() -> {
                    System.out.println("Unrecognized partial multiplication technique, using the default...");
                    return JavaMatrixMultiply.PartialMultiplication.getDefaultPartialMultiplication();
                });
        int partialMultiplicationCount = c.parameter("pmc").toInteger();

        // concurrency parameters
        String concurrencyParameter = c.parameter("cs").value();
        JavaMatrixMultiply.Concurrency concurrency = JavaMatrixMultiply.Concurrency
                .getConcurrencyByParameter(concurrencyParameter)
                .orElseGet(() -> {
                    System.out.println("Unrecognized concurrency strategy, using the default...");
                    return JavaMatrixMultiply.Concurrency.getDefaultConcurrency();
                });
        int maxThreadCount = c.parameter("csc").toInteger();

        benchmark = new JavaMatrixMultiply(rowCount, commonCount, columnCount, partialMultiplication, partialMultiplicationCount, concurrency, maxThreadCount);
    }

    @Override
    public BenchmarkResult run(BenchmarkContext c) {
        benchmark.run();
        return Validators.simple("matrix-multiply", 0, 0);
    }
}

