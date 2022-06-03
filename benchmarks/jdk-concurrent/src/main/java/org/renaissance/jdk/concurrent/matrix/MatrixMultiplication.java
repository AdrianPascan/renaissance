package org.renaissance.jdk.concurrent.matrix;

public class MatrixMultiplication {

    public static Matrix emptyMatrixOfMultiply(Matrix A, Matrix B) {
        if (A.getColumnCount() != B.getRowCount()) {
            throw new MatrixMultiplicationException("MatrixMultiplicationException:" +
                    " cannot perform matrix multiplication on matrices of sizes" +
                    " (" + A.getRowCount() + ", " + A.getColumnCount() + ") and" +
                    " (" + B.getRowCount() + ", " + B.getColumnCount() + ")" +
                    " as " + A.getColumnCount() + "!=" + B.getRowCount());
        }

        return new Matrix(A.getRowCount(), B.getColumnCount());
    }

    private static int multiplyForElement(Matrix A, Matrix B, int rowIndex, int columnIndex) {
        int result = 0;
        for (int commonIndex = 0; commonIndex < A.getColumnCount(); commonIndex++) {
            result += A.getElements().get(rowIndex).get(commonIndex) * B.getElements().get(commonIndex).get(columnIndex);
        }
        return result;
    }

    public static void multiplyByRowForConsecutiveElements(Matrix A, Matrix B, Matrix C, int elementCount, int startRowIndex, int startColumnIndex) {
        int rowIndex = startRowIndex;
        int columnIndex = startColumnIndex;

        for (int elementNo = 0; elementNo < elementCount; elementNo++) {
            if (columnIndex >= C.getColumnCount()) {
                columnIndex = 0;
                rowIndex++;
                if (rowIndex >= C.getRowCount()){
                    throw new MatrixMultiplicationException("MatrixMultiplicationException: multiplyByRowForConsecutiveElements");
                }
            }

            int element = multiplyForElement(A, B, rowIndex, columnIndex);
            C.getElements().get(rowIndex).set(columnIndex, element);

            columnIndex++;
        }
    }

    public static void multiplyByColumnForConsecutiveElements(Matrix A, Matrix B, Matrix C, int elementCount, int startRowIndex, int startColumnIndex) {
        int rowIndex = startRowIndex;
        int columnIndex = startColumnIndex;

        for (int elementNo = 0; elementNo < elementCount; elementNo++) {
            if (rowIndex >= C.getRowCount()) {
                rowIndex = 0;
                columnIndex++;
                if (columnIndex >= C.getColumnCount()){
                    throw new MatrixMultiplicationException("MatrixMultiplicationException: multiplyByColumnForConsecutiveElements");
                }
            }

            int element = multiplyForElement(A, B, rowIndex, columnIndex);
            C.getElements().get(rowIndex).set(columnIndex, element);

            rowIndex++;
        }
    }

    public static void multiplyByRowForEveryKthElement(Matrix A, Matrix B, Matrix C, int k, int partialMultiplicationNo) {
        int index = partialMultiplicationNo;
        if (k <= partialMultiplicationNo || index >= C.getElementCount()) {
            throw new MatrixMultiplicationException("MatrixMultiplicationException: multiplyByRowForEveryKthElement");
        }

        while(index < C.getElementCount()) {
            int rowIndex = index / C.getColumnCount();
            int columnIndex = index % C.getColumnCount();

            int element = multiplyForElement(A, B, rowIndex, columnIndex);
            C.getElements().get(rowIndex).set(columnIndex, element);

            index += k;
        }
    }
}
