package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-30
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GridRowData {

    public static GridRowData get(@Nonnull ImmutableList<GridCellData> cellData) {
        return new AutoValue_GridRowData(cellData);
    }

    @Nonnull
    public abstract ImmutableList<GridCellData> getCells();
}
