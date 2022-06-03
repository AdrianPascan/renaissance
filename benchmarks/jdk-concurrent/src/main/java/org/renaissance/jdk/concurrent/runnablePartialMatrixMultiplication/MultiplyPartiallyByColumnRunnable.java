package org.renaissance.jdk.concurrent.runnablePartialMatrixMultiplication;

import org.renaissance.jdk.concurrent.matrix.Matrix;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplication;

public class MultiplyPartiallyByColumnRunnable implements Runnable {

    private final Matrix A;
    private final Matrix B;
    private final Matrix C;
    private final int elementCount;
    private final int startRowIndex;
    private final int startColumnIndex;

    public MultiplyPartiallyByColumnRunnable(Matrix A, Matrix B, Matrix C, int elementCount, int startRowIndex, int startColumnIndex) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.elementCount = elementCount;
        this.startRowIndex = startRowIndex;
        this.startColumnIndex = startColumnIndex;
    }

    @Override
    public void run() {
        MatrixMultiplication.multiplyByColumnForConsecutiveElements(A, B, C, elementCount, startRowIndex, startColumnIndex);
    }
}
