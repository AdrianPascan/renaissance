package org.renaissance.jdk.concurrent;

import org.renaissance.jdk.concurrent.matrix.Matrix;
import org.renaissance.jdk.concurrent.sequentialMatrixMultiplication.MultiplyByColumnSequential;
import org.renaissance.jdk.concurrent.sequentialMatrixMultiplication.MultiplyByRowKthSequential;
import org.renaissance.jdk.concurrent.sequentialMatrixMultiplication.MultiplyByRowSequential;
import org.renaissance.jdk.concurrent.threadMatrixMultiplication.MultiplyByColumnThread;
import org.renaissance.jdk.concurrent.threadMatrixMultiplication.MultiplyByRowKthThread;
import org.renaissance.jdk.concurrent.threadMatrixMultiplication.MultiplyByRowThread;
import org.renaissance.jdk.concurrent.threadPoolMatrixMultiplication.MultiplyByColumnThreadPool;
import org.renaissance.jdk.concurrent.threadPoolMatrixMultiplication.MultiplyByRowKthThreadPool;
import org.renaissance.jdk.concurrent.threadPoolMatrixMultiplication.MultiplyByRowThreadPool;

import java.util.Arrays;
import java.util.Optional;

public final class JavaMatrixMultiply {

    private Matrix A;
    private Matrix B;
    private Runnable multiplication;

    public JavaMatrixMultiply(int rowCount, int commonCount, int columnCount,
                              PartialMultiplication partialMultiplication, int partialMultiplicationCount,
                              Concurrency concurrency, int maxThreadCount) {
        generateMatrices(rowCount, commonCount, columnCount);
        initializeMultiplication(partialMultiplication, partialMultiplicationCount, concurrency, maxThreadCount);

        System.out.println("ROW_COUNT= " + rowCount);
        System.out.println("COMMON_COUNT= " + commonCount);
        System.out.println("COLUMN_COUNT= " + columnCount);
        System.out.println("PARTIAL_MULTIPLICATION= " + partialMultiplication);
        System.out.println("PARTIAL_MULTIPLICATION_COUNT= " + partialMultiplicationCount);
        System.out.println("CONCURRENCY= " + concurrency);
        System.out.println("CONCURRENCY_COUNT= " + maxThreadCount);
    }

    private void generateMatrices(int rowCount, int commonCount, int columnCount) {
        A = new Matrix(rowCount, commonCount, 0);
        B = new Matrix(commonCount, columnCount, rowCount * commonCount);
    }

    private void initializeMultiplication(PartialMultiplication partialMultiplication, int partialMultiplicationCount,
                                          Concurrency concurrency, int maxThreadCount) {
        switch (concurrency) {
            case SEQUENTIAL:
                switch (partialMultiplication) {
                    case ROW:
                        multiplication = new MultiplyByRowSequential(A, B, partialMultiplicationCount);
                        break;
                    case COLUMN:
                        multiplication = new MultiplyByColumnSequential(A, B, partialMultiplicationCount);
                        break;
                    case ROW_KTH:
                        multiplication = new MultiplyByRowKthSequential(A, B, partialMultiplicationCount);
                        break;
                }
                break;
            case THREAD:
                switch (partialMultiplication) {
                    case ROW:
                        multiplication = new MultiplyByRowThread(A, B, partialMultiplicationCount);
                        break;
                    case COLUMN:
                        multiplication = new MultiplyByColumnThread(A, B, partialMultiplicationCount);
                        break;
                    case ROW_KTH:
                        multiplication = new MultiplyByRowKthThread(A, B, partialMultiplicationCount);
                        break;
                }
                break;
            case THREAD_POOL:
                switch (partialMultiplication) {
                    case ROW:
                        multiplication = new MultiplyByRowThreadPool(A, B, partialMultiplicationCount, maxThreadCount);
                        break;
                    case COLUMN:
                        multiplication = new MultiplyByColumnThreadPool(A, B, partialMultiplicationCount, maxThreadCount);
                        break;
                    case ROW_KTH:
                        multiplication = new MultiplyByRowKthThreadPool(A, B, partialMultiplicationCount, maxThreadCount);
                        break;
                }
                break;
        }
    }

    public void run() {
        multiplication.run();
    }

    public enum PartialMultiplication {
        ROW("row"), COLUMN("col"), ROW_KTH("row_kth");

        private final String parameter;

        PartialMultiplication(String parameter) {
            this.parameter = parameter;
        }

        public String getParameter() {
            return parameter;
        }

        public static PartialMultiplication getDefaultPartialMultiplication() {
            return ROW;
        }

        public static Optional<PartialMultiplication> getPartialMultiplicationByParameter(String parameter) {
            return Arrays.stream(PartialMultiplication.values()).filter(pm -> pm.parameter.equals(parameter)).findAny();
        }
    }

    public enum Concurrency {
        SEQUENTIAL("seq"), THREAD("thread"), THREAD_POOL("pool");

        private final String parameter;

        Concurrency(String parameter) {
            this.parameter = parameter;
        }

        public String getParameter() {
            return parameter;
        }

        public static Concurrency getDefaultConcurrency() {
            return SEQUENTIAL;
        }

        public static Optional<Concurrency> getConcurrencyByParameter(String parameter) {
            return Arrays.stream(Concurrency.values()).filter(c -> c.parameter.equals(parameter)).findAny();
        }
    }
}