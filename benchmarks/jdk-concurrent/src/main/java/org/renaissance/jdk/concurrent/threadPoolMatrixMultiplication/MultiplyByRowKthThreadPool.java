package org.renaissance.jdk.concurrent.threadPoolMatrixMultiplication;

import org.renaissance.jdk.concurrent.matrix.Matrix;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplication;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplicationException;
import org.renaissance.jdk.concurrent.runnablePartialMatrixMultiplication.MultiplyPartiallyByRowKthRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiplyByRowKthThreadPool implements Runnable {

    private final Matrix A;
    private final Matrix B;
    private final Matrix C;
    private final int partialMultiplicationCount;
    private final int maxThreadCount;

    public MultiplyByRowKthThreadPool(Matrix A, Matrix B, int partialMultiplicationCount, int maxThreadCount) {
        C = MatrixMultiplication.emptyMatrixOfMultiply(A, B);
        if (partialMultiplicationCount > C.getRowCount() * C.getColumnCount()) {
            throw new MatrixMultiplicationException("MatrixMultiplicationException: MultiplyByColumn");
        }

        this.A = A;
        this.B = B;
        this.partialMultiplicationCount = partialMultiplicationCount;
        this.maxThreadCount = maxThreadCount;
    }

    @Override
    public void run() {
        List<Runnable> runnables = new ArrayList<>();
        for (int partialMultiplicationNo = 0; partialMultiplicationNo < partialMultiplicationCount; partialMultiplicationNo++) {
            Runnable runnable = new MultiplyPartiallyByRowKthRunnable(A, B, C, partialMultiplicationCount, partialMultiplicationNo);
            runnables.add(runnable);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(maxThreadCount);
        List<Future<?>> futures = new ArrayList<>();
        runnables.forEach(r -> {
            Future<?> future = executorService.submit(r);
            futures.add(future);
        });
        futures.forEach(f -> {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException exception) {
                exception.printStackTrace();
            }
        });
        executorService.shutdown();
    }
}
