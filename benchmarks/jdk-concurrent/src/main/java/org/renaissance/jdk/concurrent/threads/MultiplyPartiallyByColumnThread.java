package org.renaissance.jdk.concurrent.threads;

import org.renaissance.jdk.concurrent.matrix.Matrix;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplication;

public class MultiplyPartiallyByColumnThread extends Thread {
    private final Matrix A;
    private final Matrix B;
    private final Matrix C;
    private final int elementsNo;
    private final int startRowIndex;
    private final int startColumnIndex;

    public MultiplyPartiallyByColumnThread(Matrix A, Matrix B, Matrix C, int elementsNo, int startRowIndex, int startColumnIndex) {
        super();

        this.A = A;
        this.B = B;
        this.C = C;
        this.elementsNo = elementsNo;
        this.startRowIndex = startRowIndex;
        this.startColumnIndex = startColumnIndex;
    }

    @Override
    public void run() {
        super.run();

        MatrixMultiplication.multiplyByColumnForConsecutiveElements(A, B, C, elementsNo, startRowIndex, startColumnIndex);
    }
}
