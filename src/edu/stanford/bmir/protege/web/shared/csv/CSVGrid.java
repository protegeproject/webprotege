package edu.stanford.bmir.protege.web.shared.csv;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkElementIndex;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Author: Matthew Horridge<br>
 * Stanford University<br>
 * Bio-Medical Informatics Research Group<br>
 * Date: 19/05/2013
 */
public class CSVGrid implements IsSerializable {

    private List<CSVRow> grid = new ArrayList<CSVRow>();

    private int columnCount;

    private CSVGrid() {
    }

    private CSVGrid(List<CSVRow> grid) {
        this.grid.addAll(checkNotNull(grid));
        int maxRowSize = 0;
        for (CSVRow row : grid) {
            final int rowSize = row.size();
            if (rowSize > maxRowSize) {
                maxRowSize = rowSize;
            }
        }
        columnCount = maxRowSize;
    }

    public List<CSVRow> getRows() {
        return new ArrayList<CSVRow>(grid);
    }

    public int getRowCount() {
        return grid.size();
    }

    public int getColumnCount() {
        return columnCount;
    }

    /**
     * Gets the value at a given row and column.
     * @param rowIndex The row index.  Must be greater or equal to zero and less than the row count.
     * @param columnIndex The column index.  Must be greater or equal to zero and less than the column count.
     * @return The value at the specified row and column.  Not {@code null}.
     * @throws IndexOutOfBoundsException if the row or column indexes are out of bounds.
     */
    public String getValueAt(int rowIndex, int columnIndex) {
        checkElementIndex(rowIndex, getRowCount());
        checkElementIndex(columnIndex, getColumnCount());

        CSVRow rowValues = grid.get(rowIndex);
        if(columnIndex >= rowValues.size()) {
            return "";
        }
        else {
            return rowValues.getColumnValue(columnIndex);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private List<CSVRow> rows = new ArrayList<CSVRow>();

        public void addRow(CSVRow row) {
            checkNotNull(row);
            rows.add(row);
        }

        public void addRow(String ... rowItems) {
            addRow(Arrays.asList(rowItems));
        }

        public void addRow(List<String> rowItems) {
            rows.add(new CSVRow(rowItems));
        }

        public CSVGrid build() {
            return new CSVGrid(rows);
        }


    }



}

