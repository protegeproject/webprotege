package edu.stanford.bmir.protege.web.server.form.data;

import edu.stanford.bmir.protege.web.shared.form.data.GridCellDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.GridRowDataDto;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Comparator;

import static com.google.common.base.Preconditions.checkNotNull;

public class GridRowDtoByColumnIndexComparator implements Comparator<GridRowDataDto> {

    @Nonnull
    private final GridCellDataDtoComparator cellDataDtoComparator;

    private final int columnIndex;

    @Inject
    public GridRowDtoByColumnIndexComparator(@Nonnull GridCellDataDtoComparator cellDataDtoComparator,
                                             int columnIndex) {
        this.cellDataDtoComparator = checkNotNull(cellDataDtoComparator);
        this.columnIndex = columnIndex;
    }

    @Override
    public int compare(GridRowDataDto o1, GridRowDataDto o2) {
        GridCellDataDto cellDataDto1 = o1.getCells()
                                         .get(columnIndex);
        GridCellDataDto cellDataDto2 = o2.getCells()
                                         .get(columnIndex);
        return cellDataDtoComparator.compare(cellDataDto1, cellDataDto2);
    }

    public int getColumnIndex() {
        return columnIndex;
    }
}
