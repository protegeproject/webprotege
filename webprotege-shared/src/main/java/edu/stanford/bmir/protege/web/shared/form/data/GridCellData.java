package edu.stanford.bmir.protege.web.shared.form.data;

import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;
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
                                   @Nullable FormControlData value) {
        return new AutoValue_GridCellData(columnId, value);
    }

    @Nonnull
    public abstract GridColumnId getColumnId();

    @Nullable
    protected abstract FormControlData getValueInternal();

    @Nonnull
    public Optional<FormControlData> getValue() {
        return Optional.ofNullable(getValueInternal());
    }
}
