package org.renaissance.jdk.concurrent.runnablePartialMatrixMultiplication;

import org.renaissance.jdk.concurrent.matrix.Matrix;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplication;

public class MultiplyPartiallyByRowKthRunnable implements Runnable {

    private final Matrix A;
    private final Matrix B;
    private final Matrix C;
    private final int k;
    private final int partialMultiplicationNo;

    public MultiplyPartiallyByRowKthRunnable(Matrix A, Matrix B, Matrix C, int k, int partialMultiplicationNo) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.k = k;
        this.partialMultiplicationNo = partialMultiplicationNo;
    }

    @Override
    public void run() {
        MatrixMultiplication.multiplyByRowForEveryKthElement(A, B, C, k, partialMultiplicationNo);
    }
}
