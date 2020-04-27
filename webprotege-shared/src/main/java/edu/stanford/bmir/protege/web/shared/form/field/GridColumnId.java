package edu.stanford.bmir.protege.web.shared.form.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.auto.value.AutoValue;
import com.google.common.annotations.GwtCompatible;

import javax.annotation.Nonnull;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-25
 */
@GwtCompatible(serializable = true)
@AutoValue
public abstract class GridColumnId implements FormRegionId {

    @JsonCreator
    @Nonnull
    public static GridColumnId get(@Nonnull String id) {
        return new AutoValue_GridColumnId(id);
    }

    @Override
    @JsonValue
    public abstract String getId();
}
