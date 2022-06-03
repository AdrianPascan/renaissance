package org.renaissance.jdk.concurrent.sequentialMatrixMultiplication;

import org.renaissance.jdk.concurrent.matrix.Matrix;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplication;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplicationException;
import org.renaissance.jdk.concurrent.runnablePartialMatrixMultiplication.MultiplyPartiallyByRowRunnable;

import java.util.ArrayList;
import java.util.List;

public class MultiplyByRowSequential implements Runnable {

    private final Matrix A;
    private final Matrix B;
    private final Matrix C;
    private final int partialMultiplicationCount;

    public MultiplyByRowSequential(Matrix A, Matrix B, int partialMultiplicationCount) {
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

        runnables.forEach(Runnable::run);
    }
}
