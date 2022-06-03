package org.renaissance.jdk.concurrent.threadPoolMatrixMultiplication;

import org.renaissance.jdk.concurrent.matrix.Matrix;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplication;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplicationException;
import org.renaissance.jdk.concurrent.runnablePartialMatrixMultiplication.MultiplyPartiallyByRowRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MultiplyByRowThreadPool implements Runnable {

    private final Matrix A;
    private final Matrix B;
    private final Matrix C;
    private final int partialMultiplicationCount;
    private final int maxThreadCount;

    public MultiplyByRowThreadPool(Matrix A, Matrix B, int partialMultiplicationCount, int maxThreadCount) {
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
        int elementCountPerPartialMultiplication = C.getElementCount() / partialMultiplicationCount;

        for (int partialMultiplicationNo = 0; partialMultiplicationNo < partialMultiplicationCount - 1; partialMultiplicationNo++) {
            int index = partialMultiplicationNo * elementCountPerPartialMultiplication;
            int startRowIndex = index / C.getColumnCount();
            int startColumnIndex = index % C.getColumnCount();

            Runnable runnable = new MultiplyPartiallyByRowRunnable(A, B, C, elementCountPerPartialMultiplication, startRowIndex, startColumnIndex);
            runnables.add(runnable);
        }
        // last partial multiplication
        int index = (partialMultiplicationCount - 1) * elementCountPerPartialMultiplication;
        int startRowIndex = index / C.getColumnCount();
        int startColumnIndex = index % C.getColumnCount();
        elementCountPerPartialMultiplication += C.getElementCount() % partialMultiplicationCount;
        Runnable runnable = new MultiplyPartiallyByRowRunnable(A, B, C, elementCountPerPartialMultiplication, startRowIndex, startColumnIndex);
        runnables.add(runnable);

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
