package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlOrderBy;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java.util.Comparator;

import static com.google.common.base.Preconditions.checkNotNull;

public class GridRowDataDtoComparatorFactory {


    @Nonnull
    private final GridCellDataDtoComparator cellComparator;

    @Inject
    public GridRowDataDtoComparatorFactory(@Nonnull GridCellDataDtoComparator cellComparator) {
        this.cellComparator = checkNotNull(cellComparator);
    }

    @Nonnull
    public Comparator<GridRowDataDto> get(@Nonnull ImmutableList<GridControlOrderBy> orderBy,
                                          @Nonnull GridControlDescriptor descriptor) {
        ImmutableMap<GridColumnId, GridColumnId> leafColumnToTopLevelColumnMap = descriptor.getLeafColumnToTopLevelColumnMap();
        return orderBy.stream()
                      .map(order -> {
                          GridColumnId nestedColumnId = order.getColumnId();
                          GridColumnId topLevelColumnId = leafColumnToTopLevelColumnMap.get(nestedColumnId);
                          int columnIndex = getColumnIndex(descriptor, topLevelColumnId);
                          GridRowDtoByColumnIndexComparator rowComp = new GridRowDtoByColumnIndexComparator(
                                  cellComparator,
                                  columnIndex);
                          if(order.isAscending()) {
                              return rowComp;
                          }
                          else {
                              return rowComp.reversed();
                          }
                      })
                      .reduce(Comparator::thenComparing)
                      // Compare by the first column
                      .orElseGet(() -> new GridRowDtoByColumnIndexComparator(cellComparator, 0));
    }


    private static int getColumnIndex(@Nonnull GridControlDescriptor descriptor, GridColumnId mappedColumnId) {
        ImmutableList<GridColumnDescriptor> columns = descriptor.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            GridColumnId iColumnId = columns.get(i).getId();
            if (iColumnId.equals(mappedColumnId)) {
                return i;
            }
        }
        return -1;
    }
}
