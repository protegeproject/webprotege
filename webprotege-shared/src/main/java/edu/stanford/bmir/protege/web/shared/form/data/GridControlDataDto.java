package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import edu.stanford.bmir.protege.web.shared.form.field.FormRegionOrdering;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlDescriptor;
import edu.stanford.bmir.protege.web.shared.pagination.Page;

import javax.annotation.Nonnull;

@AutoValue
@GwtCompatible(serializable = true)
public abstract class GridControlDataDto implements FormControlDataDto {

    @Nonnull
    public static GridControlDataDto get(@Nonnull GridControlDescriptor descriptor,
                                         @Nonnull Page<GridRowDataDto> rows,
                                         @Nonnull ImmutableSet<FormRegionOrdering> ordering) {
        return new AutoValue_GridControlDataDto(descriptor, rows, ordering);
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
}
