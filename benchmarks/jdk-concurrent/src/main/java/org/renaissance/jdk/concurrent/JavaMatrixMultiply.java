package org.renaissance.jdk.concurrent;

import org.renaissance.jdk.concurrent.matrix.Matrix;
import org.renaissance.jdk.concurrent.threadMatrixMultiplication.MultiplyByColumnThread;
import org.renaissance.jdk.concurrent.threadMatrixMultiplication.MultiplyByRowKthThread;
import org.renaissance.jdk.concurrent.threadMatrixMultiplication.MultiplyByRowThread;
import org.renaissance.jdk.concurrent.threadPoolMatrixMultiplication.MultiplyByColumnThreadPool;
import org.renaissance.jdk.concurrent.threadPoolMatrixMultiplication.MultiplyByRowKthThreadPool;
import org.renaissance.jdk.concurrent.threadPoolMatrixMultiplication.MultiplyByRowThreadPool;

import java.util.Arrays;

public final class JavaMatrixMultiply {

    private final Matrix A = new Matrix(3, 2, Arrays.asList(1,2,3,4,5,6));
    private final Matrix B = new Matrix(2, 4, Arrays.asList(1,2,3,4,5,6,7,8));
//    Result matrix:
    private final Matrix C = new Matrix(3, 4, Arrays.asList(11,14,17,20,23,30,37,44,35,46,57,68));

    public Matrix run(int tasksNo) throws InterruptedException {
        Runnable runnable = new MultiplyByRowThread(A, B, tasksNo);
        runnable.run();

        runnable = new MultiplyByColumnThread(A, B, tasksNo);
        runnable.run();

        runnable = new MultiplyByRowKthThread(A, B, tasksNo);
        runnable.run();

        // thread pool(2)
        runnable = new MultiplyByRowThreadPool(A, B, tasksNo, 2);
        runnable.run();

        runnable = new MultiplyByColumnThreadPool(A, B, tasksNo, 2);
        runnable.run();

        runnable = new MultiplyByRowKthThreadPool(A, B, tasksNo, 2);
        runnable.run();

        // thread pool(5)
        runnable = new MultiplyByRowThreadPool(A, B, tasksNo, 5);
        runnable.run();

        runnable = new MultiplyByColumnThreadPool(A, B, tasksNo, 5);
        runnable.run();

        runnable = new MultiplyByRowKthThreadPool(A, B, tasksNo, 5);
        runnable.run();

        // thread pool(10)
        runnable = new MultiplyByRowThreadPool(A, B, tasksNo, 10);
        runnable.run();

        runnable = new MultiplyByColumnThreadPool(A, B, tasksNo, 10);
        runnable.run();

        runnable = new MultiplyByRowKthThreadPool(A, B, tasksNo, 10);
        runnable.run();

        return C;
    }
}