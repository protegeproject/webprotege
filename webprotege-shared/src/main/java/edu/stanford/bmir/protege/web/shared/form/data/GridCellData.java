package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.form.field.GridColumnId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-30
 */
@AutoValue
@GwtCompatible(serializable = true)
public abstract class GridCellData {

    public static GridCellData get(@Nonnull GridColumnId columnId,
                                   @Nullable ImmutableList<FormControlData> values) {
        return new AutoValue_GridCellData(columnId, values);
    }

    @Nonnull
    public abstract GridColumnId getColumnId();

    @Nonnull
    public abstract ImmutableList<FormControlData> getValues();
}
