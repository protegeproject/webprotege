package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.form.FilterState;
import edu.stanford.bmir.protege.web.shared.form.HasFilterState;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionOrdering;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class GridControlDataDto implements FormControlDataDto, HasFilterState {

    @Nonnull
    public static GridControlDataDto get(@Nonnull GridControlDescriptor descriptor,
                                         @Nonnull Page<GridRowDataDto> rows,
                                         @Nonnull ImmutableSet<FormRegionOrdering> ordering,
                                         int depth,
                                         @Nonnull FilterState filterState) {
        return new AutoValue_GridControlDataDto(depth, descriptor, rows, ordering, filterState);
    }

    @Nonnull
    public abstract GridControlDescriptor getDescriptor();

    @Nonnull
    public abstract Page<GridRowDataDto> getRows();

    @Nonnull
    public abstract ImmutableSet<FormRegionOrdering> getOrdering();

    @Override
    public <R> R accept(FormControlDataDtoVisitorEx<R> visitor) {
        return visitor.visit(this);
    }

    @Nonnull
    @Override
    public GridControlData toFormControlData() {
        return GridControlData.get(getDescriptor(),
                                   getRows().transform(GridRowDataDto::toGridRowData),
                                   getOrdering());
    }

    @Nonnull
    @Override
    public abstract FilterState getFilterState();

    /**
     * Determines whether this grid is empty because it has been filtered empty.
     * @return true if this grid is empty and this is due to filtering
     */
    public boolean isFilteredEmpty() {
        return getFilterState().equals(FilterState.FILTERED) &&
                getRows().getTotalElements() == 0;
    }
}
