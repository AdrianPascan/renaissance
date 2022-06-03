package org.renaissance.jdk.concurrent.matrix;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class Matrix {

    private int rowCount;
    private int columnCount;
    private Vector<Vector<Integer>> elements; //thread-safe

    public Matrix(int order) {
        this.rowCount = this.columnCount = order;
        initialiseElements();
    }

    public Matrix(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
        initialiseElements();
    }

    public Matrix(int rowCount, int columnCount, int startElement) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;

        this.elements = new Vector<>(rowCount);
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            Vector<Integer> row = new Vector<>(columnCount);
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                row.add(startElement + rowIndex * rowCount + columnIndex);
            }
            this.elements.add(row);
        }
    }

    public Matrix(int rowCount, int columnCount, List<Integer> elements) {
        if (elements.size() != rowCount * columnCount) {
            throw new MatrixException("MatrixException: no. of elements is different than the capacity of the matrix");
        }

        this.rowCount = rowCount;
        this.columnCount = columnCount;

        this.elements = new Vector<>(rowCount);
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            Vector<Integer> row = new Vector<>(elements.subList(rowIndex * columnCount, (rowIndex + 1) * columnCount));
            this.elements.add(row);
        }
    }

    private void initialiseElements() {
        elements = new Vector<>(rowCount);
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            Vector<Integer> row = new Vector<>(columnCount);
            row.addAll(Collections.nCopies(columnCount, 0));
            elements.add(row);
        }
    }

    public int getElementCount() {
        return rowCount * columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public Vector<Vector<Integer>> getElements() {
        return elements;
    }

    public void setElements(Vector<Vector<Integer>> elements) {
        this.elements = elements;
    }
}
