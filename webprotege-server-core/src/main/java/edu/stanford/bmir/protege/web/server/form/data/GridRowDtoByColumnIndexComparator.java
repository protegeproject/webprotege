package edu.stanford.bmir.protege.web.server.form.data;

import edu.stanford.bmir.protege.web.shared.form.data.GridCellDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.GridRowDataDto;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionOrderingDirection;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.Comparator;

import static com.google.common.base.Preconditions.checkNotNull;

public class GridRowDtoByColumnIndexComparator implements Comparator<GridRowDataDto> {

    @Nonnull
    private final GridCellDataDtoComparator cellDataDtoComparator;

    private final FormRegionOrderingDirection orderingDirection;

    private final int columnIndex;

    public GridRowDtoByColumnIndexComparator(@Nonnull GridCellDataDtoComparator cellDataDtoComparator,
                                             FormRegionOrderingDirection orderingDirection,
                                             int columnIndex) {
        this.cellDataDtoComparator = checkNotNull(cellDataDtoComparator);
        this.orderingDirection = orderingDirection;
        this.columnIndex = columnIndex;
    }

    @Override
    public int compare(GridRowDataDto o1, GridRowDataDto o2) {
        var cellDataDto1 = o1.getCells()
                             .get(columnIndex);
        var cellDataDto2 = o2.getCells()
                             .get(columnIndex);
        var diff = cellDataDtoComparator.compare(cellDataDto1, cellDataDto2);
        if(orderingDirection.isAscending()) {
            return diff;
        }
        else {
            return -diff;
        }
    }

    public int getColumnIndex() {
        return columnIndex;
    }
}
