package org.renaissance.jdk.concurrent.threadPoolMatrix;

import org.renaissance.jdk.concurrent.matrix.Matrix;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplication;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplicationException;
import org.renaissance.jdk.concurrent.runnables.MultiplyPartiallyByRowKthRunnable;

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
    private final int tasksNo;
    private final int maxThreadsNo;

    public MultiplyByRowKthThreadPool(Matrix A, Matrix B, int tasksNo, int maxThreadsNo) {
        C = MatrixMultiplication.emptyMatrixOfMultiply(A, B);
        if (tasksNo > C.getRowsNo() * C.getColumnsNo()) {
            throw new MatrixMultiplicationException("MatrixMultiplicationException: MultiplyByColumn");
        }

        this.A = A;
        this.B = B;
        this.tasksNo = tasksNo;
        this.maxThreadsNo = maxThreadsNo;
    }

    @Override
    public void run() {
        List<Runnable> runnables = new ArrayList<>();
        for (int orderNo = 0; orderNo < tasksNo; orderNo++) {
            Runnable runnable = new MultiplyPartiallyByRowKthRunnable(A, B, C, tasksNo, orderNo);
            runnables.add(runnable);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(maxThreadsNo);
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
