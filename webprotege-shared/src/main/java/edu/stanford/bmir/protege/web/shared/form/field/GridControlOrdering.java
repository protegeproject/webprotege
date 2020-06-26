package edu.stanford.bmir.protege.web.shared.form.field;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.GridRowDataComparator;
import edu.stanford.bmir.protege.web.shared.form.data.GridRowDataDto;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Objects;

@AutoValue
@GwtCompatible(serializable = true)
@Nonnull
public abstract class GridControlOrdering {

    public static GridControlOrdering get(@Nonnull FormFieldId formFieldId,
                                          @Nonnull ImmutableList<GridControlOrderBy> ordering) {
        return new AutoValue_GridControlOrdering(formFieldId, ordering);
    }

    @Nonnull
    public abstract FormFieldId getFieldId();

    @Nonnull
    public abstract ImmutableList<GridControlOrderBy> getOrdering();

    public Comparator<GridRowDataDto> getComparator(GridControlDescriptor descriptor) {
        ImmutableList<GridControlOrderBy> ordering = getOrdering();
        return ordering.stream()
                       .map(orderBy -> getComparator(orderBy, descriptor))
                       .filter(Objects::nonNull)
                       .reduce(Comparator::thenComparing)
                       .orElseGet(Comparator::naturalOrder);
    }

    @Nullable
    private Comparator<GridRowDataDto> getComparator(GridControlOrderBy orderBy,
                                                     GridControlDescriptor descriptor) {
        ImmutableList<GridColumnDescriptor> columns = descriptor.getColumns();
        for (int i = 0; i < columns.size(); i++) {
            GridColumnDescriptor columnDescriptor = columns.get(i);
            if (orderBy.getColumnId().equals(columnDescriptor.getId())) {
                return new GridRowDataComparator(i, orderBy.getDirection());
            }
        }
        return null;
    }
}
