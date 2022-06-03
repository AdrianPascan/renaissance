package org.renaissance.jdk.concurrent.threadPartialMatrixMultiplication;

import org.renaissance.jdk.concurrent.matrix.Matrix;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplication;

public class MultiplyPartiallyByRowKthThread extends Thread {

    private final Matrix A;
    private final Matrix B;
    private final Matrix C;
    private final int k;
    private final int partialMultiplicationNo;

    public MultiplyPartiallyByRowKthThread(Matrix A, Matrix B, Matrix C, int k, int partialMultiplicationNo) {
        super();

        this.A = A;
        this.B = B;
        this.C = C;
        this.k = k;
        this.partialMultiplicationNo = partialMultiplicationNo;
    }

    @Override
    public void run() {
        super.run();

        MatrixMultiplication.multiplyByRowForEveryKthElement(A, B, C, k, partialMultiplicationNo);
    }
}
