package org.renaissance.jdk.concurrent.matrix;

public class MatrixMultiplication {
    public static Matrix emptyMatrixOfMultiply(Matrix A, Matrix B) {
        if (A.getColumnsNo() != B.getRowsNo()) {
            throw new MatrixMultiplicationException("MatrixMultiplicationException:" +
                    " cannot perform matrix multiplication on matrices of sizes" +
                    " (" + A.getRowsNo() + ", " + A.getColumnsNo() + ") and" +
                    " (" + B.getRowsNo() + ", " + B.getColumnsNo() + ")" +
                    " as " + A.getColumnsNo() + "!=" + B.getRowsNo());
        }

        return new Matrix(A.getRowsNo(), B.getColumnsNo());
    }

    private static int multiplyForElement(Matrix A, Matrix B, int rowIndex, int columnIndex) {
        int result = 0;
        for (int commonIndex = 0; commonIndex < A.getColumnsNo(); commonIndex++) {
            result += A.getElements().get(rowIndex).get(commonIndex) * B.getElements().get(commonIndex).get(columnIndex);
        }
        return result;
    }

    public static void multiplyByRowForConsecutiveElements(Matrix A, Matrix B, Matrix C, int elementsNo, int startRowIndex, int startColumnIndex) {
        int rowIndex = startRowIndex;
        int columnIndex = startColumnIndex;

        for (int elementNo = 0; elementNo < elementsNo; elementNo++) {
            if (columnIndex >= C.getColumnsNo()) {
                columnIndex = 0;
                rowIndex++;
                if (rowIndex >= C.getRowsNo()){
                    throw new MatrixMultiplicationException("MatrixMultiplicationException: multiplyByRowForConsecutiveElements");
                }
            }

            int element = multiplyForElement(A, B, rowIndex, columnIndex);
            C.getElements().get(rowIndex).set(columnIndex, element);

            columnIndex++;
        }
    }

    public static void multiplyByColumnForConsecutiveElements(Matrix A, Matrix B, Matrix C, int elementsNo, int startRowIndex, int startColumnIndex) {
        int rowIndex = startRowIndex;
        int columnIndex = startColumnIndex;

        for (int elementNo = 0; elementNo < elementsNo; elementNo++) {
            if (rowIndex >= C.getRowsNo()) {
                rowIndex = 0;
                columnIndex++;
                if (columnIndex >= C.getColumnsNo()){
                    throw new MatrixMultiplicationException("MatrixMultiplicationException: multiplyByColumnForConsecutiveElements");
                }
            }

            int element = multiplyForElement(A, B, rowIndex, columnIndex);
            C.getElements().get(rowIndex).set(columnIndex, element);

            rowIndex++;
        }
    }

    public static void multiplyByRowForEveryKthElement(Matrix A, Matrix B, Matrix C, int k, int orderNo) {
        int index = orderNo;
        if (k <= orderNo || index >= C.getElementsNo()) {
            throw new MatrixMultiplicationException("MatrixMultiplicationException: multiplyByRowForEveryKthElement");
        }

        while(index < C.getElementsNo()) {
            int rowIndex = index / C.getColumnsNo();
            int columnIndex = index % C.getColumnsNo();

            int element = multiplyForElement(A, B, rowIndex, columnIndex);
            C.getElements().get(rowIndex).set(columnIndex, element);

            index += k;
        }
    }
}
