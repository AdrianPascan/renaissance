package org.renaissance.jdk.concurrent.sequentialMatrixMultiplication;

import org.renaissance.jdk.concurrent.matrix.Matrix;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplication;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplicationException;
import org.renaissance.jdk.concurrent.runnablePartialMatrixMultiplication.MultiplyPartiallyByRowKthRunnable;

import java.util.ArrayList;
import java.util.List;

public class MultiplyByRowKthSequential implements Runnable {

    private final Matrix A;
    private final Matrix B;
    private final Matrix C;
    private final int partialMultiplicationCount;

    public MultiplyByRowKthSequential(Matrix A, Matrix B, int partialMultiplicationCount) {
        C = MatrixMultiplication.emptyMatrixOfMultiply(A, B);
        if (partialMultiplicationCount > C.getRowCount() * C.getColumnCount()) {
            throw new MatrixMultiplicationException("MatrixMultiplicationException: MultiplyByColumn");
        }

        this.A = A;
        this.B = B;
        this.partialMultiplicationCount = partialMultiplicationCount;
    }

    @Override
    public void run() {
        List<Runnable> runnables = new ArrayList<>();
        for (int partialMultiplicationNo = 0; partialMultiplicationNo < partialMultiplicationCount; partialMultiplicationNo++) {
            Runnable runnable = new MultiplyPartiallyByRowKthRunnable(A, B, C, partialMultiplicationCount, partialMultiplicationNo);
            runnables.add(runnable);
        }
        runnables.forEach(Runnable::run);
    }
}
