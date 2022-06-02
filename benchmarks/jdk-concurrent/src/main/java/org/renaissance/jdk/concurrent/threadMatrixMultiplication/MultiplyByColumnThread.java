package org.renaissance.jdk.concurrent.threadMatrixMultiplication;

import org.renaissance.jdk.concurrent.matrix.Matrix;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplication;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplicationException;
import org.renaissance.jdk.concurrent.threadPartialMatrixMultiplication.MultiplyPartiallyByColumnThread;

import java.util.ArrayList;
import java.util.List;

public class MultiplyByColumnThread implements Runnable {
    private final Matrix A;
    private final Matrix B;
    private final Matrix C;
    private final int tasksNo;

    public MultiplyByColumnThread(Matrix A, Matrix B, int tasksNo) {
        super();

        C = MatrixMultiplication.emptyMatrixOfMultiply(A, B);
        if (tasksNo > C.getRowsNo() * C.getColumnsNo()) {
            throw new MatrixMultiplicationException("MatrixMultiplicationException: MultiplyByColumn");
        }

        this.A = A;
        this.B = B;
        this.tasksNo = tasksNo;
    }

    @Override
    public void run() {
        List<Thread> threads = new ArrayList<>();
        int elementsNoPerTask = C.getElementsNo() / tasksNo;

        for (int taskIndex = 0; taskIndex < tasksNo - 1; taskIndex++) {
            int index = taskIndex * elementsNoPerTask;
            int startRowIndex = index % C.getRowsNo();
            int startColumnIndex = index / C.getRowsNo();

            Thread thread = new MultiplyPartiallyByColumnThread(A, B, C, elementsNoPerTask, startRowIndex, startColumnIndex);
            threads.add(thread);
        }
        // last thread
        int index = (tasksNo - 1) * elementsNoPerTask;
        int startRowIndex = index % C.getRowsNo();
        int startColumnIndex = index / C.getRowsNo();
        elementsNoPerTask += C.getElementsNo() % tasksNo;
        Thread thread = new MultiplyPartiallyByColumnThread(A, B, C, elementsNoPerTask, startRowIndex, startColumnIndex);
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
