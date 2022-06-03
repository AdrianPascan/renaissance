package org.renaissance.jdk.concurrent.threadMatrixMultiplication;

import org.renaissance.jdk.concurrent.matrix.Matrix;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplication;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplicationException;
import org.renaissance.jdk.concurrent.threadPartialMatrixMultiplication.MultiplyPartiallyByRowThread;

import java.util.ArrayList;
import java.util.List;

public class MultiplyByRowThread implements Runnable {

    private final Matrix A;
    private final Matrix B;
    private final Matrix C;
    private final int partialMultiplicationCount;

    public MultiplyByRowThread(Matrix A, Matrix B, int partialMultiplicationCount) {
        super();

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
        List<Thread> threads = new ArrayList<>();
        int elementCountPerTask = C.getElementCount() / partialMultiplicationCount;

        for (int taskIndex = 0; taskIndex < partialMultiplicationCount - 1; taskIndex++) {
            int index = taskIndex * elementCountPerTask;
            int startRowIndex = index / C.getColumnCount();
            int startColumnIndex = index % C.getColumnCount();

            Thread thread = new MultiplyPartiallyByRowThread(A, B, C, elementCountPerTask, startRowIndex, startColumnIndex);
            threads.add(thread);
        }
        // last partial multiplication
        int index = (partialMultiplicationCount - 1) * elementCountPerTask;
        int startRowIndex = index / C.getColumnCount();
        int startColumnIndex = index % C.getColumnCount();
        elementCountPerTask += C.getElementCount() % partialMultiplicationCount;
        Thread thread = new MultiplyPartiallyByRowThread(A, B, C, elementCountPerTask, startRowIndex, startColumnIndex);
        threads.add(thread);

        threads.forEach(Thread::start);
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        });
    }
}
