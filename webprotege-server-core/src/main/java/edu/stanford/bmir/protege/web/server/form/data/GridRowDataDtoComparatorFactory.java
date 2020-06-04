package edu.stanford.bmir.protege.web.server.form.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.stanford.bmir.protege.web.shared.form.data.GridRowDataDto;
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
        var leafColumnToTopLevelColumnMap = descriptor.getLeafColumnToTopLevelColumnMap();
        return orderBy.stream()
                      .map(order -> {
                          var nestedColumnId = order.getColumnId();
                          var topLevelColumnId = leafColumnToTopLevelColumnMap.get(nestedColumnId);
                          int columnIndex = getColumnIndex(descriptor, topLevelColumnId);
                          var rowComp = new GridRowDtoByColumnIndexComparator(
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
        var columns = descriptor.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            var iColumnId = columns.get(i).getId();
            if (iColumnId.equals(mappedColumnId)) {
                return i;
            }
        }
        return -1;
    }
}
