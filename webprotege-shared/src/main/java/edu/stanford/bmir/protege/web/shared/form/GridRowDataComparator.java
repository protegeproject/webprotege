package edu.stanford.bmir.protege.web.shared.form;

import edu.stanford.bmir.protege.web.shared.form.data.GridCellDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.GridRowDataDto;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlOrderByDirection;

import javax.annotation.Nonnull;
import java.util.Comparator;

import static com.google.common.base.Preconditions.checkNotNull;

public class GridRowDataComparator implements Comparator<GridRowDataDto> {

    private final int columnIndex;

    @Nonnull
    private final GridControlOrderByDirection direction;

    public GridRowDataComparator(int columnIndex, @Nonnull GridControlOrderByDirection direction) {
        this.columnIndex = columnIndex;
        this.direction = checkNotNull(direction);
    }

    @Override
    public int compare(GridRowDataDto o1, GridRowDataDto o2) {
        GridCellDataDto c1 = o1.getCells().get(columnIndex);
        GridCellDataDto c2 = o2.getCells().get(columnIndex);
        return c1.compareTo(c2) * direction.getDir();
    }
}

