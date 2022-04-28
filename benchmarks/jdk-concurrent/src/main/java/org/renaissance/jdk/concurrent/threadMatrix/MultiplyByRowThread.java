package org.renaissance.jdk.concurrent.threadMatrix;

import org.renaissance.jdk.concurrent.matrix.Matrix;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplication;
import org.renaissance.jdk.concurrent.matrix.MatrixMultiplicationException;
import org.renaissance.jdk.concurrent.threads.MultiplyPartiallyByRowThread;

import java.util.ArrayList;
import java.util.List;

public class MultiplyByRowThread extends Thread {
    private final Matrix A;
    private final Matrix B;
    private final Matrix C;
    private final int tasksNo;

    public MultiplyByRowThread(Matrix A, Matrix B, int tasksNo) {
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
            int startRowIndex = index / C.getColumnsNo();
            int startColumnIndex = index % C.getColumnsNo();

            Thread thread = new MultiplyPartiallyByRowThread(A, B, C, elementsNoPerTask, startRowIndex, startColumnIndex);
            threads.add(thread);
        }
        // last thread
        int index = (tasksNo - 1) * elementsNoPerTask;
        int startRowIndex = index / C.getColumnsNo();
        int startColumnIndex = index % C.getColumnsNo();
        elementsNoPerTask += C.getElementsNo() % tasksNo;
        Thread thread = new MultiplyPartiallyByRowThread(A, B, C, elementsNoPerTask, startRowIndex, startColumnIndex);
        threads.add(thread);

        threads.forEach(Thread::start);
        threads.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        });

        System.out.println(C.toString());
    }
}
